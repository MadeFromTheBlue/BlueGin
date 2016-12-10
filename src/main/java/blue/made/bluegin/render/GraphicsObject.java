package blue.made.bluegin.render;

import blue.made.bluegin.BlueGin;

public abstract class GraphicsObject {
    /**
     * The object id assigned by glGen*()
     */
    public final int id;

    /**
     * Should BlueGin call glDelete*(id) on this object when it is garbage collected? Keep in mind
     * <a href="https://www.opengl.org/wiki/OpenGL_Object#Object_Creation_and_Destruction">OpenGL object destruction
     * rules</a>. If this is, say, a texture linked to a framebuffer, it will only be deleted once the framebuffer is
     * deleted (or it is unlinked) no matter when glDelete is called on it.
     * <br>
     */
    public boolean deleteOnGC = BlueGin.DEFAULT_DELETE_ON_GC;

    protected GraphicsObject(int id) {
        this.id = id;
    }

    /**
     * Get the object id assigned by the glGen*() call that created this object.
     */
    public int getObjectID() {
        return id;
    }

    /**
     * Preform the standard glDelete*() call on this object. Keep in mind
     * <a href="https://www.opengl.org/wiki/OpenGL_Object#Object_Creation_and_Destruction">OpenGL object destruction
     * rules</a>. If this is, say, a texture linked to a framebuffer, it will only be deleted once the framebuffer is
     * deleted (or it is unlinked) no matter when glDelete is called on it.
     * <br>
     * Because of this, other methods of this object will still attempt to preform actions even after delete has been
     * called (as the object may still exist and be accessible). This can produce undefined behavior as OpenGL calls
     * referencing this object can continue to be made long after deletion. There SHOULD be no effect in most cases
     * as no object is bound and so most operations will be silently ignored, but that cannot be guaranteed.
     */
    public abstract void delete();

    @Override
    public void finalize() {
        if (deleteOnGC) {
            delete();
        }
    }
}
