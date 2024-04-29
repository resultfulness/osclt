package lmp2.oscillate.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import lmp2.oscillate.Maze_InputFormat;

public class RegularMazeParser extends MazeParser {
    public RegularMazeParser() {
    }

    @Override
    public void parseInto(
        Maze_InputFormat maze_InputFormat,
        String inputFilePath
    ) throws IOException, IllegalStateException {
        int fileWidth, fileLength;

        try (
            BufferedReader reader = new BufferedReader(
                new FileReader(inputFilePath)
            )
        ) {
            fileWidth = reader.readLine().length();
            fileLength = 1;
            while (reader.readLine() != null) {
                fileLength++;
            }
            try {
                maze_InputFormat.initialise(fileWidth, fileLength);
            } catch (IllegalArgumentException e) {
                throw new IllegalStateException(
                    "error initializing input format maze: " + e.getMessage()
                );
            }
        }

        try (
            BufferedReader reader = new BufferedReader(
                new FileReader(inputFilePath)
            )
        ) {
            int i = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                for (int j = 0; j < line.length(); j++) {
                    char c = line.charAt(j);
                    try {
                        maze_InputFormat.mapCharAt(c, i * fileWidth + j);
                    } catch (IllegalArgumentException e) {
                        throw new IllegalStateException(
                            "input file invalid: " + e.getMessage()
                        );
                    } catch (IndexOutOfBoundsException e) {
                        throw new IllegalStateException(
                            "index out of bounds while reading input file: " +
                            e.getMessage()
                        );
                    }
                }
                i++;
            }
        }
    }
}
