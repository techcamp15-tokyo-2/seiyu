__author__ = 'zhangxinzheng'

from BaseHandler import BaseHandler
from pymongo import DESCENDING
import json
from crawler import *
from bson import json_util
from bson.objectid import ObjectId


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
        print self.db
        if user:
            state = "fail"
            message = "has registered"
        else:
            state = "success"
            self.set_secure_cookie("uid", uid)
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

        helper = SeiyuFeedHelper.instance()


        tmpList = helper.feedList[page*10:(page+1)*10]
        if len(tmpList) == 0:
            state = "fail"
            message = "No more data"
        else:
            state = "success"
            for i in tmpList:
                curous = self.db.seiyuPicture.find({"seiyuName": i}).sort("index").sort("timeSmap", DESCENDING).limit(1)
                tempDict = curous.next()
                tempDict["seiyuId"] = self.db.seiyu.find_one({"seiyuName": i})["_id"].__str__()
                feedList.append(tempDict)
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

        tmpList = self.db.user.find_one({"uid": uid})["followed"][page*10: (page+1)*10]
        if len(tmpList) == 0:
            state = "fail"
            message = "No more data"
        else:
            state = "success"
            for i in tmpList:
                seiyuName = self.db.seiyu.find_one({"_id": ObjectId(i)})["seiyuName"]
                curous = self.db.seiyuPicture.find({"seiyuName": seiyuName}).sort("index").sort("timeSmap", DESCENDING).limit(1)
                tempDict = curous.next()
                tempDict["seiyuId"] = i
                favList.append(tempDict)
        returnDict = {"state": state, "message": message, "imageList": favList}
        self.write(json.dumps(returnDict, default=json_util.default))


class Search(BaseHandler):
    def get(self, *args, **kwargs):
        state = ""
        message = ""
        keyword = self.get_argument("keyword")
        searchList = []

        if self.get_argument("page"):
            page = int(self.get_argument("page"))
        else:
            page = 0
        result = self.db.seiyu.find({"seiyuName": {"$regex": keyword}}).skip(page*10).limit(10)
        for i in result:
            i["seiyuId"] = i["_id"].__str__()
            searchList.append(i)

        if len(searchList) == 0:
            state = "fail"
            message = "no Data"
        else:
            state = "success"
        returnDict = {"state": state, "message": message, "imageList": searchList}
        self.write(json.dumps(returnDict, default=json_util.default))


class ImageDetail(BaseHandler):
    def get(self, *args, **kwargs):
        state = ""
        message = ""
        imageList = []
        seiyuId = self.get_argument("seiyuId")
        if self.get_argument("page"):
            page = int(self.get_argument("page"))
        else:
            page = 0
        seiyuName = self.db.seiyu.find_one({"_id": ObjectId(seiyuId)})["seiyuName"]
        curous = self.db.seiyuPicture.find({"seiyuName": seiyuName}).sort("index").sort("timeSmap").skip(page*10).limit(10)
        for i in curous:
            i["seiyuId"] = seiyuId
            imageList.append(i)
        if len(imageList) == 0:
            state = "fail"
            message = "no Data"
        else:
            state = "success"
        returnDict = {"state": state, "message": message, "imageList": imageList}
        self.write(json.dumps(returnDict, default=json_util.default))


class BlogDetail(BaseHandler):
    def get(self, *args, **kwargs):
        state = ""
        message = ""
        blogList = []
        seiyuId = self.get_argument("seiyuId")
        if self.get_argument("page"):
            page = int(self.get_argument("page"))
        else:
            page = 0
        seiyuPrefix = self.db.seiyu.find_one({"_id": ObjectId(seiyuId)})["prefix"]
        if seiyuPrefix:
            blogList += SeiyuBlogHelper.instance().crawlblog(seiyuPrefix, page)
            state = "success"
        else:
            state = "fail"
            message = "unexsist seiyu"
        returnDict = {"state": state, "message": message, "blogList": blogList}
        self.write(json.dumps(returnDict, default=json_util.default))


class Action(BaseHandler):
    def get(self, *args, **kwargs):
        state = ""
        message = ""
        seiyuId = self.get_argument("seiyuId")
        followed = int(self.get_argument("followed"))
        uid = self.get_argument("uid")

        if seiyuId and followed in [0, 1] and self.db.seiyu.find_one({"_id": ObjectId(seiyuId)}):
            user = self.db.user.find_one({"uid": uid})
            if user:
                if followed == 1:
                    hasFolloed = False
                    for i in user["followed"]:
                        if i == seiyuId:
                            hasFolloed = True
                            break
                    if not hasFolloed:
                        user["followed"].append(seiyuId)
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


class Recommend(BaseHandler):
    def get(self, *args, **kwargs):
        state = ""
        message = ""
        infoList = []
        uid = self.get_argument("uid")
        followed = self.db.user.find_one({"uid", ObjectId(uid)})["followed"]
        recommendList = []
        curous = self.db.user.find({"tag": {"$in": followed}})
        for i in curous:
            recommendList.append(i)
        if len(recommendList) == 0:
            state = "fail"
            message = "no similar user found"
        else:
            for i in recommendList:
                userId = i["uid"]
                userName = i["name"]
                imageList = []

                followed = self.db.user.find_one({"uid": userId})["followed"]
                for j in followed:
                    seiyuName = self.db.seiyu.find_one({"_id": ObjectId(j)})["seiyuName"]
                    tempDict = self.db.seiyuPicture.find({"seiyuName": seiyuName}).sort("index").sort("timeSmap", DESCENDING).limit(1).next()
                    tempDict["seiyuId"] = tempDict["_id"].__str__()
                    imageList.append(tempDict)
                infoList.append({"userId": userId, "userName": userName, "imageList": imageList})
        returnDict = {"state": state, "message": message, "infoList": infoList}
        self.write(json.dumps(returnDict, default=json_util.default))


class EditInfo(BaseHandler):
    def get(self, *args, **kwargs):
        state = ""
        message = ""

        uid = self.get_argument("uid")
        followed = self.get_argument("tags")
        name = self.get_argument("name")
        email = self.get_argument("email")

        user = self.db.user.find_one({"uid": uid})
        if user:
            _id = user["_id"]
            user["followed"] = followed.split(",")
            user["name"] = name
            user["email"] = email
            user.pop("_id", None)
            self.db.user.update({"_id": _id}, user)
            state = "success"
        else:
            state = "fail"
            message = "invalid user"
        returnDict = {"state": state, "message": message}
        self.write(json.dumps(returnDict, default=json_util.default))
