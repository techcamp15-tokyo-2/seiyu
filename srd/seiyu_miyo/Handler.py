__author__ = 'zhangxinzheng'

from BaseHandler import BaseHandler
from pymongo import DESCENDING
import json
import crawler
from bson import json_util

class Login(BaseHandler):
    def get(self, *args, **kwargs):
        uid = self.get_argument("uid")
        email = self.get_argument("email")
        pwd = self.get_argument("pwd")

        name = ""
        state = ""
        message = ""
        tag = ""
        gender = ""
        user = self.db.user.find_one({"uid": uid})
        if user:
            if user["pwd"] == pwd:
                self.set_secure_cookie("uid", uid)
                state = "success"
                name = user["name"]
                for tt in user["tag"]:
                    tag += tt
                    tag += ","
                gender = user["gender"]
            else:
                self.set_secure_cookie("uid", None)
                state = "fail"
                message = "wrong password"
        else:
            state = "fail"
            message = "has not register"
        returnDict = {"state": state, "message": message, "email": email, "name": name, "gender": gender, "tag": tag}
        self.write(json.dumps(returnDict))


class Register(BaseHandler):
    def get(self, *args, **kwargs):
        email = self.get_argument("email")
        pwd = self.get_argument("pwd")
        uid = self.get_argument("uid")
        name = self.get_argument("name")
        gender = self.get_argument("gender")
        tag = self.get_argument("tag")
        followed = []
        state = ""
        message = ""
        user = self.db.user.find_one({"uid": uid, "email": email})
        if user:
            state = "fail"
            message = "has registered"
        else:
            state = "success"
            self.db.user.insert({"email": email, "pwd": pwd, "uid": uid, "name": name, "gender": gender, "tag": tag, "followed": followed})
        returnDict = {"state": state, "message": message, "email": email, "name": name}
        self.write(json.dumps(returnDict))


class FindPwd(BaseHandler):
    def get(self, *args, **kwargs):
        #email = self.get_argument("email")
        #todo
        self.write("FindPwd")


class LatestFeed(BaseHandler):
    def get(self, *args, **kwargs):
        state = ""
        message = ""
        feedList = []

        if self.get_argument("page"):
            page = int(self.get_argument("page"))
        else:
            page = 0

        helper = crawler.SeiyuFeedHelper()
        allFeedCount = len(helper.feedList)
        tmpList = allFeedCount[page*10:(page+1)*10]
        if len(tmpList) == 0:
            state = "fail"
            message = "No more data"
        else:
            state = "success"
            for i in tmpList:
                curous = self.db.seiyuPicture.find({"seiyuName": i}).sort("index").sort("timeSmap", DESCENDING).limit(1)
                feedList.append(curous.next())
        returnDict = {"state": state, "message": message, "imageList": feedList}
        self.write(json.dumps(returnDict, default=json_util.default))


class Favourite(BaseHandler):
    def get(self, *args, **kwargs):
        state = ""
        message =""
        favList = []
        uid = self.get_argument("uid")

        if self.get_argument("page"):
            page = int(self.get_argument("page"))
        else:
            page = 0
        allFavCount = len(self.db.user.find_one({"uid": uid})["followed"])
        tmpList = allFavCount[page*10:(page+1)*10]
        if len(tmpList) == 0:
            state = "fail"
            message = "No more data"
        else:
            state = "success"
            for i in tmpList:
                seiyuName = self.db.seiyu.find_one({"_id": i})
                curous = self.db.seiyuPicture.find({"seiyuName": seiyuName}).sort("index").sort("timeSmap", DESCENDING).limit(1)
                favList.append(curous.next())
        returnDict = {"state": state, "message": message, "imageList": favList}
        self.write(json.dumps(returnDict, default=json_util.default)


class Action(BaseHandler):
    def get(self, *args, **kwargs):
        state = ""
        message = ""
        seiyuId = self.get_argument("seiyuId")
        followed = int(self.get_argument("followed"))
        uid = self.get_argument("uid")

        if seiyuId and followed and followed in [0, 1] and self.db.seiyu.find_one({"_id": seiyuId}):
            user = self.db.user.find_one({"uid": uid})
            if user:
                if followed == 1:
                    hasFolloed = False
                    for i in user["followed"]:
                        if i == seiyuId:
                            hasFolloed = True
                            break
                    if not hasFolloed:
                        user["followed"].append(i)
                else:
                    for i in user["followed"]:
                        if i == seiyuId:
                            user["followed"].remove(i)
                            break
                self.db.user.update({"_id": user["_id"]}, {"$set": {"followed": user["followed"]}})
                state = "success"
            else:
                state = "fail"
                message = "not invalid user"
        else:
            state = "fail"
            message = "wrong arguments"
        self.write(json.dumps({"state": state, "message": message}))




