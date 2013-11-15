__author__ = 'zhangxinzheng'
# -*- coding: utf-8 -*-

import pymongo
db = pymongo.Connection().test.seiyuPicture

print db.find().count()