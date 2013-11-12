__author__ = 'zhangxinzheng'

import urllib2
from BeautifulSoup import *
from Seiyu import Seiyu
from Singleton import Singleton
import pymongo

class SeiyuHelper(Singleton):
    def __init__(self):
        self.db = pymongo.Connection().test


    def updateSeiyuInfo(self):
        seiyuUrl = ["http://official.ameba.jp/genrekana/kana22-"+str(i)+".html" for i in xrange(1, 44)]
        for i in seiyuUrl:
            html = urllib2.urlopen(i).read()
            soup = BeautifulSoup(html)
            for j in soup.findAll("dl", {"class": "clr"}):
                self.db.seiyu.insert(Seiyu(j).getDict())




SeiyuHelper().updateSeiyuInfo()