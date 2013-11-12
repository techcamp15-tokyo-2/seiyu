__author__ = 'zhangxinzheng'

from BaseHandler import BaseHandler
import json

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
        returnDict = dict({"state": state, "message": message, "email": email, "name": name, "gender": gender, "tag": tag})
        self.write(json.dumps(returnDict))


class Register(BaseHandler):
    def get(self, *args, **kwargs):
        email = self.get_argument("email")
        pwd = self.get_argument("pwd")
        uid = self.get_argument("uid")
        name = self.get_argument("name")
        gender = self.get_argument("gender")
        tag = self.get_argument("tag")

        state = ""
        message = ""
        user = self.db.user.find_one({"uid": uid, "email": email})
        if user:
            state = "fail"
            message = "has registered"
        else:
            state = "success"
            self.db.user.insert({"email": email, "pwd": pwd, "uid": uid, "name": name, "gender": gender, "tag": tag})
        returnDict = dict({"state": state, "message": message, "email": email, "name": name})
        self.write(json.dumps(returnDict))


class FindPwd(BaseHandler):
    def get(self, *args, **kwargs):
        #email = self.get_argument("email")
        #todo
        self.write("FindPwd")


class LatestFeed(BaseHandler):
    def get(self, *args, **kwargs):
        page = int(self.get_argument("page"))
