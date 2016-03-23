package blue.made.bluegin.core.exception;

public class GLUnsupportedException extends GLException {
	private static final long serialVersionUID = 2563885303640220814L;

	public String op;

	public GLUnsupportedException(String op) {
		super(op);
	}
}
