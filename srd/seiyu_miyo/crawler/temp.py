__author__ = 'zhangxinzheng'
# -*- coding: utf-8 -*-

import pymongo

db = pymongo.Connection().test.seiyu
user = db.find_one({"seiyuName": u"福原香織"})
aa = user["_id"]
user.pop("_id",None)
user.pop("latestCrawlerTime",None)
db.update({"_id":aa},user)
