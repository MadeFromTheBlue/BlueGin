package blue.made.bluegin.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class GLUtils {
    public static String getShaderInfoLog(int shader) {
        int loglength = GL20.glGetShaderi(shader, GL20.GL_INFO_LOG_LENGTH);
        return GL20.glGetShaderInfoLog(shader, loglength);
    }

    public static String getProgramInfoLog(int program) {
        int loglength = GL20.glGetProgrami(program, GL20.GL_INFO_LOG_LENGTH);
        return GL20.glGetProgramInfoLog(program, loglength);
    }

    public static GLError getError() {
        int error = GL11.glGetError();
        if (error == GL11.GL_NO_ERROR) return null;
        else return new GLError(error);
    }

    /**
     * BlueGin does not call this automatically (and so will fail silently). As a result, it is smart to call it
     * yourself now and then.
     */
    public static void checkError() throws GLError {
        GLError error = getError();
        if (error != null) throw error;
    }
}
