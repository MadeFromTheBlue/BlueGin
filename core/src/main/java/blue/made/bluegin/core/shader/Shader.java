package blue.made.bluegin.core.shader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import blue.made.bluegin.core.asset.Asset;
import blue.made.bluegin.core.gl.GL;
import blue.made.bluegin.core.gl.GLEnum;

public class Shader
{
	public static enum ShaderType
	{
		FRAGMENT("fsh", GLEnum.FRAGMENT_SHADER),
		VERTEX("vsh", GLEnum.VERTEX_SHADER),
		GEOMETRY("gsh", GLEnum.GEOMETRY_SHADER);
		
		public final String extension;
		public final GLEnum glMode;
		
		ShaderType(String extension, GLEnum glMode)
		{
			this.extension = extension;
			this.glMode = glMode;
		}
	}
	
	public final ShaderType type;
	public final Asset asset;
	private boolean created = false;
	private boolean isMarkedForDelete = false;
	private int shader = -1;
	
	public Shader(ShaderType type, Asset asset)
	{
		this.type = type;
		this.asset = asset;
	}
	
	public String loadSource() throws IOException
	{
		InputStream in = this.asset.getFileAsset(this.type.extension).getInput();
		if (in == null)
		{
			throw new FileNotFoundException("Could not find shader source: " + this.asset);
		}
		return IOUtils.toString(in);
	}
	
	public ShaderCreateInfo create()
	{
		this.updateDeleteStatus();
		if (this.created)
		{
			return new ShaderCreateInfo(ShaderCreateInfo.Error.ALREADY_CREATED, null, null);
		}
		String source;
		try
		{
			source = this.loadSource();
		}
		catch (FileNotFoundException e)
		{
			return new ShaderCreateInfo(ShaderCreateInfo.Error.SOURCE_MISSING, null, e);
		}
		catch (IOException e)
		{
			return new ShaderCreateInfo(ShaderCreateInfo.Error.IO_EXCEPTION, null, e);
		}
		this.shader = GL.createShader(type.glMode);
		this.created = true;
		GL.shaderSource(this.shader, source);
		GL.compileShader(this.shader);
		if (GL.getShaderi(this.shader, GLEnum.COMPILE_STATUS) == 0)
		{
			String log = GL.getShaderInfoLog(this.shader);
			this.delete();
			return new ShaderCreateInfo(ShaderCreateInfo.Error.COMPILE_ERROR, log, null);
		}
		return new ShaderCreateInfo(ShaderCreateInfo.Error.NONE, null, null);
	}
	
	public boolean attach(Program program)
	{
		this.updateDeleteStatus();
		if (!this.created)
		{
			return false;
		}
		int pid = program.getProgram();
		if (pid <= 0)
		{
			return false;
		}
		GL.attachShader(pid, this.shader);
		return true;
	}
	
	public void detach(Program program)
	{
		this.updateDeleteStatus();
		if (!this.created)
		{
			return;
		}
		int pid = program.getProgram();
		if (pid <= 0)
		{
			return;
		}
		GL.detachShader(pid, this.shader);
	}
	
	public void delete()
	{
		if (this.created)
		{
			this.isMarkedForDelete = true;
			GL.deleteShader(this.shader);
			this.updateDeleteStatus();
		}
	}
	
	private void updateDeleteStatus()
	{
		if (this.isMarkedForDelete)
		{
			if (!GL.isShader(this.shader))
			{
				this.isMarkedForDelete = false;
				this.shader = -1;
				this.created = false;
			}
		}
	}
	
	public int getShader()
	{
		this.updateDeleteStatus();
		if (this.created)
		{
			return this.shader;
		}
		return -1;
	}
	
	@Override
	public String toString()
	{
		return this.asset.asset + "." + this.type.extension;
	}
}
