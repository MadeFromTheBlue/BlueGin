package blue.made.bluegin.core.gl;

import org.lwjgl.opengl.*;

public class GLBuffer implements GLObject {
	public static enum AccessFrequency {
		/**The data store contents will be modified once and used many times.*/
		STATIC,
		/**The data store contents will be modified repeatedly and used many times.*/
		DYNAMIC,
		/**The data store contents will be modified once and used at most a few times.*/
		STREAM
	}

	public static enum AccessType {
		/**The data store contents are modified by the application, and used as the source for GL drawing and image specification commands.*/
		DRAW,
		/**The data store contents are modified by reading data from the GL, and used to return that data when queried by the application.*/
		READ,
		/**The data store contents are modified by reading data from the GL, and used as the source for GL drawing and image specification commands.*/
		COPY
	}

	private static int getUsageEnum(AccessFrequency freq, AccessType type) {
		switch (freq) {
			case STATIC:
				switch (type) {
					case DRAW:
						return GL15.GL_STATIC_DRAW;
					case READ:
						return GL15.GL_STATIC_READ;
					case COPY:
						return GL15.GL_STATIC_COPY;
				}
			case DYNAMIC:
				switch (type) {
					case DRAW:
						return GL15.GL_DYNAMIC_DRAW;
					case READ:
						return GL15.GL_DYNAMIC_READ;
					case COPY:
						return GL15.GL_DYNAMIC_COPY;
				}
			case STREAM:
				switch (type) {
					case DRAW:
						return GL15.GL_STREAM_DRAW;
					case READ:
						return GL15.GL_STREAM_READ;
					case COPY:
						return GL15.GL_STREAM_COPY;
				}
		}
		throw new IllegalArgumentException("Somehow and invalid AccessFrequency or AccessType was provided. Maybe one was null?");
	}
}
