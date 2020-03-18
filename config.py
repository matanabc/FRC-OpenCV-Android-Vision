import cv2
import urllib.request

def hMin(x):
    urllib.request.urlopen("http://192.168.14.34:8888/?hMin=" + str(x)).read()

def sMin(x):
    urllib.request.urlopen("http://192.168.14.34:8888/?sMin=" + str(x)).read()

def vMin(x):
    urllib.request.urlopen("http://192.168.14.34:8888/?vMin=" + str(x)).read()

def hMax(x):
    urllib.request.urlopen("http://192.168.14.34:8888/?hMax=" + str(x)).read()

def sMax(x):
    urllib.request.urlopen("http://192.168.14.34:8888/?sMax=" + str(x)).read()

def vMax(x):
    urllib.request.urlopen("http://192.168.14.34:8888/?vMax=" + str(x)).read()

def showHSV(x):
    if x == 1:
        urllib.request.urlopen("http://192.168.14.34:8888/?showHSV=true").read()
    else:
        urllib.request.urlopen("http://192.168.14.34:8888/?showHSV=false").read()

cv2.namedWindow('config')

# create switch for ON/OFF functionality
cv2.createTrackbar('HSV', 'config', 0, 1, showHSV)

cv2.createTrackbar('H Min','config',0,255,hMin)
cv2.createTrackbar('H Max','config',255,255,hMax)

cv2.createTrackbar('S Min','config',0,255,sMin)
cv2.createTrackbar('S Max','config',255,255,sMax)

cv2.createTrackbar('V Min','config',0,255,vMin)
cv2.createTrackbar('V Max','config',255,255,vMax)

while(1):    
    if cv2.waitKey(1) & 0xFF == 27:
        break

    # # get current positions of four trackbars
    # r = cv2.getTrackbarPos('R','config')
    # g = cv2.getTrackbarPos('G','config')
    # b = cv2.getTrackbarPos('B','config')
    # s = cv2.getTrackbarPos('HSV','config')


cv2.destroyAllWindows()