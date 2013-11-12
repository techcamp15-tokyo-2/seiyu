__author__ = 'zhangxinzheng'
# -*- coding: utf-8 -*-

import urllib2
from BeautifulSoup import *
from Seiyu import Seiyu
from Singleton import Singleton
import pymongo
import sys
from time import gmtime, strftime ,sleep
from SeiyuPicture import SeiyuPicture

class SeiyuPictureHelper(Singleton):
    def __init__(self):
        self.db = pymongo.Connection().test


    def updateSeiyuPictureInfo(self, seiyuName):
        mdbIn = self.db.seiyu.find_one({"seiyuName": seiyuName})
        if mdbIn:
            if "latestCrawlerTime" in mdbIn.keys():
                # crawled data need Update
                latestTime = mdbIn["latestCrawlerTime"]
                oldyear, oldmonth = int(latestTime.split('-')[0]), int(latestTime.split('-')[1])
                newyear, newmonth = int(strftime('%Y', gmtime())), int(strftime('%m', gmtime()))
                for i in xrange(oldyear, newyear + 1):
                    if oldyear == newyear:
                        realMonth = newmonth
                    else:
                        realMonth = 12
                    for j in xrange(oldmonth, realMonth + 1):
                        url = mdbIn["prefix"] + "imagelist-" + str(i) + str(j).zfill(2) + ".html"
                        print url
                        html = ""
                        try:
                            html = urllib2.urlopen(url).read()
                        except urllib2.HTTPError, err:
                            if err.code == 500:
                                sleep(2)
                                html = urllib2.urlopen(url).read()
                            else:
                                pass

                        except urllib2.URLError, err:
                            print err
                            continue
                        soup = BeautifulSoup(html)
                        imgBoxList = soup.find(id="imgList")
                        if imgBoxList:
                            for k in imgBoxList.findAll("li", {"class": "imgBox"}):
                                pic = SeiyuPicture(k)
                                pic.seiyuname = seiyuName
                                pic.prefix = mdbIn["prefix"]
                                mdbPic = self.db.seiyuPicture.find_one({"imageUrl": pic.imageurl})
                                if mdbPic:
                                    print "update picture %s" % pic.imageurl
                                    self.db.seiyuPicture.update({"_id": mdbPic["_id"]}, pic.getDict())
                                else:
                                    print "insert %s" % pic.imageurl
                                    self.db.seiyuPicture.insert(pic.getDict())
                mdbIn["latestCrawlerTime"] = str(newyear) + "-" + str(realMonth)
                self.db.seiyu.update({"_id": mdbIn["_id"]}, mdbIn)
            else:
                # has not crawled data
                newyear, newmonth = int(strftime('%Y', gmtime())), int(strftime('%m', gmtime()))
                noData = False
                noDataFlag = 0
                for i in xrange(newyear, 1900, -1):
                    for j in xrange(1, 13):
                        url = mdbIn["prefix"] + "imagelist-" + str(i) + str(j).zfill(2) + ".html"
                        print url
                        html = ""
                        try:
                            html = urllib2.urlopen(url).read()
                        except urllib2.HTTPError, err:
                            print err
                            if err.code == 500:
                                sleep(2)
                                html = urllib2.urlopen(url).read()
                            else:
                                noData = True

                        except urllib2.URLError, err:
                            print err
                            continue
                        soup = BeautifulSoup(html)
                        imgBoxList = soup.find(id="imgList")
                        if imgBoxList:
                            for k in imgBoxList.findAll("li", {"class": "imgBox"}):
                                pic = SeiyuPicture(k)
                                pic.seiyuname = seiyuName
                                pic.prefix = mdbIn["prefix"]
                                mdbPic = self.db.seiyuPicture.find_one({"imageUrl": pic.imageurl})
                                if mdbPic:
                                    print "update picture %s" % pic.imageurl
                                    self.db.seiyuPicture.update({"_id": mdbPic["_id"]}, pic.getDict())
                                else:
                                    print "insert %s" % pic.imageurl
                                    self.db.seiyuPicture.insert(pic.getDict())
                                noDataFlag = 0
                        else:
                            noDataFlag += 1
                            if noDataFlag > 20:
                                noData = True

                        if noData:
                            break
                    if noData:
                        break
                mdbIn["latestCrawlerTime"] = str(newyear) + "-" + str(newmonth)
                self.db.seiyu.update({"_id": mdbIn["_id"]}, mdbIn)


    def updateSeiyuAllPictureInfo(self):
        for i in self.db.seiyu.find():
            self.updateSeiyuPictureInfo(i["seiyuName"])

SeiyuPictureHelper().updateSeiyuAllPictureInfo()