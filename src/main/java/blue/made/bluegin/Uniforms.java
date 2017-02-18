package blue.made.bluegin;

import org.joml.*;

import java.nio.ByteBuffer;

/**
 * Created by sam on 2/17/17.
 */
public class Uniforms {
    private static long nextSysId = 0;
    private long sysId = nextSysId++;

    public Uniforms() {
        native_init(sysId);
    }

    @Override
    public void finalize() {
        native_delete(sysId);
    }

    public void float_(String name, float v) {
        ByteBuffer buf = ByteBuffer.allocateDirect(4);
        buf.putFloat(v);
        native_data(sysId, name,"float", buf);
    }

    public void vec2(String name, float x, float y) {
        vec2(name, new Vector2f(x, y));
    }

    public void vec2(String name, Vector2f vec) {
        ByteBuffer buf = ByteBuffer.allocateDirect(4 * 2);
        vec.get(buf);
        native_data(sysId, name,"vec2", buf);
    }

    public void vec3(String name, float x, float y, float z) {
        vec3(name, new Vector3f(x, y, z));
    }

    public void vec3(String name, Vector3f vec) {
        ByteBuffer buf = ByteBuffer.allocateDirect(4 * 3);
        vec.get(buf);
        native_data(sysId, name,"vec3", buf);
    }

    public void vec4(String name, float x, float y, float z, float w) {
        vec4(name, new Vector4f(x, y, z, w));
    }

    public void vec4(String name, Vector4f vec) {
        ByteBuffer buf = ByteBuffer.allocateDirect(4 * 4);
        vec.get(buf);
        native_data(sysId, name,"vec4", buf);
    }

    public void mat3(String name, Matrix3f mat) {
        ByteBuffer buf = ByteBuffer.allocateDirect(4 * 9);
        mat.get(buf);
        native_data(sysId, name,"mat3", buf);
    }

    public void mat4(String name, Matrix4f mat) {
        ByteBuffer buf = ByteBuffer.allocateDirect(4 * 16);
        mat.get(buf);
        native_data(sysId, name,"mat4", buf);
    }

    public void remove(String name) {
        native_remove(sysId, name);
    }

    private static native void native_init(long id);
    private static native void native_data(long id, String name, String type, ByteBuffer data);
    private static native void native_remove(long id, String name);
    private static native void native_delete(long id);
}
