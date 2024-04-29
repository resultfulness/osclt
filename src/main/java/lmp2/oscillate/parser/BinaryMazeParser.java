package lmp2.oscillate.parser;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import lmp2.oscillate.Maze_InputFormat;

public class BinaryMazeParser extends MazeParser {
    private DataInputStream instream;
    private boolean areResourcesFreed = false;

    public BinaryMazeParser(String inputFilePath)
    throws FileNotFoundException {
        instream = new DataInputStream(new FileInputStream(inputFilePath));
    }

    @Override
    public void parseInto(Maze_InputFormat maze_InputFormat)
    throws IOException, IllegalStateException {
        if (areResourcesFreed) {
            throw new IllegalStateException("can't access input file: input file has been closed");
        }
    }

    public void freeResources() throws IOException {
        instream.close();
        areResourcesFreed = true;
    }
}
