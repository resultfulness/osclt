package lmp2.oscillate.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import lmp2.oscillate.Maze_InputFormat;

public class RegularMazeParser extends MazeParser {
    private BufferedReader reader;
    private int fileWidth;
    private int fileLength;
    private boolean areResourcesFreed = false;

    public RegularMazeParser(String inputFilePath) throws IOException {
        reader = new BufferedReader(new FileReader(inputFilePath));

        BufferedReader fileCharCounter = new BufferedReader(
            new FileReader(inputFilePath)
        );
        this.fileWidth = fileCharCounter.readLine().length();
        fileLength++;
        while (fileCharCounter.readLine() != null) {
            fileLength++;
        }
        fileCharCounter.close();
    }

    @Override
    public void parseInto(Maze_InputFormat maze_InputFormat)
    throws IOException, IllegalStateException {
        if (areResourcesFreed) {
            throw new IllegalStateException(
                "can't access input file: input file has been closed"
            );
        }

        maze_InputFormat.initialise(this.fileWidth, this.fileLength);

        int i = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            for (int j = 0; j < line.length(); j++) {
                char c = line.charAt(j);
                try {
                    maze_InputFormat.mapCharAt(c, i * this.fileWidth + j);
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

    public void freeResources() throws IOException {
        reader.close();
        areResourcesFreed = true;
    }
}
