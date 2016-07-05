import BaseHTTPServer
import time

OUR_HOST  = "rhodey.org"
OUR_PORT  = 8080
NEXT_HOST = "eth.rhodey.org"
NEXT_PORT = "8545"
REDIRECT  = "http://" + NEXT_HOST + ":" + NEXT_PORT

class RedirectHandler(BaseHTTPServer.BaseHTTPRequestHandler):
  def do_HEAD(s):
    s.send_response(307)
    s.send_header("Location", REDIRECT)
    s.end_headers()
  def do_GET(s):
    s.do_HEAD()

if __name__ == '__main__':
  server_class = BaseHTTPServer.HTTPServer
  httpd        = server_class((OUR_HOST, OUR_PORT), RedirectHandler)
  print time.asctime(), "Server Starts - %s:%s" % (OUR_HOST, OUR_PORT)
  try:
    httpd.serve_forever()
  except KeyboardInterrupt:
    pass
  httpd.server_close()
  print time.asctime(), "Server Stops - %s:%s" % (OUR_HOST, OUR_PORT)
