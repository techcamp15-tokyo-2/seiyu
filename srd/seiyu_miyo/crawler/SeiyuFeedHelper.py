from crawler import Singleton

__author__ = 'zhangxinzheng'

import urllib2
from BeautifulSoup import *
from time import sleep

class SeiyuFeedHelper(object):
    _instance = ''
    @classmethod
    def instance(cls):
        if not cls._instance:
            cls._instance = cls()
            cls._instance.start()
        return cls._instance

    def start(self):
        self.feedList = []
        if len(self.feedList) == 0:
            self.updateFeedInfo()



    def updateFeedInfo(self):
        feedUrl = ["http://official.ameba.jp/genreupdateimage/updateimage22_"+str(i)+".html" for i in xrange(1, 10)]
        ansList = []
        html = ""
        for i in feedUrl:
            try:
                html = urllib2.urlopen(i).read()
            except urllib2.HTTPError, err:
                print err
                if err.code == 500:
                    sleep(2)
                    html = urllib2.urlopen(i).read()
                elif err.code == 404:
                    continue
            except urllib2.URLError, err:
                print err
                continue
            soup = BeautifulSoup(html)
            imgBoxSoup = soup.find("ul", {"class": "new_photoUl clr"}).findAll("li")
            for singImgBox in imgBoxSoup:
                ansList.append(singImgBox.findAll("p")[1].find("a").string)

        self.feedList = ansList
