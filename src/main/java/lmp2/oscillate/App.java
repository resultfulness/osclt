package lmp2.oscillate;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import lmp2.oscillate.parser.BinaryMazeParser;
import lmp2.oscillate.parser.MazeParser;
import lmp2.oscillate.parser.RegularMazeParser;
import lmp2.oscillate.ui.AppWindow;

public class App {
    public static Config config;

    public static void main(String[] args) {
        Logger logger = AppLogger.getLogger();
        try {
            App.config = new Config(args);
        } catch (IllegalArgumentException e) {
            logger.log(Level.SEVERE, e.getMessage());
            System.exit(1);
        }
        logger.log(Level.INFO, "app initialized");
        logger.log(Level.INFO, "loaded config:\n" + config);

        MazeParser mazeParser = App.config.getIsInputFileBinary()
            ? new BinaryMazeParser()
            : new RegularMazeParser();
        logger.log(
            Level.INFO,
            "initialized parser: file being parsed: " +
            App.config.getInputFilePath()
        );

        Maze_InputFormat maze_InputFormat = new Maze_InputFormat();
        try {
            mazeParser.parseInto(
                maze_InputFormat,
                App.config.getInputFilePath()
            );
        } catch (IOException | IllegalStateException e) {
            logger.log(Level.SEVERE, e.getMessage());
            System.exit(1);
        }
        logger.log(
            Level.INFO,
            "finished parsing input file into input format:\n" +
            maze_InputFormat
        );

        logger.log(Level.INFO, "read maze:");
        final int truncateAfter = 16;
        for (int i = 0; i < maze_InputFormat.getFileSize(); i++) {
            if (i > maze_InputFormat.getFileWidth() * truncateAfter) {
                System.out.println(
                    String.format(
                        "\n+%s more lines...",
                        maze_InputFormat.getFileHeight() - truncateAfter
                    )
                );
                break;
            }
            System.out.print(maze_InputFormat.getCharAt(i));
            if (i % maze_InputFormat.getFileWidth() ==
                maze_InputFormat.getFileWidth() - 1
            ) {
                System.out.println();
            }
        }

        // show here
        SwingUtilities.invokeLater(() -> {
            AppWindow appWindow = new AppWindow();
            appWindow.displayMaze(maze_InputFormat);
        });

        Maze maze = null;
        try {
            maze = Maze.fromInputFormat(maze_InputFormat);
        } catch (IllegalStateException | IndexOutOfBoundsException e) {
            logger.log(Level.SEVERE, e.getMessage());
            System.exit(1);
        }
        logger.log(
            Level.INFO,
            "transformed input format maze into maze:\n" + maze
        );

        // solve and show solution here
    }
}
