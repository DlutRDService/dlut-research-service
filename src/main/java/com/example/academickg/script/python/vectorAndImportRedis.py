#! /usr/bin/python3.10
# -*- coding: utf-8 -*-

from sentence_transformers import SentenceTransformer
from createDatabase import createCollection
from sentenceVector import sentenceVector
import sys
import redis



if __name__ == "__main__":
    r = redis.Redis(host='localhost', port=6379)
    query_vector = sentenceVector(sys.argv[1])
    query_vector = query_vector.tolist()
    r.ltrim("AcademicKG:queryvector:", -1, -1)
    r.lpop("AcademicKG:queryvector:")
    r.rpush("AcademicKG:queryvector:", *query_vector)
    r.expire("AcademicKG:queryvector:", 3600)
