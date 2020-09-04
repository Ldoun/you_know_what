import flask
from flask import request
from pos import get_NNG
import pymysql
import random

app = flask.Flask(__name__)


@app.route('/', methods=['GET', 'POST'])
def handle_request():
    from_android=request.data.decode("utf-8")
        
    subject=get_NNG(from_android)  
    try:
        subject=subject[1]
    except:
        subject=subject[0]


    stac=pymysql.connect(
    user='stac',
    passwd='',
    host='127.0.0.1',
    #db='stac_test'
    db='stac_server',
    charset='utf8'
    )
    cursor = stac.cursor()

    sql="select script from tip where script LIKE '%"+subject+"%';"
    cursor.execute(sql)
    result = cursor.fetchall()
    tip=random.choice(result)[0]
    return tip

app.run(host="0.0.0.0", port=5000, debug=True)
