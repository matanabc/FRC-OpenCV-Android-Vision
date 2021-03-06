# FRC-OpenCV-Android-Vision
FRC vision application on android device using openCV and camera2 API (openCV JavaCamera2View)

### General
 * This app trying to change camera exposure to low (image will be dark), it may not work in your device. camera sittings can be found in [here](openCVLibrary349/src/main/java/org/opencv/android/JavaCamera2View.java#L209)
 * Need to give camera permissions for the app to work
 * If you changing openCV version add all the folders in sdk\native\libs from your new openCV version to jniLibs folder
 * Tested on samsung galaxy A5 2017, you can see [here](images/samsung_galaxy_A5_2017_vision_app_performance_when_web_vision_config_open.png) the performance when running it in 30FPS and 320X240 

### View should look like for FRC teams after configuration
![App view](/images/vision_test_screen_shoot.png)
you can also see [here](images/frc_2020_filed_home) 2020 field target images
### Draws on screen
 * phone battery level
 * FPS
 * Don't have connection (no one is reading vision data)
 * Counters, Target and target error 

### Servers
The servers will send value and responds only when the app is open and running on your phone! 
 * MjpgServer stream final mat (app view) to port 5800 in 15 FPS
 * VisionConstantServer get request to change vision values to port 5801, to view web vision value config send a request from your browser to `<Phone-IP>:5801` (you can see images of web vision config [here](images/vision_config_pages))
 * VisionDataServer send string with vision data (isTargetValid;x;y => true;-0.711;-0.417) to port 5802 (you can use [client.py](client/client.py) to read vision data value but change the host)

## Contact
If you have any question you can send me email: matanabc@gamil.com