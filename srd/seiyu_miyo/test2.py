__author__ = 'zhangxinzheng'
import pymongo
db = pymongo.Connection().test.seiyu

for i in db.find():
    print i["seiyuName"]