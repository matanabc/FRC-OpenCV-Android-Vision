package com.example.opencvvisiontest;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;

public class Util {
    /**
     * Get IP address from first non-localhost interface
     *
     * @param useIPv4 true=return ipv4, false=return ipv6
     * @return address or empty string
     */
    public static String getIPAddress(boolean useIPv4) {
        try {
            for (NetworkInterface intf : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                for (InetAddress addr : Collections.list(intf.getInetAddresses())) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        boolean isIPv4 = sAddr.indexOf(':') < 0;
                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return "";
    }


    private static int fps = 0, fpsCount = 0;
    private static long start = System.currentTimeMillis();

    /**
     * This function will return the FPS counter
     * @return FPS count
     */
    public static String getFPSCount() {
        if (System.currentTimeMillis() - start >= 1000) {
            fps = fpsCount;
            fpsCount = 0;
            start = System.currentTimeMillis();
        } else {
            fpsCount++;
        }
        return String.valueOf(fps);
    }
}
