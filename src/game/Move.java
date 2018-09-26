package game;

public class Move {

    public enum Piece {
        PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING, CASTLE;

        public String getPiece() {
            switch (this) {
                case PAWN:
                    return "P";
                case KNIGHT:
                    return "N";
                case BISHOP:
                    return "B";
                case ROOK:
                    return "R";
                case QUEEN:
                    return "Q";
                case KING:
                    return "K";
                default:
                    return "C";
            }
        }
    }

    public enum Turn {
        WHITE, BLACK
    }

    public enum Winner {
        WHITE, BLACK, DRAW, UNKNOWN;

        public String getWinner() throws Exception {
            switch (this) {
                case WHITE:
                    return "W";
                case BLACK:
                    return "B";
                case DRAW:
                    return "D";
                case UNKNOWN:
                    return "U";
                default:
                    throw new Exception("Error with winner.");
            }
        }
    }

    private Coordinate location;
    private int turn;
    private boolean didCapture;
    private boolean didCheck;
    private Piece type;
    private Turn turnType;
    private char piece;

    public Move(Coordinate location, int turn, boolean didCapture, char piece, boolean wasWhitesTurn,
                boolean didCheck) throws Exception {
        this.location = location;
        this.turn = turn;
        this.didCapture = didCapture;
        this.didCheck = didCheck;
        setType(piece);
        setTurn(wasWhitesTurn);
    }

    private void setType(char piece) throws Exception {
        switch (piece) {
            case 'P':
                type = Piece.PAWN;
                this.piece = piece;
                break;
            case 'N':
                type = Piece.KNIGHT;
                this.piece = piece;
                break;
            case 'B':
                type = Piece.BISHOP;
                this.piece = piece;
                break;
            case 'R':
                type = Piece.ROOK;
                this.piece = piece;
                break;
            case 'Q':
                type = Piece.QUEEN;
                this.piece = piece;
                break;
            case 'K':
                type = Piece.KING;
                this.piece = piece;
                break;
            case 'O':
                type = Piece.CASTLE;
                this.piece = piece;
                break;
            default:
                throw new Exception("Error with piece type: " + piece);
        }
    }

    private void setTurn(boolean wasWhitesTurn) {
        if (wasWhitesTurn) {
            turnType = Turn.WHITE;
        } else {
            turnType = Turn.BLACK;
        }
    }

    private String getType() {
        switch (type) {
            case PAWN:
                return "Pawn";
            case KNIGHT:
                return "Knight";
            case BISHOP:
                return "Bishop";
            case ROOK:
                return "Rook";
            case QUEEN:
                return "Queen";
            case KING:
                return "King";
            default:
                return "Castle";
        }
    }

    public char getFile() {
        return location.getFile();
    }

    public char getRank() {
        return location.getRank();
    }

    public int getTurn() {
        return turn;
    }

    public String getSide() {
        switch (turnType) {
            case WHITE:
                return "W";
            default:
                return "B";
        }
    }

    public boolean didCapture() {
        return didCapture;
    }

    public boolean didCheck() {
        return didCheck;
    }

    public Turn getTurnType() {
        return turnType;
    }

    public String getPiece() {
        return type.getPiece();
    }

    public String toString() {
        String move = "" + turn + ". " + piece;
        if (didCapture) {
            return move + "x";
        }
        move += location;
        if (didCheck) {
            return move + "+";
        }
        return move;
    }
}
