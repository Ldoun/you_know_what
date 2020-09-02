import flask
from flask import request
from pos import get_NNG


app = flask.Flask(__name__)


@app.route('/', methods=['GET', 'POST'])
def handle_request():
    from_android=request.data.decode("utf-8")
    '''if(from_android[-7:]=='대해 알려 줘'):
        from_android=from_android[:-9]
    else:
        if(from_android[-16:]=='에 대한 새로운 사실 알려줘'):
            print(from_android[:-16])
        else:
            if(from_android[-4:]=='알려 줘'):
             from_android=from_android[:-5]'''
        
    subject=get_NNG(from_android)  

        
    return subject

app.run(host="0.0.0.0", port=5000, debug=True)