package blue.made.bluegin.core.gl;

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
}
