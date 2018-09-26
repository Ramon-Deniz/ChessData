package game;

public class Coordinate {

    private char file;
    private char rank;

    public Coordinate(char file, char rank) {
        this.file = file;
        this.rank = rank;
    }

    /**
     * Returns the distance between two coordinates
     *
     * @param other
     * @return
     */
    public int getDistance(Coordinate other) {
        int tempFile = Math.abs(getFile() - other.getFile());
        int tempRank = Math.abs(getRank() - other.getRank());
        if (tempFile > tempRank) {
            return tempFile;
        }
        return tempRank;
    }

    public char getFile() {
        return file;
    }

    public char getRank() {
        return rank;
    }

    public int getFileAsIndex() {
        return file - 97;
    }

    public int getRankAsIndex() {
        return rank - 49;
    }

    public String toString() {
        return "" + file + rank;
    }

    @Override
    public boolean equals(Object other) {

        if (other instanceof Coordinate) {
            Coordinate temp = (Coordinate) other;
            return file == temp.file && rank == temp.rank;
        }
        return false;
    }

    public boolean isSameFile(Coordinate other) {
        return file == other.file;
    }

    public boolean isSameRank(Coordinate other) {
        return rank == other.rank;
    }

    public boolean isMoveDiagonal(Coordinate other) {
        int differenceInFiles = Math.abs(file - other.file);
        int differenceInRanks = Math.abs(rank - other.rank);
        return differenceInFiles == differenceInRanks;
    }
}
