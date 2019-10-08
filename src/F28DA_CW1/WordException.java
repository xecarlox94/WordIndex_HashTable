package F28DA_CW1;

public class WordException extends Exception {
	
	//Constructor for the class takes a String containing the error message
	public WordException(String errorMsg) {
		
		// it calls its super Exception, using its constructor error message
		super(errorMsg);
	}
}
