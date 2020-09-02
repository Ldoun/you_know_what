from krwordrank.word import KRWordRank
from krwordrank.hangle import normalize

texts=['고양이란 무엇일까','난 왜 이 고양이란 생물에게 집착을 하는 것일까']
texts = [normalize(text, english=True, number=True) for text in texts]

wordrank_extractor = KRWordRank(
    min_count = 5, # 단어의 최소 출현 빈도수 (그래프 생성 시)
    max_length = 10, # 단어의 최대 길이
    verbose = True
    )

beta = 0.85    # PageRank의 decaying factor beta
max_iter = 10

keywords, rank, graph = wordrank_extractor.extract(texts, beta, max_iter)

for word, r in sorted(keywords.items(), key=lambda x:x[1], reverse=True)[:30]:
    print('%8s:\t%.4f' % (word, r))
