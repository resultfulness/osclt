package lmp2.oscillate.pathfinder;

import lmp2.oscillate.Maze;
import lmp2.oscillate.Maze_InputFormat;
import lmp2.oscillate.ui.AppWindow;

public abstract class PathFinder{
    public abstract void solveMaze(Maze maze, Maze_InputFormat maze_inputFormat, AppWindow appWindow)
    throws IllegalStateException;
}