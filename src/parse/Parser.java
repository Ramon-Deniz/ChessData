package parse;

import game.Game;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Parser {

    private Game game;
    private Map tags;
    private List<Move> moveList;


    private PushbackReader reader;
    private int line;

    public Parser(String file) {
        try {
            File pgnFile = new File(file);
            reader = new PushbackReader(new FileReader(pgnFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File not found for: " + file);
        }
    }


    private Game nextGame() {
        tags = new HashMap<>();
        moveList = new ArrayList<>();
        game = new Game();
        game.setTags(tags);
        game.setMoves(moveList);
        parseTagList();
        parseMoveList();

        return game;
    }

    private void parseTagList() {

    }

    private void parseMoveList() {

    }

    // parse individual tag [key value]
    private void parseTag() throws Exception {
        char c = peek();
        if (c == '[') {
            c = next();
            String key = nextToken();
            if (!isWhiteSpace(c)) {
                throwException("White space character expected after key.");
            }

            String value = nextToken();
            if (c != ']') {
                throwException("']' character expected after value. ");
            }
            tags.ad
        } else {
            throwException("'[' character expected for start of tag.");
        }
    }

    /**
     * @return the next series of characters as a string with a whitespace being the terminating character
     */
    private String nextToken() {
        StringBuilder stringBuilder = new StringBuilder();
        c = next();
        while (!isWhiteSpace() && c != 0) {
            stringBuilder.append(c);
            c = next();
        }

        return stringBuilder.toString();
    }

    /**
     * @param c
     * @return if character is whitespace
     */
    private boolean isWhiteSpace(char c) {
        if (c == ' ' || c == '\n' || c == '\t')
            return true;
        return false;
    }

    /**
     * Reads the next character from the reader
     *
     * @return character parsed with reader
     */
    private char next() {
        try {
            int c = reader.read();

            return (char) c;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error reading from PushBackReader.");

            return 0;
        }
    }

    /**
     * Peeks at the next character from the reader
     *
     * @return character peeked with reader
     */
    private char peek() {
        try {
            int c = reader.read();
            reader.unread(c);

            return (char) c;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error peeking from PushBackReader.");

            return 0;
        }
    }

    private void throwException(String message) {
        throw new Exception(message + " Line: " + line);
    }

}
