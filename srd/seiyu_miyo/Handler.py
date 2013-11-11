__author__ = 'zhangxinzheng'

from BaseHandler import BaseHandler


class Login(BaseHandler):
    def get(self, *args, **kwargs):
        self.write("Login")


class Register(BaseHandler):
    def get(self, *args, **kwargs):
        self.write("Register")


class FindPwd(BaseHandler):
    def get(self, *args, **kwargs):
        self.write("FindPwd")