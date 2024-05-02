package lmp2.oscillate.pathfinder;

import java.util.LinkedList;
import java.util.Queue;

import lmp2.oscillate.Maze;
import lmp2.oscillate.Maze_InputFormat;
import lmp2.oscillate.Maze.Cell;
import lmp2.oscillate.ui.AppWindow2;

public final class BFS extends PathFinder {
    private Queue<Integer> solveQueue;
    private Maze maze;
    private Maze_InputFormat maze_inputFormat;

    public BFS() {

    }

    @Override
    public void solveMaze(Maze maze, Maze_InputFormat maze_inputFormat, AppWindow2 appWindow) throws IllegalStateException {
        this.maze = maze;
        this.maze_inputFormat = maze_inputFormat;
        this.solveQueue = new LinkedList<Integer>();
        this.solveQueue.add(this.maze.getStartIndex());
        Cell currentCell;
        do{
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
            appWindow.getAppContainer().repaint();
            try{
                Thread.sleep(1000/Math.max(maze.getWidth(), maze.getHeight()));
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        } while(!this.solveQueue.isEmpty());
        appWindow.getAppContainer().repaint();
        if(this.solveQueue.isEmpty())
            throw new IllegalStateException("Couldn't find path between given start and end cells\n");
    }
    
    // Returns true if processed cell is end cell
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
                solveQueue.add(cellIndex + offset);
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
