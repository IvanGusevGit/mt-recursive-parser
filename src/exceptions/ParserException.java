package exceptions;

public class ParserException extends Exception {

    public ParserException() {
        super("Parsing error");
    }

    public ParserException(String message) {
        super("Parsing error: " + message);
    }

    public ParserException(String string, int position, String details) {
        this(string.substring(position + 1) + "<<---" + string.substring(position + 1));
    }

}
