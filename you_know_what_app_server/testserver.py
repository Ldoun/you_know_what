import flask
from flask import request
from pos import get_NNG
import pymysql
import random
from konlpy.tag import Okt
import json
okt=Okt()
app = flask.Flask(__name__)
stac=pymysql.connect(
     user='stac',
     passwd='',
     db='stac_server',
     charset='utf8'
     )
cursor=stac.cursor()

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

app.run(host="0.0.0.0", port=5000, debug=True)
