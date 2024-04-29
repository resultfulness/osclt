package lmp2.oscillate.parser;

import java.io.IOException;

import lmp2.oscillate.Maze;
import lmp2.oscillate.Maze_InputFormat;

public abstract class MazeParser {
    public abstract void parseInto(Maze_InputFormat maze_InputFormat)
    throws IOException, IllegalStateException;

    public abstract void freeResources() throws IOException;

    public void parseInto(Maze maze) {
    }
}
