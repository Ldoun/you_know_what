import pymysql
import csv
    
file='/home/ubuntu/you_know_what/you_know_what_app_server/sql_py/tip.csv'
#file='C:/workspace/sql_py/tip.csv'
f=open(file, 'r', encoding='UTF8')
data=list(csv.reader(f))

stac=pymysql.connect(
    user='stac',
    passwd='',
    host='127.0.0.1',
    #db='stac_test'
    db='stac_server'
    charset ='utf8'
)

cursor = stac.cursor()
sql = "INSERT INTO `tip`(script) VALUES (%s);"
cursor.executemany(sql, data)
stac.commit()
