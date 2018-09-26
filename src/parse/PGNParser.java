package parse;

import game.Coordinate;
import game.Move;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class PGNParser {

    private BufferedReader reader;
    private int currentChar;
    private boolean isRecentCapture;
    private boolean didGameEnd;
    private Move.Winner recentWinner;
    private boolean isWhitesTurn;
    private int currentTurn;

    public PGNParser(String file) throws IOException {
        File pgnFile = new File(file);
        reader = new BufferedReader(new FileReader(pgnFile));
        read();
        skipToNextGame();
    }

    public void skipToNextGame() throws IOException {
        if (currentChar == -1) {
            return;
        }
        while (currentChar != 10) {
            reader.readLine();
            read();
        }
        currentTurn = 0;
        recentWinner = null;
        didGameEnd = false;
        isWhitesTurn = false;
    }

    public Move getNextMove() throws Exception {
        isWhitesTurn = !isWhitesTurn;
        isRecentCapture = false;
        int piece;
        while (!isNextMove()) {
            if (didGameEnd) {
                endGame();
                return null;
            }
            read();
        }

        Coordinate location = null;

        // ASCII values between a-h
        if (isValidFile()) {
            piece = 'P';
        } else {
            if (currentChar == 'O') {
                location = getLocationOfCastle();
                read();
                return new Move(location, currentTurn, isRecentCapture, 'O', isWhitesTurn, false);
            }
            piece = currentChar;
        }
        location = getNextLocation();
        return new Move(location, currentTurn, isRecentCapture, (char) piece, isWhitesTurn, didCheck());
    }

    private Coordinate getNextLocation() throws IOException {
        while (!isValidFile()) {
            if (currentChar == 'x') isRecentCapture = true;
            read();
        }
        int file = currentChar;
        read();
        if (!isValidRank()) {
            return getNextLocation();
        }
        return new Coordinate((char) file, (char) currentChar);
    }

    private boolean isValidFile() {
        return currentChar > 96 && currentChar < 105;
    }

    private boolean didCheck() throws IOException {
        while (currentChar != ' ') {
            if (currentChar == '+') {
                return true;
            }
            read();
        }
        return false;
    }

    public Move.Winner getRecentWinner() {
        return recentWinner;
    }

    private boolean isValidRank() {
        return currentChar > 48 && currentChar < 57;
    }

    private boolean isNextMove() throws IOException {
        if (currentChar == '.') {
            currentTurn++;
            skipUntilNextSpace();
        } else if (currentChar == '{') {
            skipUntilClosingBracket();
        } else if (currentChar == '-') {
            didGameEnd = true;
            read();
            if (currentChar == '0') {
                recentWinner = Move.Winner.WHITE;
            }else {
                read();
                recentWinner = Move.Winner.BLACK;
                if(currentChar == '/'){
                    recentWinner = Move.Winner.DRAW;
                }
            }
            return false;
        } else if (currentChar == '*') {
            didGameEnd = true;
            recentWinner = Move.Winner.UNKNOWN;
        }

        // return true if currentChar is between B and Q.
        return (isValidFile() || (currentChar > 65 && currentChar < 82));
    }

    private void skipUntilNextSpace() throws IOException {
        while (currentChar != ' ') {
            read();
        }
    }

    // reads until currentChar is '}'
    private void skipUntilClosingBracket() throws IOException {
        while (currentChar != '}') {
            read();
        }
    }

    private Coordinate getLocationOfCastle() throws IOException {
        boolean isKingsSideCastle = isKingsSideCastle();
        if (isWhitesTurn && isKingsSideCastle) {
            return new Coordinate('g', '1');
        } else if (isWhitesTurn) {
            return new Coordinate('c', '1');
        } else if (isKingsSideCastle) {
            return new Coordinate('g', '8');
        }
        return new Coordinate('c', '8');
    }

    private boolean isKingsSideCastle() throws IOException {
        read(3);
        if (currentChar == ' ') {
            return true;
        }
        skipUntilNextSpace();
        return false;
    }

    public void read() throws IOException {
        currentChar = reader.read();
    }

    public int getCurrentChar() {
        return currentChar;
    }

    public void skipLines(int times) throws IOException {
        while (times > 0) {
            reader.readLine();
            times--;
        }
    }

    public String readNextLine() throws IOException {
        return reader.readLine();
    }

    private void read(int times) throws IOException {
        while (times > 0) {
            read();
            times--;
        }
    }

    public String getGame() throws IOException {
        if (currentChar == -1) {
            return null;
        }
        int character = reader.read();
        StringBuilder builder = new StringBuilder();
        while (character != 10) {
            builder.append((char) character);
            character = reader.read();
        }
        endGame();
        return builder.toString();
    }

    public boolean isEndOfFile() {
        return currentChar == -1;
    }

    private void endGame() throws IOException {
        reader.readLine();
        reader.readLine();
        currentChar = reader.read();
    }

    public boolean isGameOver() {
        return didGameEnd;
    }

    public void finish() throws IOException {
        reader.close();
    }

}
