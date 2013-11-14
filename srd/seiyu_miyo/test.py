__author__ = 'zhangxinzheng'
# -*- coding: utf-8 -*-

import pymongo
import re
from BeautifulSoup import *
import urllib2
from time import sleep
db = pymongo.Connection().test.seiyu

curous = db.find()

for i in curous:
    print i["prefix"]

    html = ""
    tryCount = 0
    try:
        html = urllib2.urlopen(i["prefix"]).read()
    except urllib2.HTTPError, err:
        print err
        if err.code == 500:
            sleep(2)
            tryCount += 1
            if tryCount > 20:
                continue
            html = urllib2.urlopen(i["prefix"]).read()
        else:
            noData = True

    except urllib2.URLError, err:
        print err
        continue

    soup = BeautifulSoup(html)
    profile = soup.find(id="new_profile")
    if not profile:
        db.update({"_id": i["_id"]}, {"$set": {"gender": "-1"}})
        continue
    sex = profile.find("li", {"class": "sex"})
    if sex:
        gender = "-1"
        if sex.string == u'\u6027\u5225\uff1a\u5973\u6027':
            gender = "0"
            print 0
        elif sex.string == u'\u6027\u5225\uff1a\u7537\u6027':
            gender = "1"
            print 1
        else:
            gender = "-1"
    else:
        gender = "-1"
    db.update({"_id": i["_id"]}, {"$set": {"gender": gender}})




#if sex == u'\u6027\u5225\uff1a\u5973\u6027':
#    print "female"
#else:
#    print "not"
##for i in curous:
