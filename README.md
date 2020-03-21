# FRC-OpenCV-Android-Vision
FRC vision application on android device using openCV and camera2 API (openCV JavaCamera2View)

### General
 * this app trying to change camera exposure to low (image will be dark), it may not work in your device. camera sittings can be found in [here](openCVLibrary349/src/main/java/org/opencv/android/JavaCamera2View.java#L209)
 * Need to give camera permissions for the app to work
 * if you changing openCV version add all the folders in sdk\native\libs from your new openCV version to jniLibs folder

### View should look like for FRC teams after configuration
![App view](/vision_test_screen_shoot.png)

### Servers
 * MjpgServer stream final mat (app view) to port 5800 in 15 FPS
 * VisionConstantServer get request to change vision values to port 5801, send a request from tour browser to <Phone-IP>:5801 to view the html vision value config.
 * VisionDataServer send string with vision data (isTargetValid;x;y => true;-0.711;-0.417) to port 5802 (you can use [client.py](client/client.py) to read vision data value but change the host).

## Contact
If you have any question you can send me email: matanabc@gamil.com