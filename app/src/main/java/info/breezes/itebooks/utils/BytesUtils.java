package info.breezes.itebooks.utils;

/**
 * Created by jianxingqiao on 14-6-14.
 */
public class BytesUtils {
    public static String format(long size) {
        if (size >> 10 < 1) {
            return String.format("%dB", size);
        }
        if (size >> 20 < 1) {
            return String.format("%.2fKB", size / 1024.0);
        }
        if (size >> 30 < 1) {
            return String.format("%.2fMB", size / 1024.0 / 1024);
        }
        if (size >> 40 < 1) {
            return String.format("%.2fGB", size / 1024.0 / 1024 / 1024);
        }
        return String.format("%dB", size);
    }
}
