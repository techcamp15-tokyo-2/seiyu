__author__ = 'zhangxinzheng'


class Seiyu(object):
    def __init__(self, souptag):
        if isinstance(souptag, dict):
            self.avatarurl = dict["avatarUrl"]
            self.seiyuname = dict["seiyuName"]
            self.prefix = dict["prefix"]
            self.tag = dict["tag"]
        else:
            self.avatarurl = souptag.find("dt").find("img").get("src")
            self.seiyuname = souptag.find("dt").find("img").get("alt")
            self.prefix = souptag.find("a").get("href")
            self.tag = []
            for i in souptag.find("dd", {"class": "name clr"}).find("ul").findAll("a"):
                self.tag.append(i.contents[0])

    def getDict(self):
        return {"avatarUrl":self.avatarurl, "seiyuName":self.seiyuname, "prefix": self.prefix, "tag":self.tag}