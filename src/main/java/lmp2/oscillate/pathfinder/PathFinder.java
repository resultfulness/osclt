package lmp2.oscillate.pathfinder;

import lmp2.oscillate.Maze;
import lmp2.oscillate.Maze_InputFormat;
import lmp2.oscillate.ui.AppWindow2;

public abstract class PathFinder{
    public abstract void solveMaze(Maze maze, Maze_InputFormat maze_inputFormat, AppWindow2 appWindow)
    throws IllegalStateException;
}