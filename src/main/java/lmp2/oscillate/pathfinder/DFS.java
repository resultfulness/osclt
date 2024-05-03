package lmp2.oscillate.pathfinder;

import java.util.Stack;

import lmp2.oscillate.Maze;
import lmp2.oscillate.Maze.Cell;
import lmp2.oscillate.Maze_InputFormat;
import lmp2.oscillate.ui.AppWindow;

public final class DFS extends PathFinder {

    private Stack<Integer> solveStack;

    public DFS(Maze maze, Maze_InputFormat maze_inputFormat, AppWindow appWindow) {
        super(maze, maze_inputFormat, appWindow);
    }
    
    @Override
    public void run() throws IllegalStateException {
        this.solveStack = new Stack<Integer>();
        this.solveStack.push(maze.getStartIndex());
        this.isRunning = true;
        do{
            if(!isRunning)
                break;
            Integer currentIndex = this.solveStack.pop();
            Cell currentCell = this.maze.get(currentIndex);
            currentCell.setVisited(true);
            this.maze_inputFormat.mapCharAt(Maze_InputFormat.PATH_TRACE, this.maze_inputFormat.getInputIndexFromMazeIndex(currentIndex));
            if(processAdjacent(currentIndex, Maze.NORTH_VALUE))
                break;
            if(processAdjacent(currentIndex, Maze.EAST_VALUE))
                break;
            if(processAdjacent(currentIndex, Maze.SOUTH_VALUE))
                break;
            if(processAdjacent(currentIndex, Maze.WEST_VALUE))
                break;
            appWindow.getMazeContainer().repaint();
            try{
                sleep(1000/Math.max(maze.getWidth(), maze.getHeight()));
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                this.isRunning = false;
            }
        } while(!this.solveStack.isEmpty());
        appWindow.getMazeContainer().repaint();
        if(this.solveStack.isEmpty())
            throw new IllegalStateException("Couldn't find path between given start and end cells\n");
        this.showSolution();
    }
    
    private boolean processAdjacent(int cellIndex, byte direction) {
        Cell adjacentCell;
        byte adjacents = maze.getAdjacentsAt(cellIndex);
        int offset = 0;
        switch(direction){
            case Maze.NORTH_VALUE:
                offset = -maze.getWidth();
                break;
            case Maze.EAST_VALUE:
                offset = 1;
                break;
            case Maze.SOUTH_VALUE:
                offset = maze.getWidth();
                break;
            case Maze.WEST_VALUE:
                offset = -1;
                break;
        }
        if((adjacents & direction) == direction) {
            try{
                adjacentCell = maze.get(cellIndex + offset);
            }
            catch(IndexOutOfBoundsException e){
                return false;
            }
            if(!adjacentCell.isVisited()){
                solveStack.add(cellIndex + offset);
                maze.setParentIndexAt(cellIndex + offset, cellIndex);
                int offsetBetween = (this.maze_inputFormat.getInputIndexFromMazeIndex(cellIndex + offset) - this.maze_inputFormat.getInputIndexFromMazeIndex(cellIndex)) / 2;
                this.maze_inputFormat.mapCharAt(Maze_InputFormat.PATH_TRACE, this.maze_inputFormat.getInputIndexFromMazeIndex(cellIndex + offset));
                this.maze_inputFormat.mapCharAt(Maze_InputFormat.PATH_TRACE, this.maze_inputFormat.getInputIndexFromMazeIndex(cellIndex) + offsetBetween);
                if(cellIndex + offset == maze.getEndIndex())
                    return true;
            }
        }
        return false;
    }
}
