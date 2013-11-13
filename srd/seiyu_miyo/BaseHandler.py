__author__ = 'zhangxinzheng'
import tornado.web


class BaseHandler(tornado.web.RequestHandler):
    def __int__(self, application, request, **kwargs):
        tornado.web.RequestHandler.__init__(self, application, request, **kwargs)

    def get_current_user(self):
        return self.get_secure_cookie("uid")

    @property
    def db(self):
        return self.application.db.test