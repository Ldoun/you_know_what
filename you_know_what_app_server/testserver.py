import flask
from flask import request
from pos import get_NNG
import pymysql
import random

app = flask.Flask(__name__)


@app.route('/', methods=['GET', 'POST'])
def handle_request():
    from_android=request.data.decode("utf-8")
        
    
    return from_android

app.run(host="0.0.0.0", port=5000, debug=True)