package blue.made.bluegin.core.gl;

import blue.made.bluegin.core.BlueGin;

public abstract class GLObject {
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

	protected GLObject(int id) {
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
	 * */
	public abstract void delete();

	@Override
	public void finalize() {
		if (deleteOnGC) {
			delete();
		}
	}
}
