package blue.made.bluegin.render;

import blue.made.bluegin.render.GLExtension;
import blue.made.bluegin.render.GLRequires;
import org.lwjgl.opengl.*;

/**
 * Created by Sam Sartor on 12/9/2016.
 */
public class GLError extends Exception {
    public static enum Code {
        INVALID_ENUM(GL11.GL_INVALID_ENUM),
        INVALID_VALUE(GL11.GL_INVALID_VALUE),
        INVALID_OPERATION(GL11.GL_INVALID_OPERATION),
        STACK_OVERFLOW(GL11.GL_STACK_OVERFLOW),
        STACK_UNDERFLOW(GL11.GL_STACK_UNDERFLOW),
        OUT_OF_MEMORY(GL11.GL_OUT_OF_MEMORY),
        @GLRequires(major = 3, minor = 0)
        INVALID_FRAMEBUFFER_OPERATION(GL30.GL_INVALID_FRAMEBUFFER_OPERATION),
        @GLRequires(major = 4, minor = 5)
        CONTEXT_LOST(GL45.GL_CONTEXT_LOST),
        @GLExtension(name = "ARB_imaging")
        TABLE_TOO_LARGE(ARBImaging.GL_TABLE_TOO_LARGE);

        public final int glcode;

        Code(int code) {
            this.glcode = code;
        }
    }

    public final Code code;

    private GLError(Code code) {
        super("OpenGL Error: " + (code != null ? code.name() : "UNKNOWN"));
        this.code = code;
    }

    public GLError(int code) {
       this(find(code));
    }

    public static Code find(int glcode) {
        for (Code c : Code.values()) {
            if (c.glcode == glcode) return c;
        }
        return null;
    }
}
