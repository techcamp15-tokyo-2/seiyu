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
                self.clear_cookie("email")
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

        uid = self.get_argument("uid")
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
                if curous.count() == 0:
                    continue
                tempDict = curous.next()
                seiyu = self.db.seiyu.find_one({"seiyuName": i})
                tempDict["seiyuId"] = seiyu["_id"].__str__()
                tempDict["gender"] = seiyu["gender"]
                #followIdList = self.db.user.find_one({"uid": uid})["tag"]
                #followSeiyuList = []
                #for i in followIdList:
                #    sei = self.db.seiyu.find_one({"_id": ObjectId(i)})
                #    if sei:
                #        followSeiyuList.append(sei["seiyuName"])
                #if i in followSeiyuList:
                #    tempDict["followed"] = "1"
                #else:
                #    tempDict["followed"] = "0"
                tempDict["timeSmap"] = tempDict["timeSmap"][0:4] + "-" + tempDict["timeSmap"][4:6] + "-" + tempDict["timeSmap"][6:8]
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
                if curous.count() == 0:
                    continue
                tempDict = curous.next()
                tempDict["seiyuId"] = i
                tempDict["gender"] = self.db.seiyu.find_one({"_id": ObjectId(i)})["gender"]
                #tempDict["followed"] = "1"
                tempDict["timeSmap"] = tempDict["timeSmap"][0:4] + "-" + tempDict["timeSmap"][4:6] + "-" + tempDict["timeSmap"][6:8]

                favList.append(tempDict)
        returnDict = {"state": state, "message": message, "imageList": favList}
        self.write(json.dumps(returnDict, default=json_util.default))


class Search(BaseHandler):
    def get(self, *args, **kwargs):
        state = ""
        message = ""
        keyword = self.get_argument("keyword")
        searchList = []

        uid = self.get_argument("uid")
        if self.get_argument("page"):
            page = int(self.get_argument("page"))
        else:
            page = 0
        result = self.db.seiyu.find({"seiyuName": {"$regex": keyword}}).skip(page*10).limit(10)
        #followIdList = self.db.user.find_one({"uid": uid})["tag"]

        for i in result:
            curois = self.db.seiyuPicture.find({"seiyuName": i["seiyuName"]}).sort("index").sort("timeSmap", DESCENDING).limit(1)
            if curois.count() == 0:
                continue
            tempDict = curois.next()
            tempDict["seiyuId"] = self.db.seiyu.find_one({"seiyuName": i["seiyuName"]})["_id"].__str__()
            tempDict["timeSmap"] = tempDict["timeSmap"][0:4] + "-" + tempDict["timeSmap"][4:6] + "-" + tempDict["timeSmap"][6:8]
            tempDict["gender"] =  self.db.seiyu.find_one({"seiyuName": i["seiyuName"]})["gender"]
            #if tempDict["seiyuId"] in followIdList:
            #    tempDict["follow"] = "1"
            #else:
            #    tempDict["follow"] = "0"
            searchList.append(tempDict)

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
        uid = self.get_argument("uid")
        seiyuId = self.get_argument("seiyuId")
        if self.get_argument("page"):
            page = int(self.get_argument("page"))
        else:
            page = 0
        seiyuName = self.db.seiyu.find_one({"_id": ObjectId(seiyuId)})["seiyuName"]
        curous = self.db.seiyuPicture.find({"seiyuName": seiyuName}).sort("index").sort("timeSmap").skip(page*10).limit(10)
        for i in curous:
            i["seiyuId"] = seiyuId
            i["timeSmap"] = i["timeSmap"][0:4] + "-" + i["timeSmap"][4:6] + "-" + i["timeSmap"][6:8]
            imageList.append(i)
        if len(imageList) == 0:
            state = "fail"
            message = "no Data"
        else:
            state = "success"
        followIdList = self.db.user.find_one({"uid": uid})["followed"]
        if seiyuId in followIdList:
            followed = "1"
        else:
            followed = "0"
        returnDict = {"state": state, "message": message, "imageList": imageList, "followed": followed}
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
                if followed == 0:
                    hasFolloed = False
                    for i in user["followed"]:
                        if i == seiyuId:
                            hasFolloed = True
                            break
                    if not hasFolloed:
                        user["followed"].append(seiyuId)
                    message = "1"
                else:
                    for i in user["followed"]:
                        if i == seiyuId:
                            user["followed"].remove(i)
                            break
                    message = "0"
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
        followed = self.db.user.find_one({"uid": uid})["followed"]
        recommendList = []
        curous = self.db.user.find({"followed": {"$in": followed}, "uid": {"$ne": uid}})
        #curous = self.db.user.find({"followed": {"$in": followed}})
        for i in curous:
            i["similar"] = len(set(i["followed"]).intersection(set(followed)))
            recommendList.append(i)
        recommendList.sort(key=lambda x: x["similar"], reverse=True)
        recommendList = recommendList[0:10]
        if len(recommendList) == 0:
            state = "fail"
            message = "no similar user found"
        else:
            state = "success"
            for i in recommendList:
                userId = i["uid"]
                userName = i["name"]
                email = i["email"]
                imageList = []

                otherfollowed = self.db.user.find_one({"uid": userId})["followed"]
                difFollowed = set(otherfollowed).difference(set(followed))
                count = len(difFollowed)
                seiyuRecommend = []
                if count > 6:
                    seiyuRecommend += list(difFollowed[0:6])
                else:
                    seiyuRecommend += (list(difFollowed) + otherfollowed[count:6-count])
                for j in seiyuRecommend:
                    seiyuName = self.db.seiyu.find_one({"_id": ObjectId(j)})["seiyuName"]
                    curous = self.db.seiyuPicture.find({"seiyuName": seiyuName}).sort("index").sort("timeSmap", DESCENDING).limit(1)
                    if curous.count() == 0:
                        continue
                    tempDict = curous.next()
                    tempDict["seiyuId"] = j
                    tempDict["timeSmap"] = tempDict["timeSmap"][0:4] + "-" + tempDict["timeSmap"][4:6] + "-" + tempDict["timeSmap"][6:8]
                    imageList.append(tempDict)
                infoList.append({"userId": userId, "userName": userName, "imageList": imageList, "email": email})
        returnDict = {"state": state, "message": message, "infoList": infoList}
        self.write(json.dumps(returnDict, default=json_util.default))


class User(BaseHandler):
    def get(self, *args, **kwargs):
        uid = self.get_argument("uid")
        ouid = self.get_argument("ouid")
        selfFollowed = self.db.user.find_one({"uid": uid})["followed"]
        ouser = self.db.user.find_one({"uid": ouid})

        state = "success"
        message = ""
        seiyuList = []
        for i in ouser["followed"]:
            seiyuName = self.db.seiyu.find_one({"_id": ObjectId(i)})["seiyuName"]
            if i in selfFollowed:
                followed = "1"
            else:
                followed = "0"
            seiyuList.append({"seiyuId": i, "seiyuName": seiyuName, "followed": followed})
        returnDict = {"state": state, "message": message, "name": ouser["name"], "email": ouser["email"], "seiyuList":seiyuList}
        self.write(json.dumps(returnDict, default=json_util.default))


#class EditInfo(BaseHandler):
#    def get(self, *args, **kwargs):
#        state = ""
#        message = ""
#
#        uid = self.get_argument("uid")
#        followed = self.get_argument("tags")
#        name = self.get_argument("name")
#        email = self.get_argument("email")
#
#        user = self.db.user.find_one({"uid": uid})
#        if user:
#            _id = user["_id"]
#            user["followed"] = followed.split(",")
#            user["name"] = name
#            user["email"] = email
#            user.pop("_id", None)
#            self.db.user.update({"_id": _id}, user)
#            state = "success"
#        else:
#            state = "fail"
#            message = "invalid user"
#        returnDict = {"state": state, "message": message}
#        self.write(json.dumps(returnDict, default=json_util.default))
