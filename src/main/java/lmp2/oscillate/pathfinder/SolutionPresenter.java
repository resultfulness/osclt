package lmp2.oscillate.pathfinder;

import lmp2.oscillate.Maze;
import lmp2.oscillate.Maze_InputFormat;
import lmp2.oscillate.ui.AppWindow;

public class SolutionPresenter extends Thread {
    private Maze maze;
    private Maze_InputFormat maze_inputFormat;
    private AppWindow appWindow;
    private boolean isRunning;

    public SolutionPresenter() {

    }

    public void showSolution(Maze maze, Maze_InputFormat maze_inputFormat, AppWindow appWindow) {
        this.maze = maze;
        this.maze_inputFormat = maze_inputFormat;
        this.appWindow = appWindow;

        this.start();
    }

    @Override
    public void run() {
        int currentIndex = maze.getEndIndex(), previousIndex = 0;
        this.isRunning = true;
        while(currentIndex != maze.getStartIndex()) {
            if(!isRunning)
                break;
            previousIndex = currentIndex;
            currentIndex = maze.getParentIndexAt(previousIndex);
            int offsetBetween = (maze_inputFormat.getInputIndexFromMazeIndex(currentIndex) - maze_inputFormat.getInputIndexFromMazeIndex(previousIndex)) / 2;
            maze_inputFormat.mapCharAt(Maze_InputFormat.PATH_SOLUTION, maze_inputFormat.getInputIndexFromMazeIndex(previousIndex));
            try {
                Thread.sleep(1000/Math.max(maze.getWidth(), maze.getHeight()));
                appWindow.getMazeContainer().repaint();
                Thread.sleep(1000/Math.max(maze.getWidth(), maze.getHeight()));
                maze_inputFormat.mapCharAt(Maze_InputFormat.PATH_SOLUTION, maze_inputFormat.getInputIndexFromMazeIndex(previousIndex) + offsetBetween);
                appWindow.getMazeContainer().repaint();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                this.isRunning = false;
            }
        }
        maze_inputFormat.mapCharAt(Maze_InputFormat.PATH_SOLUTION, maze_inputFormat.getInputIndexFromMazeIndex(currentIndex));
        appWindow.getMazeContainer().repaint();
    }
}
