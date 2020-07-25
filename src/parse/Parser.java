package parse;

import game.Game;
import game.Move;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Parser {

    private Game game;
    private Map tags;
    private ArrayList<Move> moveList;

    private char c;
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


    public Game nextGame() throws Exception {
        tags = new HashMap<>();
        moveList = new ArrayList<>();
        game = new Game();
        game.setTags(tags);
        game.setMoves(moveList);
        parseTagList();
        parseMoveList();

        return game;
    }

    private void parseTagList() throws Exception {
        parseTag();
        char pc = peek();
        if (pc == '[') {
            parseTagList();
        }
    }

    private void parseMoveList() {

    }

    /**
     * Parse individual key and value pairs and adds to hashmap
     *
     * @throws Exception
     */
    private void parseTag() throws Exception {
        char pc = peek();
        if (pc == '[') {
            next();
            
            String key = parseKey();
            if (!isWhiteSpace(c)) {
                throwException("White space character expected after key.", c);
            }

            String value = parseValue();
            if (c != ']') {
                throwException("']' character expected after value. ", c);
            }

            readLine();
            line++;
            tags.put(key, value);
        } else {
            throwException("'[' character expected for start of tag.", c);
        }
    }

    /**
     * 
     * @return the key in a tag as a string
     */
    private String parseKey() throws Exception{
        StringBuilder stringBuilder = new StringBuilder();
        next();
        while (!isWhiteSpace(c) && c != 0) {
            stringBuilder.append(c);
            next();
        }

        return stringBuilder.toString();
    }

    /**
     * 
     * @return the value in a tag as a string
     */
    private String parseValue() {
        StringBuilder stringBuilder = new StringBuilder();
        next();
        while (c != ']' && c != 0) {
            stringBuilder.append(c);
            next();
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
    private void next() {
        try {
            int c = reader.read();

            this.c = (char) c;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error reading from PushBackReader.");

            this.c = 0;
        }
    }

    /**
     * Reads from the PushBackReader until a newline character is reached
     */
    private void readLine() {
        next();

        while (c != '\n') {
            next();
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
    
    private void pushBack(char c) {
        try {
            reader.unread(c);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error pushing back for: " + c);
        }
    }

    private void throwException(String message, char c) throws Exception {
        throw new Exception(message + " Line: " + line + ". Current ASCII: " + ((int) (c)));
    }

}
