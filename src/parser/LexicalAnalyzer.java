package parser;

import exceptions.ParserException;

public class LexicalAnalyzer {

    private String data;
    private int currentPosition, previousPosition;
    private Token currentToken;
    private String currentTokenString;

    public enum Token {
        NAME,
        SEMICOLON,
        COMMA,
        ASTERISK,
        END;
    }

    public LexicalAnalyzer(String data) throws ParserException {
        this.data = data;
        currentPosition = 0;
        updateToken();
    }

    public Token getCurrentToken() {
        return currentToken;
    }

    public String getCurrentTokenString() {
        return currentTokenString;
    }

    public int getPreviousPosition() {
        return previousPosition;
    }

    public String getData() {
        return data;
    }

    public Token updateToken() throws ParserException {
        previousPosition = currentPosition;
        while (currentPosition != data.length() && Character.isWhitespace(data.charAt(currentPosition))) {
            currentPosition++;
        }
        if (currentPosition == data.length()) {
            currentToken = Token.END;
            return Token.END;
        } else if (Character.isAlphabetic(current()) || current() == '_') {
            int startPosition = currentPosition;
            while (currentPosition != data.length() && (Character.isAlphabetic(current()) || Character.isDigit(current()) || current() == '_')) {
                currentPosition++;
            }
            currentTokenString = data.substring(startPosition, currentPosition);
            currentToken = Token.NAME;
        } else if (current() == ';') {
            currentTokenString = ";";
            currentToken = Token.SEMICOLON;
            currentPosition++;
        } else if (current() == ',') {
            currentTokenString = ",";
            currentToken = Token.COMMA;
            currentPosition++;
        } else if (current() == '*') {
            currentTokenString = "*";
            currentToken = Token.ASTERISK;
            currentPosition++;
        } else if (currentPosition == data.length()) {
            currentToken = Token.END;
        } else {
            throw new ParserException(data, currentPosition, "unexpected character");
        }
        return currentToken;
    }

    private char current() {
        return data.charAt(currentPosition);
    }
}
