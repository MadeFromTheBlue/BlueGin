package blue.made.bluegin.core.gl;

import org.lwjgl.opengl.GL11;

public class GL 
{
	/** From: GL_VERSION_1_1 */
	public static void enable(GLEnum cap)
	{
		GL11.glEnable(cap.value());
	}
	
	/** From: GL_VERSION_1_1 */
	public static void disable(GLEnum cap)
	{
		GL11.glDisable(cap.value());
	}
}
