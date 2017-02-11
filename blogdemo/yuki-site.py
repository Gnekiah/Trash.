import funclib, markup
import os, httplib, time, platform
import tornado.web
import tornado.ioloop
import tornado.httpserver
from tornado.options import options, define


CONFIG_PATH = "config.conf"

define("port", default=80, help="run on this port", type=int)

define("debug", default=False, help="debug mode", type=bool)
define("xsrf_cookies", default=True, help="xsrf cookies", type=bool)

define("template_path", default="templates", help="template path", type=str)
define("static_path", default="static", help="static path", type=str)

define("bg_path", default="static/background", help="background dir", type=str)
define("files_path", default="static/files", help="upload file path", type=str)
define("cache_path", default="static/cache", help="cache path", type=str)
define("blog_path", default="static/blog", help="blog path", type=str)
define("cland_path", default="static/cland", help="cland path", type=str)
define("log_file", default="log.log", help="log file", type=str)

define("login", default="root", help="user name", type=str)
define("passwd", default="0000", help="user password", type=str)
define("salt", default="0000", help="salt string", type=str)
define("cookie_secret", default="0000", help="cookie secret", type=str)
define("cookie_timeout", default=60, help="cookie time out", type=int)


class WebApp(tornado.web.Application):
    def __init__(self):
        handlers = [
            (r"/", IndexHandler),
            (r"/index", IndexHandler),
            (r"/index.html", IndexHandler),
            (r"/blog([0-9]*)", BlogHandler),
            (r"/blog/([0-9]+)", ArticleHandler),
            (r"/cland([0-9]*)", ClandHandler),
            (r"/cland/([0-9]+)", ChapterHandler),
            (r"/about", AboutHandler),
            (r"/login", LoginHandler),
            (r"/logout", LogoutHandler),
            (r"/manage", ManageHandler),
            (r"/writing", WritingHandler),
            (r"/upload_image", UploadImageHandler),
            (r"/upload", UploadHandler), ]
        settings = dict(
            template_path = os.path.join(os.path.dirname(__file__), options.template_path),
            static_path = os.path.join(os.path.dirname(__file__), options.static_path),
            cookie_secret = options.cookie_secret,
            login_url = "/login",
            xsrf_cookies = options.xsrf_cookies,
            debug = options.debug, )
        tornado.web.Application.__init__(self, handlers, **settings)


class IndexHandler(tornado.web.RequestHandler):
    def get(self):
        self.render("index.html")


class BlogHandler(tornado.web.RequestHandler):
    def get(self, args):
        bloglists = []
        # TODO get blog list according args
        self.render("blog.html", bloglists=bloglists)


class ArticleHandler(tornado.web.RequestHandler):
    def get(self, args):
        title = ""
        text = ""
        # TODO get title and text according args
        self.render("article.html", title=title, text=text)


class ClandHandler(tornado.web.RequestHandler):
    def get(self, args):
        clandlists = []
        # TODO get cland list according args
        self.render("cland.html", clandlists=clandlists)


class ChapterHandler(tornado.web.RequestHandler):
    def get(self, args):
        title = ""
        text = ""
        # TODO get title and text according args
        self.render("chapter.html", title=title, text=text)


class AboutHandler(tornado.web.RequestHandler):
    def get(self):
        about = ""
        # TODO get "about text"
        self.render("about.html", about=about)

################################################################################

class BaseHandler(tornado.web.RequestHandler):
    def get_current_user(self):
        return self.get_secure_cookie("login")


class LoginHandler(BaseHandler):
    def get(self):
        self.render("login.html")

    def post(self):
        login = self.get_argument("login")
        passwd = self.get_argument("passwd")
        if funclib.authenticating(login, passwd):
            cookietimeout = int(time.time()) + options.cookie_timeout
            self.set_secure_cookie("login", self.get_argument("login", "passwd"), expires=cookietimeout)
            self.redirect("/manage")
        else:
            self.redirect("/login")


class LogoutHandler(BaseHandler):
    def get(self):
        self.clear_all_cookies("login")
        self.redirect("/")


class ManageHandler(BaseHandler):
    @tornado.web.authenticated
    def get(self):
        managelists=[]
        # TODO generate manage list
        self.render("manage.html", managelists=managelists)

    @tornado.web.authenticated
    def post(self):
        # TODO do something
        pass


class WritingHandler(BaseHandler):
    @tornado.web.authenticated
    def get(self):
        self.render("writing.html")


class UploadImageHandler(BaseHandler):
    @tornado.web.authenticated
    def post(self):
        # TODO save images
        pass


class UploadHandler(BaseHandler):
    @tornado.web.authenticated
    def post(self):
        # TODO do save
        pass

################################################################################

class ReErrorHandler(tornado.web.RequestHandler):
    def __init__(self, application, request, status_code):
        tornado.web.RequestHandler.__init__(self, application, request)
        self.set_status(status_code)

    def write_error(self, status_code, **kwargs):
        message=httplib.responses[status_code]
        # TODO DIY error page
        self.render("error.html", status_code=status_code, message=message)

    def prepare(self):
        raise tornado.web.HTTPError(self._status_code)


def main():
    options.parse_config_file(CONFIG_PATH)
    http_server = tornado.httpserver.HTTPServer(WebApp())
    tornado.web.ErrorHandler = ReErrorHandler
    http_server.listen(options.port)
    tornado.ioloop.IOLoop.current().start()


if __name__ == "__main__":
    main()