package lmp2.oscillate;

import java.util.logging.Level;
import java.util.logging.Logger;

public class App {
    private static Config config;

    public static void main(String[] args) {
        Logger logger = AppLogger.getLogger();

        logger.log(Level.INFO, "app initialized");

        try {
            config = new Config(args);
        } catch (IllegalArgumentException e) {
            logger.log(Level.SEVERE, e.getMessage());
            System.exit(1);
        }
        logger.log(Level.INFO, "loaded config:\n" + config);

        Maze maze = new Maze(10, 10, 0, 2);
        maze.add((byte)1);
        maze.add((byte)2);
        maze.add((byte)3);
        maze.setParentIndexAt(0, 2);
        maze.setAdjacentsAt(0, (byte)3);
        System.out.println(maze.getParentIndexAt(0));
        System.out.println(maze.getCellCount());
        // Parser parser = config.isbin ? new BinaryParser() : new RegularParser();
        // parser.parse(config.infile);
    }
}
