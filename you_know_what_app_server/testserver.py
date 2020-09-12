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

@app.route('/login', methods=['GET', 'POST'])
def login():
    from_android=request.data.decode("utf-8")
    user=json.loads(from_android)

    sql="insert into user(password,email) values('"+user['pwd']+"','"+user['email']+"');"
    sql2="select LAST_INSERT_ID()";
    print('here')
    cursor.execute(sql)
    stac.commit()
    cursor.execute(sql2)
    result=cursor.fetchone()

    return str(result[0])

@app.route('/topic', methods=['GET', 'POST'])
def topic_tip():
    from_android=request.data.decode("utf-8")
    user=json.loads(from_android)
    subject=okt.nouns(user["topic"])[0]    

    sql="select script from tip where script LIKE '%"+subject+"%';"
    cursor.execute(sql)
    result=cursor.fetchall()
    print(result)
    tip=random.choice(result)[0]
    print(tip)
    #sql="insert into presaw(uid, script, date) values('"+user['email']+"','"+tip+"', now());"
    #cursor.execute(sql)
    return tip

@app.route('/review', methods=['GET', 'POST'])
def handle_request():
    from_android=request.data.decode("utf-8")
    user=json.loads(from_android)
    email=user["email"]

@app.route('/Topic_tip', methods=['POST'])
def Topic_tip():
    req_json = request.json
    print(req_json)
    response = {
        "version": "2.0",
        "resultCode": "OK",
        "output": {
            "Topic": req_json['action']['parameters']['Topic']['value'],
            "topic_tip": 1,
            "topic_tip1": 2,
            "topic_tip2": 3
            },
        "directives": []
        }
    return jsonify(response)
@app.route('/random_tip', methods=['POST'])
def random_tip():
    req_json = request.json
    print(req_json)
    #tips = get_Tips()
    response = {
            "version": "2.0",
            "resultCode": "OK",
            "output": {
                "random" : req_json['action']['parameters']['random']['value'],
                "tip": 1
                },
            "directives": []
            }
    return jsonify(response)
if __name__=="__main__":
    ssl_context=ssl.SSLContext(ssl.PROTOCOL_TLS)
    ssl_context.load_cert_chain(certfile='/etc/letsencrypt/live/common.stac-know.tk/fullchain.pem',keyfile='/etc/letsencrypt/live/common.stac-know.tk/privkey.pem',password='')

    app.run(host="0.0.0.0",port=5000,debug=True,ssl_context=ssl_context)
