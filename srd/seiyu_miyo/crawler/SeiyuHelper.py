__author__ = 'zhangxinzheng'

import urllib2
from BeautifulSoup import *
from Seiyu import Seiyu
from Singleton import Singleton
import pymongo
import sys

class SeiyuHelper(Singleton):
    def __init__(self):
        self.db = pymongo.Connection().test


    def updateSeiyuInfo(self):
        seiyuUrl = ["http://official.ameba.jp/genrekana/kana22-"+str(i)+".html" for i in xrange(1, 44)]
        for i in seiyuUrl:
            html = urllib2.urlopen(i).read()
            soup = BeautifulSoup(html)
            for j in soup.findAll("dl", {"class": "clr"}):
                seiyu = Seiyu(j).getDict()
                mdbIn = self.db.seiyu.find_one({"seiyuName": seiyu["seiyuName"]})
                if mdbIn:
                    sys.stdout.write("update seiyu Info %s\n" % seiyu["seiyuName"])
                    self.db.seiyu.update({"_id": mdbIn["_id"]}, seiyu)
                else:
                    sys.stdout.write("insert seiyu Info %s\n" % seiyu["seiyuName"])
                    self.db.seiyu.insert(seiyu)

SeiyuHelper().updateSeiyuInfo()