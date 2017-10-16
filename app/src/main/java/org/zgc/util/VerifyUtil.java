package org.zgc.util;

import java.util.regex.Pattern;

/**
 * Created by Nick on 2017/10/2
 */
public class VerifyUtil {
    public static boolean verifyMAC(String mac) {
        String patternMac = "^[A-F0-9]{2}(:[A-F0-9]{2}){5}$";
        return Pattern.compile(patternMac).matcher(mac).find();
    }
}
