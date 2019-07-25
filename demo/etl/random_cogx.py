from pymongo import MongoClient
import pymongo
from random import randint

mongo = MongoClient()
strain = mongo['gcm']['strain']

for p in (strain.find()):
	p.update({'COG1':randint(1000,4000),'COG2':randint(1000,4000),'COG3':randint(1000,4000),'COG4':randint(1000,4000)})
	strain.replace_one({'_id': p['_id']}, p)