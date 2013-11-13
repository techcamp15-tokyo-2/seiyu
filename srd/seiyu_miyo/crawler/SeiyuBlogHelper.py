from crawler import Singleton

__author__ = 'zhangxinzheng'

from BeautifulSoup import *
import urllib2
import pymongo
class SeiyuBlogHelper(object):
    _instance = ''
    @classmethod
    def instance(cls):
        if not cls._instance:
            cls._instance = cls()
            cls._instance.start()
        return cls._instance

    def start(self):
        self.db = pymongo.Connection().test

    def crawlblog(self, prefix, page):
        returnList = []
        for i in xrange(1, 30):
            if len(returnList) == (page+1) * 10:
                break
            else:
                url = prefix + "page-" + str(i) + ".html"
                html = urllib2.urlopen(url)
                soup = BeautifulSoup(html)
                for entry in soup.find(id="sub_main").findAll("div", {"class": "entry"}):
                    blogName = entry.find("h3", {"class":"title"}).find("a").string
                    blogUrl = entry.find("h3", {"class":"title"}).find("a").get("href")
                    timeSmap = entry.find("div", {"class":"entry_head"}).find("span").string
                    returnList.append({"blogName":blogName, "blogUrl":blogUrl, "timeSmap": timeSmap})
        print returnList
        return returnList[-10:]