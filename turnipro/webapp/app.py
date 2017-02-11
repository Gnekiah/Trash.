import os, httplib, time
import tornado.web
import tornado.ioloop
import tornado.httpserver
from tornado.options import options, define
import torndb
from classify import classify
from merge_all import merge_all
import csvdp

CONFIG_PATH = "config.conf"

define("port", default=80, help="run on this port", type=int)
define("debug", default=False, help="debug mode", type=bool)
define("xsrf_cookies", default=True, help="xsrf cookies", type=bool)
define("cookie_timeout", default=60, help="cookie time out", type=int)

define("template_path", default="templates", help="template path", type=str)
define("static_path", default="static", help="static path", type=str)
define("cache_path", default="cache", help="cache path", type=str)
define("log_file", default="log.log", help="log file", type=str)

define("mysqlhost", default="localhost:3306", help="mysql", type=str)
define("mysqldb", default="turnipro", help="mysql database", type=str)
define("mysqluser", default="test", help="mysql username", type=str)
define("mysqlpasswd", default="123456", help="passwd", type=str)

define("pp0", default=0.0, help="pp0", type=float)
define("pp1", default=0.0, help="pp1", type=float)
define("propcut", default=[], help="propcut", type=list)
define("p0Property", default=[], help="p0Property", type=list)
define("p1Property", default=[], help="p1Property", type=list)


class WebApp(tornado.web.Application):
    def __init__(self):
        handlers = [
            (r"/", HomeHandler),
            (r"/index", HomeHandler),
            (r"/index.html", HomeHandler),
            (r"/login", LoginHandler),
            (r"/logout", LogoutHandler),
            (r"/query", QueryHandler),
            (r"/manage", ManageHandler) ]
        settings = dict(
            template_path = os.path.join(os.path.dirname(__file__), options.template_path),
            static_path = os.path.join(os.path.dirname(__file__), options.static_path),
            cookie_secret = "dd82290738723r3ab3527d822907b2fee3da22a02fee3a3rj",
            login_url = "/login",
            xsrf_cookies = options.xsrf_cookies,
            debug = options.debug, )
        tornado.web.Application.__init__(self, handlers, **settings)
        self.db = torndb.Connection(
            host=options.mysqlhost, database=options.mysqldb,
            user=options.mysqluser, password=options.mysqlpasswd)


class HomeHandler(tornado.web.RequestHandler):
    def get(self):
        self.render("index.html")


class BaseHandler(tornado.web.RequestHandler):
    @property
    def db(self):
        return self.application.db

    def get_current_user(self):
        return self.get_secure_cookie("login")


class LoginHandler(BaseHandler):
    def get(self):
        self.render("login.html")

    def post(self):
        login = self.get_argument("login")
        passwd = self.get_argument("passwd")
        user = self.db.get("SELECT * FROM users WHERE name = %s", login)
        if user != None and user["passwd"] == passwd:
            cookietimeout = int(time.time()) + options.cookie_timeout
            self.set_secure_cookie("login", self.get_argument("login", "passwd"), expires=cookietimeout)
            self.redirect("/query")
        else:
            self.redirect("/login")


class LogoutHandler(BaseHandler):
    @tornado.web.authenticated
    def get(self):
        self.clear_all_cookies("login")
        self.redirect("/")


class QueryHandler(BaseHandler):
    @tornado.web.authenticated
    def get(self):
        self.render("query.html", msg="", result_table="")

    @tornado.web.authenticated
    def post(self):
        sid = str(self.get_argument('sid'))
        res = str(self.get_argument('yn'))
        major = self.get_argument('major')
        result = list()
        if res == 'yes':
            res = '1'
        elif res == 'no':
            res = '0'
        else:
            res = ""
        if len(sid) < 2:
            sid = ""
        if major == 'empty':
            major = ""
        if sid is not "":
            if res is not "":
                if major is not "":
                    result = self.db.query("SELECT * FROM result WHERE sid=%s and res=%s and major=%s", sid, res, major)
                else:
                    result = self.db.query("SELECT * FROM result WHERE sid=%s and res=%s", sid, res)
            else:
                if major is not "":
                    result = self.db.query("SELECT * FROM result WHERE sid=%s and major=%s", sid, major)
                else:
                    result = self.db.query("SELECT * FROM result WHERE sid=%s", sid)
        else:
            if res is not "":
                if major is not "":
                    result = self.db.query("SELECT * FROM result WHERE res=%s and major=%s", res, major)
                else:
                    result = self.db.query("SELECT * FROM result WHERE res=%s", res)
            else:
                if major is not "":
                    result = self.db.query("SELECT * FROM result WHERE major=%s", major)
                else:
                    result = self.db.query("SELECT * FROM result")
        table = ""
        for i in result:
            i['res'] = 'yes' if i['res']==u'1' else '-'
            table += "<tr><td>"+i['sid']+"</td><td>"+i['res']+"</td><td>"+i['major']+"</td></tr>"
        self.render('query.html', result_table=table)


class ManageHandler(BaseHandler):
    @tornado.web.authenticated
    def get(self):
        self.render("manage.html")

    @tornado.web.authenticated
    def post(self):
        upload_path = os.path.join(os.path.dirname(__file__), options.cache_path)
        card = self.request.files['card'][0]
        card_name = card["filename"]
        with open(os.path.join(upload_path, card_name), "wb") as f:
            f.write(card["body"])
        score_name = "exam.csv"
        try: 
            score = self.request.files['score'][0]
            score_name = score['filename']
            with open(os.path.join(upload_path, score_name), "wb") as f:
                f.write(score["body"])
        except:
            pass
        try:
            merge_all(os.path.join(upload_path, score_name), os.path.join(upload_path, card_name))
            result = classify('term.csv', [options.pp0, options.pp1, options.propcut, options.p0Property, options.p1Property])
        except:
            self.write("<html><head><meta http-equiv=\"refresh\" content=\"5;"
                    + "url=/query\"/></head><body>ERROR, file format error!</body></html>")
            return
        for i in result:
            query = self.db.query("SELECT * FROM result WHERE sid = %s", i[0].decode())
            if query == None or query == []:
                major = self.db.query("SELECT * FROM information WHERE sid = %s", i[0].decode())[0]
                self.db.execute("INSERT INTO result(sid, res, major) VALUES (%s, %s, %s)", i[0], str(i[1]), major['major'])
        self.write("<html><head><meta http-equiv=\"refresh\" content=\"5;"
                    + "url=/query\"/></head><body>OK!</body></html>")

################################################################################

class ReErrorHandler(tornado.web.RequestHandler):
    def __init__(self, application, request, status_code):
        tornado.web.RequestHandler.__init__(self, application, request)
        self.set_status(status_code)

    def write_error(self, status_code, **kwargs):
        message=httplib.responses[status_code]
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
