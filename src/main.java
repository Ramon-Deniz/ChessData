import game.Game;
import parse.Parser;

public class main {
    public static void main(String[] args) throws Exception {
        Parser parser = new Parser("lichess_db_standard_rated_2013-01.pgn");
        Game game = parser.nextGame();

        System.out.println(game.getTags());
    }
}
