package blue.made.bluegin.core.shader;

import blue.made.bluegin.core.fbo.DrawBufferOutput;
import blue.made.bluegin.core.gl.GL;
import blue.made.bluegin.core.gl.GLEnum;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Program {
	private int program = -1;
	private boolean created = false;
	private boolean linked = false;
	private DrawBufferOutput[] outputs;

	public Program(DrawBufferOutput... outputs) {
		this.outputs = outputs;
	}

	public void create() {
		if (!this.created) {
			this.program = GL20.glCreateProgram();
			for (DrawBufferOutput loc : this.outputs) {
				GL30.glBindFragDataLocation(this.program, loc.location(), loc.outputName());
			}
			this.created = true;
		}
	}

	public ProgramLinkInfo link() {
		if (!this.created) {
			return new ProgramLinkInfo(ProgramLinkInfo.Error.PROGRAM_NOT_CREATED, null);
		}
		GL.linkProgram(this.program);
		if (GL.getProgrami(this.program, GLEnum.LINK_STATUS) == 0) {
			String log = GL.getProgramInfoLog(this.program);
			return new ProgramLinkInfo(ProgramLinkInfo.Error.LINK_ERROR, log);
		}
		linked = true;
		return new ProgramLinkInfo(ProgramLinkInfo.Error.NONE, null);
	}

	public void delete() {
		if (this.created) {
			GL.deleteProgram(this.program);
			this.created = false;
			this.linked = false;
		}
	}

	public void bind() {
		if (this.created && this.linked) {
			GL.useProgram(this.program);
		}
	}

	public void unbind() {
		GL.useProgram(0);
	}

	public int getProgram() {
		if (this.created) {
			return this.program;
		}
		return -1;
	}
}
