__author__ = 'zhangxinzheng'
import tornado.web


class BaseHandler(tornado.web.RequestHandler):
    def __int__(self, application, request, **kwargs):
        tornado.web.RequestHandler.__init__(self, application, request, **kwargs)

    @property
    def db(self):
        return self.application.db