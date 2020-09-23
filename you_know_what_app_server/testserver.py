import flask
import ssl
from flask import request,jsonify
from pos import get_NNG
import pymysql
import random
from konlpy.tag import Okt
import json
from flask_talisman import Talisman

okt=Okt()
app = flask.Flask(__name__)

stac=pymysql.connect(
     user='stac',
     passwd='',
     db='stac_server',
     charset='utf8'
     )
cursor=stac.cursor()

@app.route('/delete', methods=['GET', 'POST'])
def delete():
    from_android=request.data.decode("utf-8")
    user=json.loads(from_android)
    sql="delete from presaw where uid="+user['uid']+"and script ='"+user['tip']+"';"
    cursor.execute(sql)
    stac.commit()
    return ""

@app.route('/email', methods=['GET', 'POST'])
def check():
    from_android=request.data.decode("utf-8")
    user=json.loads(from_android)
    sql="select `index` from `user` where `email`= '"+user['email']+"';"
    cursor.execute(sql)
    result=cursor.fetchone()
    print(result)
    if(result):
        print("fail")
        return "fail"
    else:
        return "success"

@app.route('/device', methods=['GET', 'POST'])
def link():
    from_android=request.data.decode("utf-8")
    user=json.loads(from_android)
    print(user)
    sql="insert into device(`uid`,`device`) values("+user['uid']+",'"+user['device']+"');"
    cursor.execute(sql)
    stac.commit()
    return "success"

@app.route('/login', methods=['GET', 'POST'])
def login():
    from_android=request.data.decode("utf-8")
    user=json.loads(from_android)
    print(user)
    sql="select `index` from `user` where `email` = '"+user['email']+"' and `password`='"+user['pwd']+"';" 
    cursor.execute(sql)
    result=cursor.fetchone()
    print(result)
    if(result):
        print(result[0])
        return str(result[0])
    else:
        return "0" 

@app.route('/register', methods=['GET', 'POST'])
def regi():
    from_android=request.data.decode("utf-8")
    user=json.loads(from_android)
    print(user)
    sql="insert into user(password,email) values('"+user['pwd']+"','"+user['email']+"');"
    
    sql2="select LAST_INSERT_ID()";
    
    cursor.execute(sql)
    stac.commit()
    cursor.execute(sql2)
    result=cursor.fetchone()
    print(result)
    return str(result[0])

@app.route('/topic', methods=['GET', 'POST'])
def topic_tip():
    from_android=request.data.decode("utf-8")
    user=json.loads(from_android)
    subject=okt.nouns(user["topic"])[0]    
    if(subject=="상식" or subject=="랜덤" or subject=="램덤" or subject==""):
        tip= get_random_tip()
    else:
        try:
            sql="select script from tip where script LIKE '%"+subject+"%';"
            cursor.execute(sql)
            result=cursor.fetchall()
            print(user)
            tip=random.choice(result)[0]
        except:
            return "해당하는 상식 없음"
    print(tip)
    sql="insert into presaw(uid, script, date) values('"+user['usernum']+"','"+tip+"', now());"
    cursor.execute(sql)
    stac.commit()
    print(tip)
    return tip

@app.route('/review', methods=['GET', 'POST'])
def handle():
    from_android=request.data.decode("utf-8")
    user=json.loads(from_android)
    uid=user["id"]
    sql='select script from presaw where `uid`="'+uid+'";'
    cursor.execute(sql)
    result=cursor.fetchall()
    return list2String(result)

@app.route('/Topic_tip_v2', methods=['POST'])
def one_tip():
    req_json = request.json
    print(req_json)
    #tips = get_Tips
    tips=get_topic_tip(req_json['action']['parameters']['Topic']['value'])
    response = {
    "version": "2.0",
    "resultCode": "OK",
    "output": {
        "Topic" : req_json['action']['parameters']['Topic']['value'],
        "tip":tips
        },
    "directives": []
    }
    sql="select `uid` from device where device='"+req_json['profile']['privatePlay']['deviceUniqueId']+"';"
    cursor.execute(sql)
    try:
        uid=cursor.fetchone()[0]
    except:
        return jsonify(response)

    print(uid)
    if(uid):
        sql="insert into presaw(uid, script, date) values("+str(uid)+",'"+tips+"', now());"
        cursor.execute(sql)
        stac.commit()   
    return jsonify(response)
    
@app.route('/Topic_tip', methods=['POST'])
def Topic_tip():
    req_json = request.json
    print(req_json)
    tips=get_topic_tip(req_json['action']['parameters']['Topic']['value'])
    print(tips)
    response = {
        "version": "2.0",
        "resultCode": "OK",
        "output": {
            "Topic": req_json['action']['parameters']['Topic']['value'],
            "topic_tip":tips[0][0],
            "topic_tip1": tips[1][0],
            "topic_tip2":tips[2][0]
            },
        "directives": []
        }
    
    sql="select `uid` from device where device='"+req_json['context']['device']['profile']['privatePlay']['deviceUniqueId']+"';"
    cursor.execute(sql)
    uid=cursor.fetchone()[0]
    if(uid):
        for i in range(0,3):
            sql="insert into presaw(uid, script, date) values('"+uid+"','"+tip[i][0]+"', now());"
            cursor.execute(sql)
        stac.commit()
    return jsonify(response)
@app.route('/Random_tip', methods=['POST'])
def Random():
    req_json = request.json
    print(req_json)
    tips = get_tip()
    response = {  
            "version": "2.0",
            "resultCode": "OK",
            "output": {
                "random" : req_json['action']['parameters']['random']['value'],
                "topic_tip":tips[0][0],
                "topic_tip1": tips[1][0],
                "topic_tip2":tips[2][0]
                },
            "directives": []
            }
    return jsonify(response)

@app.route('/random_tip', methods=['POST'])
def random_tip():
    req_json = request.json
    print(req_json)
    #tips = get_Tips
    response = {
            "version": "2.0",
            "resultCode": "OK",
            "output": {
                "random" : req_json['action']['parameters']['random']['value'],
                "tip":get_tip() 
                },
            "directives": []
            }
    return jsonify(response)

@app.route('/test', methods=['POST'])
def test():
    from_android=request.data.decode("utf-8")
    user=json.loads(from_android)
    print(user)
    return "테스트중"

def list2String(array):
    result=""
    for i in array:
        result+=str(i[0])
        result+=':'
       
    print(result)
    return result[:-1]

def get_topic_tip(subject):
    print(subject)
    sql="select script from tip where script LIKE '%"+subject+"%';"
    cursor.execute(sql)
    result=cursor.fetchall()
    tip = random.choice(result)[0]
    return tip

def get_random_tip():
    tip_count=10
    i=random.randint(1,10)
    sql="select script from tip where num="+str(i)+";"
    cursor.execute(sql)
    tip=cursor.fetchone()[0]
    print(tip)
    return tip

#for random
def get_tip():
    tip_count=9
    tips=[]
    i=range(1,tip_count+1)
    tip_id=random.sample(i,3)
    for  i in range(3):
        sql="select script from tip where num="+str(tip_id[i])+";"
        cursor.execute(sql)
        tips.append(cursor.fetchall()[0])
    
    print(tips)
    return tips

if __name__=="__main__":
    ssl_context=ssl.SSLContext(ssl.PROTOCOL_TLS)
    ssl_context.load_cert_chain(certfile='/etc/letsencrypt/live/common.stac-know.tk/fullchain.pem',keyfile='/etc/letsencrypt/live/common.stac-know.tk/privkey.pem',password='')

    app.run(host="0.0.0.0",port=5000,debug=True,ssl_context=ssl_context)
