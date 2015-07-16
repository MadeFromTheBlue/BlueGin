package blue.made.bluegin.core.exception;

public class GLException extends RuntimeException
{
	private static final long serialVersionUID = 2563885303640220814L;
	
	public GLException()
	{
		super();
	}
	
	public GLException(String message)
	{
		super(message);
	}
	
	public GLException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	public GLException(Throwable cause)
	{
		super(cause);
	}
}
