package lmp2.oscillate.pathfinder;

import lmp2.oscillate.Maze;
import lmp2.oscillate.Maze_InputFormat;
import lmp2.oscillate.ui.AppWindow;

public abstract class PathFinder extends Thread {
    protected Maze maze;
    protected Maze_InputFormat maze_inputFormat;
    protected AppWindow appWindow;
    protected boolean isRunning;

    public PathFinder(Maze maze, Maze_InputFormat maze_inputFormat, AppWindow appWindow) {
        this.maze = maze;
        this.maze_inputFormat = maze_inputFormat;
        this.appWindow = appWindow;
    }

    protected final void showSolution() {
        SolutionPresenter solutionPresenter = new SolutionPresenter();
        solutionPresenter.showSolution(this.maze, this.maze_inputFormat, this.appWindow);
    }
}