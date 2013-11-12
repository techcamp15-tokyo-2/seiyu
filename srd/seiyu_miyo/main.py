__author__ = 'zhangxinzheng'
# -*- coding: utf-8 -*-

# #の後ろは全部コメントです

# tornado component

import tornado.httpserver
import tornado.ioloop
import tornado.web
import tornado.options
from tornado.options import define, options

# component of python

import os
# mongodbとpythonのミドルウェア
import pymongo

from Handler import *

define("port", default=8889, help="tornado will run on this port", type=int)


class Application(tornado.web.Application):
    def __init__(self):
        setting = dict(
            cookie_secret="konnichiwaohayogozaimasunannosecretdemoiidesu",
            login_url="/login",
            template_path=os.path.join(os.path.dirname(__file__), "templates"),
        )
        handlers = [
            # urlとclassのmapping
            (r"/login", Login),
            (r"/register", Register),
            (r"/findPwd", FindPwd)
        ]

        tornado.web.Application.__init__(self, handlers, **setting)
        #mongo db init
        self.db = pymongo.Connection()


def main():
    tornado.options.parse_command_line()
    http_server = tornado.httpserver.HTTPServer(Application())
    http_server.listen(options.port)
    tornado.ioloop.IOLoop.instance().start()


if __name__ == "__main__":
    main()
