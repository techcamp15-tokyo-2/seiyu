from crawler import Singleton

__author__ = 'zhangxinzheng'

from BeautifulSoup import *
import urllib2
import pymongo
import time
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
        cache = self.db.blog.find_one({"blogPrefix": prefix})
        if cache:
            createTime = int(cache["createTime"])
            if int(time.time()) - createTime > 60*60:
                self.db.blog.remove(cache)
            else:
                return cache["blogList"][page*10: (page+1)*10]
        returnList = []
        for i in xrange(1, 20):
            url = prefix + "page-" + str(i) + ".html"
            html = urllib2.urlopen(url)
            soup = BeautifulSoup(html)
            for entry in soup.find(id="sub_main").findAll("div", {"class": "entry"}):
                blogName = entry.find("h3", {"class": "title"}).find("a").string
                blogUrl = entry.find("h3", {"class": "title"}).find("a").get("href")
                timeSmap = entry.find("div", {"class": "entry_head"}).find("span").string
                returnList.append({"blogName": blogName, "blogUrl": blogUrl, "timeSmap": timeSmap})

        self.db.blog.insert({"blogPrefix": prefix, "createTime": str(int(time.time())), "blogList": returnList})
        return returnList[page*10: (page+1)*10]