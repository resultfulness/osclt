package lmp2.oscillate;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import lmp2.oscillate.parser.MazeParser;
import lmp2.oscillate.parser.BinaryMazeParser;
import lmp2.oscillate.parser.RegularMazeParser;

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

        MazeParser mazeParser = null;
        try {
            if (config.getIsInputFileBinary()) {
                mazeParser = new BinaryMazeParser(config.getInputFilePath());
            } else {
                mazeParser = new RegularMazeParser(config.getInputFilePath());
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
            System.exit(1);
        }
        logger.log(
            Level.INFO,
            "initialized parser of file " + config.getInputFilePath()
        );

        Maze_InputFormat maze_InputFormat = new Maze_InputFormat();
        try {
            mazeParser.parseInto(maze_InputFormat);
        } catch (IOException | IllegalStateException e) {
            logger.log(Level.SEVERE, e.getMessage());
            System.exit(1);
        }
        logger.log(
            Level.INFO,
            "finished parsing input file into input format:\n" +
            maze_InputFormat
        );

        // show here

        // Maze maze = new Maze();
        // mazeParser.parseInto(maze);

        try {
            mazeParser.freeResources();
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
            System.exit(1);
        }
        // solve and show solution here
    }
}
