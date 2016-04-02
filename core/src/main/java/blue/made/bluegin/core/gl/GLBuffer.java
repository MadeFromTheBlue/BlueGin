package blue.made.bluegin.core.gl;

import org.lwjgl.opengl.*;

import java.nio.ByteBuffer;

@GLRequires("1.5")
public abstract class GLBuffer extends GLObject {
	public static enum AccessFrequency {
		/**
		 * The data store contents will be modified once and used many times.
		 */
		STATIC,
		/**
		 * The data store contents will be modified repeatedly and used many times.
		 */
		DYNAMIC,
		/**
		 * The data store contents will be modified once and used at most a few times.
		 */
		STREAM
	}

	public static enum AccessType {
		/**
		 * The data store contents are modified by the application, and used as the source for GL drawing and image specification commands.
		 */
		DRAW,
		/**
		 * The data store contents are modified by reading data from the GL, and used to return that data when queried by the application.
		 */
		READ,
		/**
		 * The data store contents are modified by reading data from the GL, and used as the source for GL drawing and image specification commands.
		 */
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
		throw new IllegalArgumentException("Somehow an invalid AccessFrequency or AccessType was provided. Maybe one was null?");
	}

	public static enum BufferType {
		ARRAY_BUFFER(GL15.GL_ARRAY_BUFFER),
		ELEMENT_ARRAY_BUFFER(GL15.GL_ELEMENT_ARRAY_BUFFER),
		COPY_READ_BUFFER(GL31.GL_COPY_READ_BUFFER),
		COPY_WRITE_BUFFER(GL31.GL_COPY_WRITE_BUFFER),
		PIXEL_UNPACK_BUFFER(GL21.GL_PIXEL_UNPACK_BUFFER),
		PIXEL_PACK_BUFFER(GL21.GL_PIXEL_PACK_BUFFER),
		QUERY_BUFFER(GL44.GL_QUERY_BUFFER),
		TEXTURE_BUFFER(GL31.GL_TEXTURE_BUFFER),
		TRANSFORM_FEEDBACK_BUFFER(GL30.GL_TRANSFORM_FEEDBACK_BUFFER),
		UNIFORM_BUFFER(GL31.GL_UNIFORM_BUFFER),
		DRAW_INDIRECT_BUFFER(GL40.GL_DRAW_INDIRECT_BUFFER),
		ATOMIC_COUNTER_BUFFER(GL42.GL_ATOMIC_COUNTER_BUFFER),
		DISPATCH_INDIRECT_BUFFER(GL43.GL_DISPATCH_INDIRECT_BUFFER),
		SHADER_STORAGE_BUFFER(GL43.GL_SHADER_STORAGE_BUFFER);

		public final int glenum;

		BufferType(int glenum) {
			this.glenum = glenum;
		}
	}

	public static class ContentsBuilder {
		private BufferType type;
		private AccessFrequency accessfreq = AccessFrequency.STATIC;
		private AccessType accesstype = AccessType.DRAW;

		private ContentsBuilder(BufferType type) {
			this.type = type;
		}

		/**
		 * This does not change the operations available for this buffer, but is can drastically effect the performance
		 * of those operations. You should choose the appropriate access frequency for best results.
		 * The default value is STATIC.
		 *
		 * @see <a href="https://www.opengl.org/wiki/Buffer_Object#Buffer_Object_Usage">Buffer Object Usage</a>
		 * @param freq The predicted frequency at which the buffer will used and changed
		 */
		public void set(AccessFrequency freq) {
			accessfreq = freq;
		}

		/**
		 * This does not change the operations available for this buffer, but is can drastically effect the performance
		 * of those operations. You should choose the appropriate access type for best results.
		 * The default value is DRAW.
		 *
		 * @see <a href="https://www.opengl.org/wiki/Buffer_Object#Buffer_Object_Usage">Buffer Object Usage</a>
		 * @param type The predicted source of read and write operations to this buffer
		 */
		public void set(AccessType type) {
			accesstype = type;
		}

		/**
		 * This does not change the operations available for this buffer, but is can drastically effect the performance
		 * of those operations. You should choose the appropriate access type and frequency for best results.
		 * The default value is STATIC, DRAW.
		 *
		 * @see <a href="https://www.opengl.org/wiki/Buffer_Object#Buffer_Object_Usage">Buffer Object Usage</a>
		 * @param freq The predicted frequency at which the buffer will used and changed
		 * @param type The predicted source of read and write operations to this buffer
		 */
		public void set(AccessFrequency freq, AccessType type) {
			accessfreq = freq;
			accesstype = type;
		}

		public void build(GLBuffer to) {
			GL15.glBindBuffer(type.glenum, to.id);
			GL15.glBufferData(0, (ByteBuffer) null, 0);
		};
	}

	public static ContentsBuilder builder(BufferType type) {
		return new ContentsBuilder(type);
	}

	public GLBuffer(int id) {
		super(GL15.glGenBuffers());
	}

	@Override
	public void delete() {
		GL15.glDeleteBuffers(this.id);
	}
}
