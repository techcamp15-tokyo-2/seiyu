__author__ = 'zhangxinzheng'
import pymongo
db = pymongo.Connection().test
list = [u'52822d727aeca2448299a84e', u'52822d727aeca2448299a82e', u'52822d747aeca2448299a881', u'52822d757aeca2448299a8ab']
a=db.user.find({"followed":{"$in":list},"uid":{"$ne":"356825051203610"}})
print a.count()