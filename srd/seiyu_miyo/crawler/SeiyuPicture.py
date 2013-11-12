__author__ = 'zhangxinzheng'


class SeiyuPicture(object):
    def __init__(self, souptag):
        if isinstance(souptag, dict):
            self.prefix = dict["prefix"]
            self.imageurl = dict["imageUrl"]
            self.blogurl = dict["blogUrl"]
            self.seiyuname = dict["seiyuName"]
        else:
            self.blogurl = souptag.find("p").find("a").get("href")
            self.imageurl = souptag.find("img").get("src")


    def getDict(self):
        return {"prefix":self.prefix,"imageUrl":self.imageurl,"blogUrl":self.blogurl,"seiyuName":self.seiyuname}