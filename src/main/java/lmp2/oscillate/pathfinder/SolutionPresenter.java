package lmp2.oscillate.pathfinder;

import lmp2.oscillate.Maze;
import lmp2.oscillate.Maze_InputFormat;
import lmp2.oscillate.ui.AppWindow2;

public class SolutionPresenter {
    public SolutionPresenter() {

    }

    public void showSolution(Maze maze, Maze_InputFormat maze_inputFormat, AppWindow2 appWindow) {
        int currentIndex = maze.getEndIndex(), previousIndex = 0;
        while(currentIndex != maze.getStartIndex()) {
            previousIndex = currentIndex;
            currentIndex = maze.getParentIndexAt(previousIndex);
            int offsetBetween = (maze_inputFormat.getInputIndexFromMazeIndex(currentIndex) - maze_inputFormat.getInputIndexFromMazeIndex(previousIndex)) / 2;
            maze_inputFormat.mapCharAt(Maze_InputFormat.PATH_SOLUTION, maze_inputFormat.getInputIndexFromMazeIndex(previousIndex));
            try {
                Thread.sleep(50);
                appWindow.getAppContainer().repaint();
                maze_inputFormat.mapCharAt(Maze_InputFormat.PATH_SOLUTION, maze_inputFormat.getInputIndexFromMazeIndex(previousIndex) + offsetBetween);
                Thread.sleep(50);
                appWindow.getAppContainer().repaint();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        maze_inputFormat.mapCharAt(Maze_InputFormat.PATH_SOLUTION, maze_inputFormat.getInputIndexFromMazeIndex(currentIndex));
        appWindow.getAppContainer().repaint();
    }
}
