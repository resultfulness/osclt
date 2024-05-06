package lmp2.oscillate.pathfinder;

import java.util.LinkedList;
import java.util.Queue;

import lmp2.oscillate.Maze;
import lmp2.oscillate.Maze_InputFormat;
import lmp2.oscillate.Maze.Cell;
import lmp2.oscillate.ui.AppWindow;
import lmp2.oscillate.ui.LogDialog;

public final class BFS extends PathFinder {

    private Queue<Integer> solveQueue;

    public BFS(Maze maze, Maze_InputFormat maze_inputFormat, AppWindow appWindow) {
        super(maze, maze_inputFormat, appWindow);
    }

    @Override
    public void run() throws IllegalStateException {
        this.solveQueue = new LinkedList<Integer>();
        this.isRunning = true;
        this.solveQueue.add(this.maze.getStartIndex());
        Cell currentCell;
        do{
            if(!isRunning)
                break;
            Integer currentIndex = this.solveQueue.remove();
            currentCell = this.maze.get(currentIndex);
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
                if(1000/Math.max(maze.getWidth(), maze.getHeight()) > 0)
                    sleep(1000/Math.max(maze.getWidth(), maze.getHeight()));
                else
                    sleep(1);
            } catch (InterruptedException ex) {
                LogDialog.show("Solution interrupted.", LogDialog.Level.WARN);
                this.isRunning = false;
            }
        } while(!this.solveQueue.isEmpty());
        appWindow.getMazeContainer().repaint();
        if(this.solveQueue.isEmpty())
            throw new IllegalStateException("Couldn't find path between given start and end cells\n");
        if (this.isRunning)
            this.showSolution();
    }
    
    // Returns true if processed cell is end cell
    private boolean processAdjacent(int cellIndex, byte direction) {
        Cell adjacentCell;
        byte adjacents = this.maze.getAdjacentsAt(cellIndex);
        int offset = 0;
        switch(direction){
            case Maze.NORTH_VALUE:
                offset = -this.maze.getWidth();
                break;
            case Maze.EAST_VALUE:
                offset = 1;
                break;
            case Maze.SOUTH_VALUE:
                offset = this.maze.getWidth();
                break;
            case Maze.WEST_VALUE:
                offset = -1;
                break;
        }
        if((adjacents & direction) == direction) {
            try{
                adjacentCell = this.maze.get(cellIndex + offset);
            }
            catch(IndexOutOfBoundsException e){
                return false;
            }
            if(!adjacentCell.isVisited()){
                this.solveQueue.add(cellIndex + offset);
                this.maze.setParentIndexAt(cellIndex + offset, cellIndex);
                int offsetBetween = (this.maze_inputFormat.getInputIndexFromMazeIndex(cellIndex + offset) - this.maze_inputFormat.getInputIndexFromMazeIndex(cellIndex)) / 2;
                this.maze_inputFormat.mapCharAt(Maze_InputFormat.PATH_TRACE, this.maze_inputFormat.getInputIndexFromMazeIndex(cellIndex + offset));
                this.maze_inputFormat.mapCharAt(Maze_InputFormat.PATH_TRACE, this.maze_inputFormat.getInputIndexFromMazeIndex(cellIndex) + offsetBetween);
                if(cellIndex + offset == this.maze.getEndIndex())
                    return true;
            }
        }
        return false;
    }
}
