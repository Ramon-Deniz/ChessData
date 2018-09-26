package sqlite;

import game.Move;

import java.sql.*;

public class SQLiteInterface {

    private final String URL = "jdbc:sqlite:/home/ramon/dev/chess/Chess";
    private final String INSERT_GAME = "INSERT INTO Game (GameID, Turns, Winner) VALUES(?, ?, ?)";
    private final String INSERT_MOVE =
            "INSERT INTO Move (MoveID, Turn, Piece, File, Rank, DidCapture, Side, DidCheck, GameID) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final String INSERT_GAME_MOVE = "INSERT INTO GameMove (MoveID, GameID) VALUES(?, ?)";

    private PreparedStatement addMoveStatement;
    private PreparedStatement addGameStatement;

    private Connection connection;

    public SQLiteInterface() throws SQLException {
        establishConnection();
        addMoveStatement = connection.prepareStatement(INSERT_MOVE);
        addGameStatement = connection.prepareStatement(INSERT_GAME);
    }

    private void establishConnection() {
        try {
            connection = DriverManager.getConnection(URL);
            connection.setAutoCommit(false);
            System.out.println("Connection established.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Unable to create connection to database.");
        }
    }

    public void addMove(long moveID, long gameID, Move move) throws SQLException {
        addMoveStatement.setLong(1, moveID);
        addMoveStatement.setInt(2, move.getTurn());
        addMoveStatement.setString(3, move.getPiece());
        addMoveStatement.setString(4, "" + move.getFile());
        addMoveStatement.setString(5, "" + move.getRank());
        addMoveStatement.setBoolean(6, move.didCapture());
        addMoveStatement.setString(7, move.getSide());
        addMoveStatement.setBoolean(8, move.didCheck());
        addMoveStatement.setLong(9, gameID);
        addMoveStatement.addBatch();
    }


    public void addGame(long gameId, int turns, String winner) throws SQLException {
        addGameStatement.setLong(1, gameId);
        addGameStatement.setInt(2, turns);
        addGameStatement.setString(3, winner);
        addGameStatement.addBatch();
    }

    private ResultSet getGameGeneratedKeys() throws SQLException {
        PreparedStatement statement = connection.prepareStatement(INSERT_GAME, Statement.RETURN_GENERATED_KEYS);
        statement.setObject(1, null);
        statement.setInt(2, 0);
        statement.setString(3, "Temp");
        statement.executeUpdate();
        return statement.getGeneratedKeys();
    }

    private ResultSet getMoveGeneratedKeys() throws SQLException {
        PreparedStatement statement = connection.prepareStatement(INSERT_MOVE, Statement.RETURN_GENERATED_KEYS);
        statement.setObject(1, null);
        statement.setInt(2, 0);
        statement.setString(3, "Temp");
        statement.setString(4, "Temp");
        statement.setString(5, "Temp");
        statement.setBoolean(6, false);
        statement.setString(7, "Black");
        statement.setBoolean(8, false);
        statement.setLong(9, -1);
        statement.executeUpdate();
        return statement.getGeneratedKeys();
    }

    private void removeTempGame(long gameID) throws SQLException {
        String removeQuery = "DELETE FROM Game WHERE GameID=" + gameID;
        String update = "UPDATE SQLITE_SEQUENCE SET SEQ=" + (gameID - 1) + " WHERE NAME='Game'";
        PreparedStatement statement = connection.prepareStatement(removeQuery);
        PreparedStatement updateStatement = connection.prepareStatement(update);
        statement.executeUpdate();
        updateStatement.executeUpdate();
    }

    private void removeTempMove(long moveID) throws SQLException {
        String removeQuery = "DELETE FROM Move WHERE MoveID=" + moveID;
        String update = "UPDATE SQLITE_SEQUENCE SET SEQ=" + (moveID - 1) + " WHERE NAME='Move'";
        PreparedStatement statement = connection.prepareStatement(removeQuery);
        PreparedStatement updateStatement = connection.prepareStatement(update);
        statement.executeUpdate();
        updateStatement.executeUpdate();
    }

    public long getCurrentGameID() throws SQLException {
        ResultSet set = getGameGeneratedKeys();
        if (set.next()) {
            long temp = set.getLong(1);
            removeTempGame(temp);
            return temp;
        }
        return -1;
    }

    public long getCurrentMoveID() throws SQLException {
        ResultSet set = getMoveGeneratedKeys();
        if (set.next()) {
            long temp = set.getLong(1);
            removeTempMove(temp);
            return temp;
        }
        return -1;
    }

    public void finish() throws SQLException {
        connection.close();
        System.out.println("Connection closed.");
    }

    public void reset() throws SQLException {
        PreparedStatement gameStatement = connection.prepareStatement("UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='Game'");
        PreparedStatement moveStatement = connection.prepareStatement("UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='Move'");
        gameStatement.executeUpdate();
        moveStatement.executeUpdate();
        connection.commit();
    }

    public void executeBatch() throws SQLException {
        try {
            addMoveStatement.executeBatch();
            addGameStatement.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Error with executing batch, rolling back the database.");
            connection.rollback();
        }
    }

}
