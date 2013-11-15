from Seiyu import *

__author__ = 'zhangxinzheng'

import urllib2
from BeautifulSoup import *
import pymongo
import sys
from SeiyuPictureHelper import *
class SeiyuHelper(object):
    _instance = ''
    @classmethod
    def instance(cls):
        if not cls._instance:
            cls._instance = cls()
            cls._instance.start()
        return cls._instance

    def start(self):
        self.db = pymongo.Connection().test

    def updateSeiyuInfo(self):
        seiyuUrl = ["http://official.ameba.jp/genrekana/kana22-"+str(i)+".html" for i in xrange(1, 44)]
        for i in seiyuUrl:
            tryCount = 0
            html = ""
            try:
                html = urllib2.urlopen(i).read()
            except urllib2.HTTPError, err:
                print err
                if err.code == 500:
                    sleep(2)
                    tryCount += 1
                    if tryCount > 20:
                        continue
                    html = urllib2.urlopen(i).read()
                else:
                    pass

            except urllib2.URLError, err:
                print err
                continue
            soup = BeautifulSoup(html)
            for j in soup.findAll("dl", {"class": "clr"}):
                seiyu = Seiyu(j).getDict()

                mdbIn = self.db.seiyu.find_one({"seiyuName": seiyu["seiyuName"]})
                if mdbIn:
                    #sys.stdout.write("update seiyu Info %s\n" % seiyu["seiyuName"])
                    #self.db.seiyu.update({"_id": mdbIn["_id"]}, seiyu)
                    pass
                else:
                    sys.stdout.write("insert seiyu Info %s\n" % seiyu["seiyuName"])
                    html = ""
                    try:
                        html = urllib2.urlopen(seiyu["prefix"]).read()
                    except urllib2.URLError, err:
                        print err
                    soup = BeautifulSoup(html)
                    profile = soup.find(id="new_profile")
                    if not profile:
                        gender = "-1"
                    else:
                        sex = profile.find("li", {"class": "sex"})
                        if sex:
                            if sex.string == u'\u6027\u5225\uff1a\u5973\u6027':
                                gender = "0"
                            elif sex.string == u'\u6027\u5225\uff1a\u7537\u6027':
                                gender = "1"
                            else:
                                gender = "-1"
                        else:
                            gender = "-1"
                    seiyu["gender"] = gender
                    self.db.seiyu.insert(seiyu)
                    SeiyuPictureHelper.instance().updateSeiyuPictureInfo(seiyu["seiyuName"])
