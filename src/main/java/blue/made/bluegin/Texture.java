package blue.made.bluegin;

import com.sun.beans.editors.ByteEditor;
import org.joml.*;

import java.nio.ByteBuffer;

/**
 * Created by sam on 2/17/17.
 */
public class Texture {
    private static long nextSysId = 0;
    private long sysId = nextSysId++;

    public Texture(String format, int width, int height, ByteBuffer data) {
        if (!data.isDirect()) {
            throw new IllegalArgumentException("Buffer must be direct");
        }
        native_init(sysId, format, width, height, data);
    }

    @Override
    public void finalize() {
        native_delete(sysId);
    }

    private static native boolean native_init(long id, String format, int width, int height, ByteBuffer data);
    private static native void native_delete(long id);
}
