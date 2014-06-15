package info.breezes.itebooks.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by jianxingqiao on 14-6-14.
 */
public class URLUtils {
    public static String encode(String param, String charset) {
        try {
            return URLEncoder.encode(param, charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            return URLEncoder.encode(param, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
