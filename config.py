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


def minArea(x):
    urllib.request.urlopen("http://192.168.14.34:8888/?minArea=" + str(x)).read()

def maxArea(x):
    urllib.request.urlopen("http://192.168.14.34:8888/?maxArea=" + str(x)).read()

def minRatio(x):
    urllib.request.urlopen("http://192.168.14.34:8888/?minRatio=" + str(x)).read()

def maxRatio(x):
    urllib.request.urlopen("http://192.168.14.34:8888/?maxRatio=" + str(x)).read()

def minWidth(x):
    urllib.request.urlopen("http://192.168.14.34:8888/?minWidth=" + str(x)).read()

def maxWidth(x):
    urllib.request.urlopen("http://192.168.14.34:8888/?maxWidth=" + str(x)).read()

def minHeight(x):
    urllib.request.urlopen("http://192.168.14.34:8888/?minHeight=" + str(x)).read()

def maxHeight(x):
    urllib.request.urlopen("http://192.168.14.34:8888/?maxHeight=" + str(x)).read()

cv2.namedWindow('HSV')
cv2.namedWindow('Filters')
# create switch for ON/OFF functionality
cv2.createTrackbar('HSV', 'HSV', 0, 1, showHSV)

cv2.createTrackbar('H Min','HSV',0,255,hMin)
cv2.createTrackbar('H Max','HSV',255,255,hMax)

cv2.createTrackbar('S Min','HSV',0,255,sMin)
cv2.createTrackbar('S Max','HSV',255,255,sMax)

cv2.createTrackbar('V Min','HSV',0,255,vMin)
cv2.createTrackbar('V Max','HSV',255,255,vMax)


cv2.createTrackbar('Area Min','Filters',0,255,minArea)
cv2.createTrackbar('Area Max','Filters',255,255,maxArea)

cv2.createTrackbar('Ratio Min','Filters',0,255,minRatio)
cv2.createTrackbar('Ratio Max','Filters',255,255,maxRatio)

cv2.createTrackbar('Width Min','Filters',0,255,minWidth)
cv2.createTrackbar('Width Max','Filters',255,255,maxWidth)

cv2.createTrackbar('Height Min','Filters',0,255,minHeight)
cv2.createTrackbar('Height Max','Filters',255,255,maxHeight)

while(1):    
    if cv2.waitKey(33) == ord('a'):
        break

cv2.destroyAllWindows()