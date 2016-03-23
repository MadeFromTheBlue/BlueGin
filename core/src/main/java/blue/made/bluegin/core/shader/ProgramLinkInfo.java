package blue.made.bluegin.core.shader;

public class ProgramLinkInfo {
	public enum Error {
		NONE,
		PROGRAM_NOT_CREATED,
		LINK_ERROR
	}

	public ProgramLinkInfo(Error state, String log) {
		this.state = state;
		this.log = log;
	}

	public final Error state;
	public final String log;
}