package game;

import java.util.ArrayList;
import java.util.Map;

public class Game {

    private Map<String, String> tags;
    private ArrayList<Move> moves;


    public Map<String, String> getTags() {
        return tags;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }

    public ArrayList<Move> getMoves() {
        return moves;
    }

    public void setMoves(ArrayList<Move> moves) {
        this.moves = moves;
    }
}
