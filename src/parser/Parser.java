package parser;

import exceptions.ParserException;
import tree.Graph;
import tree.Tree;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Parser {

    LexicalAnalyzer lexicalAnalyzer;

    public Parser() {

    }

    private Tree parseP() throws ParserException {
        var currentToken = lexicalAnalyzer.getCurrentToken();
        Tree result;
        switch (currentToken) {
            case NAME -> {
                result = new Tree("P", parseT());
            }
            case ASTERISK -> {
                lexicalAnalyzer.updateToken();
                result = new Tree("P", new Tree("*"), parseP());
            }
            case END -> {
                result = new Tree("P", new Tree("<null>"));
            }
            default -> {
                throw new ParserException(lexicalAnalyzer.getData(), lexicalAnalyzer.getPreviousPosition(),
                        "unexpected token found");
            }
        }
        return result;
    }

    private Tree parseVPrime() throws ParserException {
        var currentToken = lexicalAnalyzer.getCurrentToken();
        Tree result;
        switch (currentToken) {
            case COMMA -> {
                lexicalAnalyzer.updateToken();
                result = new Tree("V'", new Tree(","), parseP(), parseVPrime());
            }
            case END, SEMICOLON -> {
                result = new Tree("V'", new Tree("<null>"));
            }
            default -> {
                throw new ParserException(lexicalAnalyzer.getData(), lexicalAnalyzer.getPreviousPosition(),
                        "unexpected token found");
            }
        }
        return result;
    }

    private Tree parseV() throws ParserException {
        var currentToken = lexicalAnalyzer.getCurrentToken();
        switch (currentToken) {
            case END -> {
                return new Tree("V");
            }
            default -> {
                return new Tree("V", parseP(), parseVPrime());
            }
        }
    }

    private Tree parseT() throws ParserException {
        var currentToken = lexicalAnalyzer.getCurrentToken();
        Tree result;
        switch (currentToken) {
            case END -> {
                result = new Tree("T");
            }
            case NAME -> {
                result = new Tree("T", new Tree(lexicalAnalyzer.getCurrentTokenString()));
                lexicalAnalyzer.updateToken();
            }
            default -> {
                throw new ParserException(lexicalAnalyzer.getData(), lexicalAnalyzer.getPreviousPosition(),
                        "unexpected token found");
            }
        }
        return result;
    }

    private Tree parseD() throws ParserException {
        Tree type = parseT();
        Tree variables = parseV();
        return new Tree("D", type, variables);
    }


    private Tree parseE() throws ParserException {
        var currentToken = lexicalAnalyzer.getCurrentToken();
        Tree result;
        switch (currentToken) {
            case END -> {
                result = new Tree("E", new Tree("<null>"));
            }
            default -> {
                Tree declaration = parseD();
                if (lexicalAnalyzer.getCurrentToken() == LexicalAnalyzer.Token.SEMICOLON) {
                    lexicalAnalyzer.updateToken();
                } else {
                    throw new ParserException(lexicalAnalyzer.getData() + "  ", lexicalAnalyzer.getPreviousPosition(),
                            "expected semicolon");
                }
                Tree other = parseE();
                result = new Tree("E", declaration, new Tree(";"), other);
            }
        }
        return result;
    }

    public Tree parse(String line) throws ParserException {
        lexicalAnalyzer = new LexicalAnalyzer(line);
        return parseE();
    }

    public static void printUsage() {
        System.out.println("Expected two arguments\n" +
                "<outputPath> - path for output file\n" +
                "<expression> - expression for parsing");
    }

    public static void main(String[] args) throws ParserException {
        if (args.length != 2) {
            printUsage();
        } else {
            String outputPath = args[0];
            String expression = args[1];
            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputPath))) {
                Parser parser = new Parser();
                Tree result = parser.parse(expression);

                List<String> dotResult = result.collectToGraph().toDot();
                for (var line : dotResult) {
                    writer.write(line);
                    writer.write("\n");
                }
            } catch (IOException e) {
                System.err.println("Unable to save parsing result");
            } catch (ParserException e) {
                System.err.println(e.getMessage());
            }
        }
    }

}
