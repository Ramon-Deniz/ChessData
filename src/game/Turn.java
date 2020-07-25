package game;

public class Turn {

    /**
     * Bits 1 = not Significant
     * Bits 2-4 = piece
     * Bits 5-8 = root (file/rank)
     */
    private byte identity;

    /**
     * Bits 1-2 = not significant
     * Bits 3-8 = square (e4, g7, etc.)
     */
    private byte square;

    /**
     * Bits 1-3 = promotion
     * Bits 4-5 = checkType
     * Bit 6 = is capture
     * Bits 7-8 = castle type
     */
    private byte events;

    public Turn() {
        identity = 0;
        square = 0;
        events = 0;
    }

    // sets the piece type (K, B, N, etc.)
    public void setPiece(byte piece) {
        identity |= piece;
        identity <<= 4;
    }

    // sets the file/rank the piece moved from
    public void setRoot(byte root) {
        identity |= root;
    }

    // sets the square in which the piece moves to
    public void setSquare(byte square) {
        this.square = square;
    }

    public void setPromotion(byte promotion) {
        events |= promotion;
        events <<= 2;
    }

    public void setCheckType(byte checkType) {
        events |= checkType;
        events <<= 1;
    }

    public void setCapture(byte capture) {
        events |= capture;
        events <<= 2;
    }

    public void setCastle(byte castle) {
        events |= castle;
    }

}
