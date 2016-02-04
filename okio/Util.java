package okio;

import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import java.nio.charset.Charset;

public final class Util {
    public static final Charset UTF_8;

    static {
        UTF_8 = Charset.forName("UTF-8");
    }

    public static void checkOffsetAndCount(long size, long offset, long byteCount) {
        if ((offset | byteCount) < 0 || offset > size || size - offset < byteCount) {
            throw new ArrayIndexOutOfBoundsException(String.format("size=%s offset=%s byteCount=%s", new Object[]{Long.valueOf(size), Long.valueOf(offset), Long.valueOf(byteCount)}));
        }
    }

    public static short reverseBytesShort(short s) {
        int i = s & SupportMenu.USER_MASK;
        return (short) (((MotionEventCompat.ACTION_POINTER_INDEX_MASK & i) >>> 8) | ((i & MotionEventCompat.ACTION_MASK) << 8));
    }

    public static int reverseBytesInt(int i) {
        return ((((ViewCompat.MEASURED_STATE_MASK & i) >>> 24) | ((16711680 & i) >>> 8)) | ((MotionEventCompat.ACTION_POINTER_INDEX_MASK & i) << 8)) | ((i & MotionEventCompat.ACTION_MASK) << 24);
    }

    public static void sneakyRethrow(Throwable t) {
        throw t;
    }

    public static boolean arrayRangeEquals$5c8eef72(byte[] a, byte[] b, int byteCount) {
        for (int i = 0; i < byteCount; i++) {
            if (a[i + 0] != b[i + 0]) {
                return false;
            }
        }
        return true;
    }
}
