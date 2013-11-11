import urllib2,urllib,random,threading
from BeautifulSoup import *

import sys
reload(sys)
sys.setdefaultencoding('utf-8')

class Images(threading.Thread):
    def __init__(self,lock,src):
        threading.Thread.__init__(self)
        self.lock = lock
        self.src = src
    def run(self):
        self.lock.acquire()
        urllib.urlretrieve(self.src,'./image/'+str(random.choice(range(9999))))
        print self.src + 'get'
        self.lock.release()

def img_greb(url):
    lock = threading.Lock()
    html = urllib2.urlopen(url).read()
    soup = BeautifulSoup(html)
    img = soup.findAll(['img'])
    for i in img:
        Images(lock,i.get('src')).start()

if __name__ ==  '__main__':
    img_greb("http://ameblo.jp/misawasachika/archive1-201307.html")
