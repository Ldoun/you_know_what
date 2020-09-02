from konlpy.tag import Kkma
'''kkma = Kkma()
x=kkma.pos('고양이에 대해 알려줘')
print(type(x[0]))

for i in x:
    if 'NNG' in i:
        print(i[0])'''



def get_NNG(sentence):
    kkma=Kkma()
    x=kkma.nouns(sentence)

    '''print(x)
    for i in x:
        if 'NNG' in i:'''
    return x[0]
