package info.breezes.itebooks.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by jianxingqiao on 14-6-7.
 */
public class DigestUtils {
    private static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String md5(String str) {
        try {
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(str.getBytes());
            byte[] bytes = mdInst.digest();
            mdInst.reset();
            int j = bytes.length;
            char mds[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = bytes[i];
                mds[k++] = hexDigits[byte0 >>> 4 & 0xf];
                mds[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(mds);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
