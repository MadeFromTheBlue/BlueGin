package blue.made.bluegin.core.gl;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class GL 
{
	public static void enable(GLEnum cap)
	{
		GL11.glEnable(cap.value());
	}
	
	public static void disable(GLEnum cap)
	{
		GL11.glDisable(cap.value());
	}
	
	public static int createShader(GLEnum type)
	{
		return GL20.glCreateShader(type.value());
	}
	
	public static void shaderSource(int shader, String source)
	{
		GL20.glShaderSource(shader, source);
	}
	
	public static void compileShader(int shader)
	{
		GL20.glCompileShader(shader);
	}
	
	public static int getShaderi(int shader, GLEnum pname)
	{
		return GL20.glGetShaderi(shader, pname.value());
	}
	
	public static String getShaderInfoLog(int shader)
	{
		int loglength = GL.getShaderi(shader, GLEnum.INFO_LOG_LENGTH);
		return GL20.glGetShaderInfoLog(shader, loglength);
	}
	
	public static void attachShader(int pid, int shader)
	{
		GL20.glAttachShader(pid, shader);
	}
	
	public static void detachShader(int pid, int shader)
	{
		GL20.glDetachShader(pid, shader);
	}
	
	public static void deleteShader(int shader)
	{
		GL20.glDeleteShader(shader);
	}
	
	public static boolean isShader(int shader)
	{
		return GL20.glIsShader(shader);
	}
	
	public static void bindFragDataLocation(int program, int colorNumber, String name)
	{
		GL30.glBindFragDataLocation(program, colorNumber, name);
	}
	
	public static int createProgram()
	{
		return GL20.glCreateProgram();
	}
	
	public static void linkProgram(int program)
	{
		GL20.glLinkProgram(program);
	}
	
	public static int getProgrami(int program, GLEnum pname)
	{
	return GL20.glGetProgrami(program, pname.value());
	}
	
	public static String getProgramInfoLog(int program)
	{
		int loglength = GL.getProgrami(program, GLEnum.INFO_LOG_LENGTH);
		return GL20.glGetProgramInfoLog(program, loglength);
	}
	
	public static void deleteProgram(int program)
	{
		GL20.glDeleteProgram(program);
	}
	
	public static void useProgram(int program)
	{
		GL20.glUseProgram(program);
	}
}
