package lmp2.oscillate.parser;

import java.io.IOException;

import lmp2.oscillate.Maze_InputFormat;

public abstract class MazeParser {
    public abstract void parseInto(
        Maze_InputFormat maze_InputFormat, String inputFilePath
    ) throws IOException, IllegalStateException;
}
