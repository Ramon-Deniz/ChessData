package sqlite;

import game.Move;
import parse.PGNParser;

import java.io.IOException;
import java.sql.SQLException;

public class GameImporter {

    private PGNParser pgnParser;
    private SQLiteInterface sqLiteInterface;
    private long gameID;
    private long moveID;
    private long matches;

    public GameImporter(String pgnFile) throws IOException, SQLException {
        pgnParser = new PGNParser(pgnFile);
        sqLiteInterface = new SQLiteInterface();
        matches = 0;
        setGameID();
        setMoveID();
    }

    private void setGameID() throws SQLException {
        gameID = sqLiteInterface.getCurrentGameID();
    }

    private void setMoveID() throws SQLException {
        moveID = sqLiteInterface.getCurrentMoveID();
    }

    public void addData() throws Exception {
        while(!pgnParser.isEndOfFile()){
            int turn = -1;
            Move temp = pgnParser.getNextMove();
            while(temp!=null){
                turn = temp.getTurn();
                sqLiteInterface.addMove(moveID, gameID, temp);
                moveID++;
                temp = pgnParser.getNextMove();
            }
            sqLiteInterface.addGame(gameID, turn, pgnParser.getRecentWinner().getWinner());
            gameID++;
            matches++;
            if(matches%750==0){
                sqLiteInterface.executeBatch();
            }
            if(matches%25000==0){
                System.out.println(matches);
            }
            pgnParser.skipToNextGame();
        }
    }

    public void finish() throws IOException, SQLException {
        pgnParser.finish();
        sqLiteInterface.finish();
    }

    public long getGameID(){
        return gameID;
    }

    public long getMoveID(){
        return moveID;
    }

    public void reset() throws SQLException {
        sqLiteInterface.reset();
    }

}
