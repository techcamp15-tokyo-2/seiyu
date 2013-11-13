__author__ = 'zhangxinzheng'
# -*- coding: utf-8 -*-

import pymongo
db = pymongo.Connection().test
a= db.seiyuPicture.find({"seiyuName": u'戸田恵子'}).sort("index").sort("timeSmap", pymongo.DESCENDING).limit(1)
print a.next()