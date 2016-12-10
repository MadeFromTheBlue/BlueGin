package blue.made.bluegin.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.nio.ByteBuffer;
import java.util.LinkedList;

public class VertexFormat {
    public static class Attrib {
        private Attrib(int type, int bytes) {
            this(1, type, bytes, false);
        }

        private Attrib(Attrib old, int veclen) {
            this(veclen, old.type, (old.bytes / old.veclen) * veclen, old.isnorm);
        }

        private Attrib(Attrib old, boolean isnorm) {
            this(old.veclen, old.type, old.bytes, isnorm);
        }

        private Attrib(int veclen, int type, int bytes, boolean isnorm) {
            this.veclen = veclen;
            this.type = type;
            this.bytes = bytes;
            this.isnorm = isnorm;
        }

        private int veclen;
        private int type;
        private int bytes;
        private boolean isnorm;

        public static final Attrib BYTE = new Attrib(GL11.GL_BYTE, 1);
        public static final Attrib UNSIGNED_BYTE = new Attrib(GL11.GL_UNSIGNED_BYTE, 1);
        ;
        public static final Attrib SHORT = new Attrib(GL11.GL_SHORT, 2);
        public static final Attrib UNSIGNED_SHORT = new Attrib(GL11.GL_UNSIGNED_SHORT, 2);
        public static final Attrib INT = new Attrib(GL11.GL_INT, 4);
        public static final Attrib UNSIGNED_INT = new Attrib(GL11.GL_UNSIGNED_INT, 4);
        public static final Attrib FLOAT = new Attrib(GL11.GL_FLOAT, 4);
        public static final Attrib DOUBLE = new Attrib(GL11.GL_DOUBLE, 8);

        //public static final Attrib COLOR = new Attrib(GL11.GL_BGRA, GL11.GL_DOUBLE, 8);

        public static Attrib vector1(Attrib of) {
            if (of.type == -1) throw new IllegalArgumentException("Cannot use an empty attribute in a vector");
            return new Attrib(of, 1);
        }

        public static Attrib vector2(Attrib of) {
            if (of.type == -1) throw new IllegalArgumentException("Cannot use an empty attribute in a vector");
            return new Attrib(of, 2);
        }

        public static Attrib vector3(Attrib of) {
            if (of.type == -1) throw new IllegalArgumentException("Cannot use an empty attribute in a vector");
            return new Attrib(of, 3);
        }

        public static Attrib vector4(Attrib of) {
            if (of.type == -1) throw new IllegalArgumentException("Cannot use an empty attribute in a vector");
            return new Attrib(of, 4);
        }

        public static Attrib normalized(Attrib of) {
            if (of.type == -1) throw new IllegalArgumentException("Cannot create a normalized empty attribute");
            switch (of.type) {
                case GL11.GL_FLOAT:
                case GL11.GL_DOUBLE:

            }
            return new Attrib(of, true);
        }

        /**
         * Ignore some length of bytes
         */
        public static Attrib empty(int bytes) {
            return new Attrib(1, -1, bytes, false) {
                protected void apply(int index, int stride, int totalSize, int byteOffset) {
                }

                ;
            };
        }

        public Attrib vector1() {
            return vector1(this);
        }

        public Attrib vector2() {
            return vector2(this);
        }

        public Attrib vector3() {
            return vector3(this);
        }

        public Attrib vector4() {
            return vector4(this);
        }

        public Attrib normalized() {
            return normalized(this);
        }

        public Attrib ignore() {
            return empty(this.bytes);
        }

        protected void apply(int index, int stride, int totalSize, int byteOffset) {
            GL20.glVertexAttribPointer(index, veclen, type, isnorm, stride, (long) byteOffset);
        }

        protected void apply(int index, int stride, int totalSize, ByteBuffer firstMemLoc) {
            GL20.glVertexAttribPointer(index, veclen, type, isnorm, stride, firstMemLoc);
        }
    }

    private static class AttribInfo {
        Attrib type;
        int offset;
        int index;
        String name;
    }

    static class Builder {
        private int totalVertSize = 0;
        private int currentPos = 0;
        private int attribCount = 0;
        private LinkedList<AttribInfo> attribs;

        public void add(Attrib attibuteFormat, String name) {
            AttribInfo info = new AttribInfo();
            info.index = attribCount++;
            info.offset = currentPos;
            currentPos += attibuteFormat.bytes;
            totalVertSize += attibuteFormat.bytes;
            attribs.add(info);
        }

        public VertexFormat build() {
            VertexFormat format = new VertexFormat();
            add(Attrib.UNSIGNED_BYTE.vector4().normalized(), "color");
            return format;
        }
    }

    public Builder builder() {
        return new Builder();
    }
}
