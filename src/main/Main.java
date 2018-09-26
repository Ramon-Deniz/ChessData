package main;

import sqlite.GameImporter;

public class Main {
    public static void main(String[] args) throws Exception {
        String file = "/home/ramon/Documents/lichess_db_standard_rated_2017-01.pgn";
        long start = System.nanoTime();

        GameImporter importer = new GameImporter(file);

        importer.reset();

        importer.addData();

        importer.finish();

        System.out.println(System.nanoTime()-start);
    }
}
