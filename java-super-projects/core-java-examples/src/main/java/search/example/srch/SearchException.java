package search.example.srch;

public class SearchException extends Exception {

	public SearchException() {
		super();
	}

	public SearchException(String msg) {
		super(msg);
	}

	public SearchException(Throwable cause) {
		super(cause);
	}

	public SearchException(String msg, Throwable cause) {
		super(msg, cause);
	}

}