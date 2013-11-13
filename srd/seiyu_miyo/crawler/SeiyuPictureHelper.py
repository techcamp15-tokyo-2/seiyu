__author__ = 'zhangxinzheng'
# -*- coding: utf-8 -*-

import urllib2
from time import gmtime, strftime ,sleep
import re

from BeautifulSoup import *
import pymongo

from SeiyuPicture import SeiyuPicture


class SeiyuPictureHelper(object):
    _instance = ''
    @classmethod
    def instance(cls):
        if not cls._instance:
            cls._instance = cls()
            cls._instance.start()
        return cls._instance

    def start(self):
        self.db = pymongo.Connection().test

    def updateSeiyuPictureInfo(self, seiyuName):
        mdbIn = self.db.seiyu.find_one({"seiyuName": seiyuName})
        httpRe = re.compile('http://\S+/\S+/')
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
                        #print url
                        html = ""
                        tryCount = 0
                        try:
                            html = urllib2.urlopen(url).read()
                        except urllib2.HTTPError, err:
                            if err.code == 500:
                                sleep(2)
                                tryCount += 1
                                if tryCount > 20:
                                    continue
                                html = urllib2.urlopen(url).read()
                            else:
                                pass

                        except urllib2.URLError, err:
                            print err
                            continue
                        soup = BeautifulSoup(html)
                        imgBoxList = soup.find(id="imgList")
                        if imgBoxList:
                            allImgList = imgBoxList.findAll("li", {"class": "imgBox"})
                            for k in xrange(0 , len(allImgList)):
                                pic = SeiyuPicture(allImgList[k])
                                pic.seiyuname = seiyuName
                                pic.prefix = mdbIn["prefix"]
                                pic.index = str(k)
                                pic.timeSmap = str(i) + str(j).zfill(2)

                                mdbPic = self.db.seiyuPicture.find_one({"blogUrl": {'$regex': pic.prefix[0:9]+"e*" + pic.prefix[9:] + "*"}, "timeSmap": pic.timeSmap, "index": pic.index})
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
                        #print url
                        html = ""
                        tryCount = 0
                        try:
                            html = urllib2.urlopen(url).read()
                        except urllib2.HTTPError, err:
                            print err
                            if err.code == 500:
                                sleep(2)
                                tryCount += 1
                                if tryCount > 20:
                                    continue
                                html = urllib2.urlopen(url).read()
                            else:
                                noData = True

                        except urllib2.URLError, err:
                            print err
                            continue
                        soup = BeautifulSoup(html)
                        imgBoxList = soup.find(id="imgList")
                        if imgBoxList:
                            allImgList = imgBoxList.findAll("li", {"class": "imgBox"})
                            for k in xrange(0, len(allImgList)):
                                pic = SeiyuPicture(allImgList[k])
                                pic.seiyuname = seiyuName
                                pic.prefix = mdbIn["prefix"]
                                pic.index = str(k)
                                pic.timeSmap = str(i) + str(j).zfill(2)
                                mdbPic = self.db.seiyuPicture.find_one({"blogUrl": {'$regex': pic.prefix[0:9]+"e*" + pic.prefix[9:] + "*"}, "timeSmap": pic.timeSmap, "index": pic.index})
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
