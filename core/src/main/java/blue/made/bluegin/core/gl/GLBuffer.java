package blue.made.bluegin.core.gl;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.nio.*;

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
		public class BackedOutputStream extends ByteArrayOutputStream {
			public BackedOutputStream(int capacity) {
				super(capacity);
			}

			public byte[] backingArray() {
				return buf;
			}
		}

		private BufferType type;
		private AccessFrequency accessfreq = AccessFrequency.STATIC;
		private AccessType accesstype = AccessType.DRAW;
		private ByteBuffer directData = null;
		private BackedOutputStream streamData;

		private ContentsBuilder(BufferType type) {
			if (type == null)
				throw new IllegalArgumentException("Type cannot be null");
			this.type = type;
		}

		/**
		 * Set the buffer that will be sent to LWJGL. This overrides any data provided by any other builder methods,
		 * but only works if the buffer is direct.
		 * @param buffer A buffer created by {@link BufferUtils BufferUtils} or {@link ByteBuffer#allocateDirect(int)}
		 * @throws IllegalArgumentException If the buffer is not direct
		 */
		public ContentsBuilder setDirectData(ByteBuffer buffer) {
			if (!buffer.isDirect())
				throw new IllegalArgumentException("The provided buffer is not direct");
			directData = buffer;
			return this;
		}

		/**
		 * Set the data that will be stored in the GLBuffer. This overrides any data provided by any other builder
		 * methods. The provided buffer is copied by this method into a new direct buffer that will be used by LWJGL.
		 * @param buffer A buffer of data
		 */
		public ContentsBuilder setDataCopy(ByteBuffer buffer) {
			ByteBuffer direct = BufferUtils.createByteBuffer(buffer.remaining());
			direct.put(buffer);
			directData = direct;
			return this;
		}

		/**
		 * Set the data that will be stored in the GLBuffer. This overrides any data provided by any other builder
		 * methods. If the given buffer is direct, then {@link #setDirectData(ByteBuffer)} is used, otherwise
		 * this method calls {@link #setDataCopy(ByteBuffer)}.
		 * @param buffer A buffer of data
		 */
		public ContentsBuilder setData(ByteBuffer buffer) {
			if (buffer.isDirect())
				directData = buffer;
			else {
				setDataCopy(buffer);
			}
			return this;
		}

		/**
		 * Set the data that will be stored in the GLBuffer. This overrides any data provided by any other builder
		 * methods.
		 * @param buffer A buffer of data
		 */
		public ContentsBuilder setData(ShortBuffer buffer) {
			directData = BufferUtils.createByteBuffer(buffer.remaining() * 2);
			directData.asShortBuffer().put(buffer);
			return this;
		}

		/**
		 * Set the data that will be stored in the GLBuffer. This overrides any data provided by any other builder
		 * methods.
		 * @param buffer A buffer of data
		 */
		public ContentsBuilder setData(IntBuffer buffer) {
			directData = BufferUtils.createByteBuffer(buffer.remaining() * 4);
			directData.asIntBuffer().put(buffer);
			return this;
		}

		/**
		 * Set the data that will be stored in the GLBuffer. This overrides any data provided by any other builder
		 * methods.
		 * @param buffer A buffer of data
		 */
		public ContentsBuilder setData(FloatBuffer buffer) {
			directData = BufferUtils.createByteBuffer(buffer.remaining() * 4);
			directData.asFloatBuffer().put(buffer);
			return this;
		}

		/**
		 * Set the data that will be stored in the GLBuffer. This overrides any data provided by any other builder
		 * methods.
		 * @param buffer A buffer of data
		 */
		public ContentsBuilder setData(DoubleBuffer buffer) {
			directData = BufferUtils.createByteBuffer(buffer.remaining() * 8);
			directData.asDoubleBuffer().put(buffer);
			return this;
		}

		/**
		 * Set the data that will be stored in the GLBuffer. This overrides any data provided by any other builder
		 * methods.
		 * @param data The data that will fill the buffer
		 */
		public ContentsBuilder setData(byte[] data) {
			directData = BufferUtils.createByteBuffer(data.length);
			directData.put(data);
			return this;
		}

		/**
		 * Set the data that will be stored in the GLBuffer. This overrides any data provided by any other builder
		 * methods.
		 * @param data The data that will fill the buffer
		 */
		public ContentsBuilder setData(short[] data) {
			directData = BufferUtils.createByteBuffer(data.length * 2);
			directData.asShortBuffer().put(data);
			return this;
		}

		/**
		 * Set the data that will be stored in the GLBuffer. This overrides any data provided by any other builder
		 * methods.
		 * @param data The data that will fill the buffer
		 */
		public ContentsBuilder setData(int[] data) {
			directData = BufferUtils.createByteBuffer(data.length * 4);
			directData.asIntBuffer().put(data);
			return this;
		}

		/**
		 * Set the data that will be stored in the GLBuffer. This overrides any data provided by any other builder
		 * methods.
		 * @param data The data that will fill the buffer
		 */
		public ContentsBuilder setData(float[] data) {
			directData = BufferUtils.createByteBuffer(data.length * 4);
			directData.asFloatBuffer().put(data);
			return this;
		}

		/**
		 * Set the data that will be stored in the GLBuffer. This overrides any data provided by any other builder
		 * methods.
		 * @param data The data that will fill the buffer
		 */
		public ContentsBuilder setData(double[] data) {
			directData = BufferUtils.createByteBuffer(data.length * 8);
			directData.asDoubleBuffer().put(data);
			return this;
		}

		public DataOutputStream dataStream(int withCapacity) {
			if (streamData == null) {
				streamData = new BackedOutputStream(withCapacity);
			}
			return new DataOutputStream(streamData);
		}

		/**
		 * Provides a DataOutputStream for writing the data that will fill the buffer when {@link #build(GLBuffer)} is
		 * called. Any data written to the DataOutputStream will be ignored if {@link #setDirectData(ByteBuffer) direct data}
		 * is provided. Each call to this method provides a new DataOutputStream, but all DataOutputStreams write to
		 * the same data.
		 */
		public DataOutputStream dataStream() {
			return dataStream(32);
		}

		/**
		 * This does not change the operations available for this buffer, but is can drastically effect the performance
		 * of those operations. You should choose the appropriate access frequency for best results.
		 * The default value is STATIC.
		 *
		 * @see <a href="https://www.opengl.org/wiki/Buffer_Object#Buffer_Object_Usage">Buffer Object Usage</a>
		 * @param freq The predicted frequency at which the buffer will used and changed
		 */
		public ContentsBuilder set(AccessFrequency freq) {
			accessfreq = freq;
			return this;
		}

		/**
		 * This does not change the operations available for this buffer, but is can drastically effect the performance
		 * of those operations. You should choose the appropriate access type for best results.
		 * The default value is DRAW.
		 *
		 * @see <a href="https://www.opengl.org/wiki/Buffer_Object#Buffer_Object_Usage">Buffer Object Usage</a>
		 * @param type The predicted source of read and write operations to this buffer
		 */
		public ContentsBuilder set(AccessType type) {
			accesstype = type;
			return this;
		}

		/**
		 * This does not change the operations available for this buffer, but is can drastically effect the performance
		 * of those operations. You should choose the appropriate access type and frequency for best results.
		 * The default value is STATIC, DRAW.
		 *
		 * @see <a href="https://www.opengl.org/wiki/Buffer_Object#Buffer_Object_Usage">Buffer Object Usage</a>
		 * @param freq The predicted frequency at which the buffer will used and changed
		 * @param type The predicted source of read and write operations to this buffer
		 *
		 * @throws IllegalArgumentException If some required information is not provided or is null
		 */
		public ContentsBuilder set(AccessFrequency freq, AccessType type) {
			accessfreq = freq;
			accesstype = type;
			return this;
		}

		/**
		 * Store the data with the access settings and data type into the provided GLBuffer. Any existing contents of
		 * the GLBuffer will be deleted and replaced.
		 * <br>
		 * On the implementation side, this calls <a href="https://www.opengl.org/wiki/GLAPI/glBufferData">glBufferData(target, size, data, usage)</a>.
		 * @param to The buffer that will store the data
		 */
		public void build(GLBuffer to) {
			boolean flag = false;
			if (directData == null) {
				flag = true;
				if (streamData != null) {
					directData = BufferUtils.createByteBuffer(streamData.size());
					directData.put(streamData.backingArray(), 0, streamData.size());
				} else {
					throw new IllegalArgumentException("No data (or invalid data) has been provided");
				}
			}
			int usageenum = getUsageEnum(accessfreq, accesstype);
			to.target = type;
			to.accessfreq = accessfreq;
			to.accesstype = accesstype;
			to.size = directData.remaining();
			GL15.glBindBuffer(type.glenum, to.id);
			GL15.glBufferData(type.glenum, directData, usageenum);
			if (flag) {
				directData = null;
			}
		};
	}

	/**
	 * Provide a new object for building the data store of a buffer. The returned builder will be used to call
	 * <a href="https://www.opengl.org/wiki/GLAPI/glBufferData">glBufferData(target, size, data, usage)</a>.
	 * @param type The type of data (i.e. texture, vertex data, weird pixel storage thing, etc.)
	 * @return The builder object
	 */
	public static ContentsBuilder contentsBuilder(BufferType type) {
		return new ContentsBuilder(type);
	}

	private int size = 0;
	private BufferType target;
	private AccessFrequency accessfreq;
	private AccessType accesstype;

	public GLBuffer(int id) {
		super(GL15.glGenBuffers());
	}

	/**
	 * Warning! This method requires OpenGL 4.3
	 * <br><br>
	 * This method makes it as though {@link ContentsBuilder#build(GLBuffer) ContentsBuilder.build(this)} was never
	 * called on this object. The uploaded data is deleted.
	 */
	@GLRequires("4.3")
	public void invalidate() {
		GL43.glInvalidateBufferData(this.id);
		target = null;
		accessfreq = null;
		accesstype = null;
		size = 0;
	}

	public int getSize() {
		return size;
	}

	public BufferType getType() {
		return target;
	}

	public AccessFrequency getAccessFreq() {
		return accessfreq;
	}

	public AccessType getAccessType() {
		return accesstype;
	}

	/**
	 * glBind this buffer
	 * @throws IllegalStateException If this buffer does not have a data store as provided by a {@link ContentsBuilder}
	 */
	public void bind() {
		if (target == null)
			throw new IllegalStateException("The buffer has not been initialized with a data store.");
		GL15.glBindBuffer(target.glenum, id);
	}

	/**
	 * Sets a portion of this buffer's data from {@code start} to {@code start + length} to the bytes in {@code from}
	 * starting at {@code offset}. Uses glBufferSubData(target, offset, size, data).
	 * @param start The first byte in the buffer to replace
	 * @param length The number of bytes in the buffer to replace
	 * @param from The bytes to copy into the buffer
	 * @param offset The first index in from
	 * @throws IndexOutOfBoundsException If length, from, and offset are not in a happy relationship
	 * @throws IllegalStateException If this buffer does not have a data store as provided by a {@link ContentsBuilder}
	 */
	public void setData(int start, int length, byte[] from, int offset) {
		ByteBuffer direct = BufferUtils.createByteBuffer(length);
		direct.put(from, offset, length);
		bind();
		GL15.glBufferSubData(target.glenum, start, direct);
	}

	/**
	 * Sets a portion of this buffer's data from {@code start} to {@code start + length} to the bytes in {@code from}
	 * starting at {@code offset}. Uses glBufferSubData(target, offset, size, data).
	 * @param start The first byte in the buffer to replace
	 * @param length The number of bytes in the buffer to replace
	 * @param from The bytes to copy into the buffer
	 * @param offset The first index in from
	 * @throws IndexOutOfBoundsException If length, from, and offset are not in a happy relationship
	 * @throws IllegalStateException If this buffer does not have a data store as provided by a {@link ContentsBuilder}
	 */
	public void setData(int start, int length, short[] from, int offset) {
		ShortBuffer direct = BufferUtils.createShortBuffer(length);
		direct.put(from, offset, length);
		bind();
		GL15.glBufferSubData(target.glenum, start, direct);
	}

	/**
	 * Sets a portion of this buffer's data from {@code start} to {@code start + length} to the bytes in {@code from}
	 * starting at {@code offset}. Uses glBufferSubData(target, offset, size, data).
	 * @param start The first byte in the buffer to replace
	 * @param length The number of bytes in the buffer to replace
	 * @param from The bytes to copy into the buffer
	 * @param offset The first index in from
	 * @throws IndexOutOfBoundsException If length, from, and offset are not in a happy relationship
	 * @throws IllegalStateException If this buffer does not have a data store as provided by a {@link ContentsBuilder}
	 */
	public void setData(int start, int length, int[] from, int offset) {
		IntBuffer direct = BufferUtils.createIntBuffer(length);
		direct.put(from, offset, length);
		bind();
		GL15.glBufferSubData(target.glenum, start, direct);
	}

	/**
	 * Sets a portion of this buffer's data from {@code start} to {@code start + length} to the bytes in {@code from}
	 * starting at {@code offset}. Uses glBufferSubData(target, offset, size, data).
	 * @param start The first byte in the buffer to replace
	 * @param length The number of bytes in the buffer to replace
	 * @param from The bytes to copy into the buffer
	 * @param offset The first index in from
	 * @throws IndexOutOfBoundsException If length, from, and offset are not in a happy relationship
	 * @throws IllegalStateException If this buffer does not have a data store as provided by a {@link ContentsBuilder}
	 */
	public void setData(int start, int length, float[] from, int offset) {
		FloatBuffer direct = BufferUtils.createFloatBuffer(length);
		direct.put(from, offset, length);
		bind();
		GL15.glBufferSubData(target.glenum, start, direct);
	}

	/**
	 * Sets a portion of this buffer's data from {@code start} to {@code start + length} to the bytes in {@code from}
	 * starting at {@code offset}. Uses glBufferSubData(target, offset, size, data).
	 * @param start The first byte in the buffer to replace
	 * @param length The number of bytes in the buffer to replace
	 * @param from The bytes to copy into the buffer
	 * @param offset The first index in from
	 * @throws IndexOutOfBoundsException If length, from, and offset are not in a happy relationship
	 * @throws IllegalStateException If this buffer does not have a data store as provided by a {@link ContentsBuilder}
	 */
	public void setData(int start, int length, double[] from, int offset) {
		DoubleBuffer direct = BufferUtils.createDoubleBuffer(length);
		direct.put(from, offset, length);
		bind();
		GL15.glBufferSubData(target.glenum, start, direct);
	}

	/**
	 * Sets a portion of this buffer's data from {@code start} to {@code start + length} to the bytes in {@code from}.
	 * Uses glBufferSubData(target, offset, size, data).
	 * @param start The first byte in the buffer to replace
	 * @param length The number of bytes in the buffer to replace
	 * @param from The bytes to copy into the buffer
	 * @throws IndexOutOfBoundsException If length and from are not in a happy relationship
	 * @throws IllegalStateException If this buffer does not have a data store as provided by a {@link ContentsBuilder}
	 */
	public void setData(int start, int length, byte[] from) {
		setData(start, length, from, 0);
	}

	/**
	 * Sets a portion of this buffer's data from {@code start} to {@code start + length} to the bytes in {@code from}.
	 * Uses glBufferSubData(target, offset, size, data).
	 * @param start The first byte in the buffer to replace
	 * @param length The number of bytes in the buffer to replace
	 * @param from The bytes to copy into the buffer
	 * @throws IndexOutOfBoundsException If length and from are not in a happy relationship
	 * @throws IllegalStateException If this buffer does not have a data store as provided by a {@link ContentsBuilder}
	 */
	public void setData(int start, int length, short[] from) {
		setData(start, length, from, 0);
	}

	/**
	 * Sets a portion of this buffer's data from {@code start} to {@code start + length} to the bytes in {@code from}.
	 * Uses glBufferSubData(target, offset, size, data).
	 * @param start The first byte in the buffer to replace
	 * @param length The number of bytes in the buffer to replace
	 * @param from The bytes to copy into the buffer
	 * @throws IndexOutOfBoundsException If length and from are not in a happy relationship
	 * @throws IllegalStateException If this buffer does not have a data store as provided by a {@link ContentsBuilder}
	 */
	public void setData(int start, int length, int[] from) {
		setData(start, length, from, 0);
	}

	/**
	 * Sets a portion of this buffer's data from {@code start} to {@code start + length} to the bytes in {@code from}.
	 * Uses glBufferSubData(target, offset, size, data).
	 * @param start The first byte in the buffer to replace
	 * @param length The number of bytes in the buffer to replace
	 * @param from The bytes to copy into the buffer
	 * @throws IndexOutOfBoundsException If length and from are not in a happy relationship
	 * @throws IllegalStateException If this buffer does not have a data store as provided by a {@link ContentsBuilder}
	 */
	public void setData(int start, int length, float[] from) {
		setData(start, length, from, 0);
	}

	/**
	 * Sets a portion of this buffer's data from {@code start} to {@code start + length} to the bytes in {@code from}.
	 * Uses glBufferSubData(target, offset, size, data).
	 * @param start The first byte in the buffer to replace
	 * @param length The number of bytes in the buffer to replace
	 * @param from The bytes to copy into the buffer
	 * @throws IndexOutOfBoundsException If length and from are not in a happy relationship
	 * @throws IllegalStateException If this buffer does not have a data store as provided by a {@link ContentsBuilder}
	 */
	public void setData(int start, int length, double[] from) {
		setData(start, length, from, 0);
	}

	/**
	 * Sets a portion of this buffer's data from {@code start} to {@code start + length} to the bytes in {@code from}.
	 * Uses glBufferSubData(target, offset, size, data).
	 * @param start The first byte in the buffer to replace
	 * @param from The bytes to copy into the buffer
	 * @throws IllegalStateException If this buffer does not have a data store as provided by a {@link ContentsBuilder}
	 */
	public void setData(int start, byte[] from) {
		setData(start, from.length, from, 0);
	}

	/**
	 * Sets a portion of this buffer's data from {@code start} to {@code start + length} to the bytes in {@code from}.
	 * Uses glBufferSubData(target, offset, size, data).
	 * @param start The first byte in the buffer to replace
	 * @param from The bytes to copy into the buffer
	 * @throws IllegalStateException If this buffer does not have a data store as provided by a {@link ContentsBuilder}
	 */
	public void setData(int start, short[] from) {
		setData(start, from.length, from, 0);
	}

	/**
	 * Sets a portion of this buffer's data from {@code start} to {@code start + length} to the bytes in {@code from}.
	 * Uses glBufferSubData(target, offset, size, data).
	 * @param start The first byte in the buffer to replace
	 * @param from The bytes to copy into the buffer
	 * @throws IllegalStateException If this buffer does not have a data store as provided by a {@link ContentsBuilder}
	 */
	public void setData(int start, int[] from) {
		setData(start, from.length, from, 0);
	}

	/**
	 * Sets a portion of this buffer's data from {@code start} to {@code start + length} to the bytes in {@code from}.
	 * Uses glBufferSubData(target, offset, size, data).
	 * @param start The first byte in the buffer to replace
	 * @param from The bytes to copy into the buffer
	 * @throws IllegalStateException If this buffer does not have a data store as provided by a {@link ContentsBuilder}
	 */
	public void setData(int start, float[] from) {
		setData(start, from.length, from, 0);
	}
	/**
	 * Sets a portion of this buffer's data from {@code start} to {@code start + length} to the bytes in {@code from}.
	 * Uses glBufferSubData(target, offset, size, data).
	 * @param start The first byte in the buffer to replace
	 * @param from The bytes to copy into the buffer
	 * @throws IllegalStateException If this buffer does not have a data store as provided by a {@link ContentsBuilder}
	 */
	public void setData(int start, double[] from) {
		setData(start, from.length, from, 0);
	}


	@Override
	public void delete() {
		GL15.glDeleteBuffers(this.id);
	}
}
