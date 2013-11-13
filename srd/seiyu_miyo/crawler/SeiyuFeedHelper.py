__author__ = 'zhangxinzheng'

import urllib2
import BeautifulSoup
import Singleton
from time import sleep

class SeiyuFeedHelper(Singleton):
    def __init__(self):
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
