# FRC-OpenCV-Android-Vision
FRC vision application on android device using openCV and camera2 API (using JavaCamera2View)

### General
 * this app trying to change camera exposure to low (image will be dark), it may not work in all the devices. the camera sittings can be found in openCV JavaCamera2View line 218
 * Need to give camera permissions for the app to work
 * if you changing openCV version add openCV as a static data to jniLibs folder

### View should look like for FRC teams after configuration
![App view](/vision_test_screen_shoot.png)

### Servers
 * MjpgServer stream final mat (app view) to port 5800 in 15 FPS
 * VisionConstantServer get request to change vision values to port 5801 (you can use [config.py](other_file.md)).
 * VisionDataServer send string with vision data (isTargetValid;x;y => true;-1.326;3.25) to port 5802.

## Contact
If you have any question you can send me email: matanabc@gamil.com
