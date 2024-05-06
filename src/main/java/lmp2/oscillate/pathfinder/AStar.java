package lmp2.oscillate.pathfinder;

import java.util.ArrayList;

import lmp2.oscillate.Maze;
import lmp2.oscillate.Maze.Cell;
import lmp2.oscillate.Maze_InputFormat;
import lmp2.oscillate.ui.AppWindow;
import lmp2.oscillate.ui.LogDialog;

public class AStar extends PathFinder {

    private ArrayList<CellInfo> solveArray;
    private boolean useEuclidianCalc = false;

    private class CellInfo {
        protected int cellIndex;
        protected int distanceFromSource;
        protected Double weight;

        protected CellInfo(int cellIndex, int distanceFromSource, double weight) {
            this.cellIndex = cellIndex;
            this.distanceFromSource = distanceFromSource;
            this.weight = weight;
        }
    }

    public AStar(Maze maze, Maze_InputFormat maze_inputFormat, AppWindow appWindow, boolean useEuclidianCalc) {
        super(maze, maze_inputFormat, appWindow);
        this.useEuclidianCalc = useEuclidianCalc;
    }

    private CellInfo getMin() {
        Double min = Double.MAX_VALUE;
        CellInfo minCell = new CellInfo(-1, -1, -1);
        for(CellInfo cell : solveArray) {
            if(cell.weight + cell.distanceFromSource < min) {
                min = cell.weight + cell.distanceFromSource;
                minCell = cell;
            }
        }
        solveArray.remove(minCell);
        return minCell;
    }

    @Override
    public void run() throws IllegalStateException {
        this.solveArray = new ArrayList<CellInfo>();
        this.isRunning = true;
        this.solveArray.add(new CellInfo(this.maze.getStartIndex(), 0, 0));
        Cell currentCell;
        do{
            if(!isRunning)
                break;
            CellInfo currentCellInfo = getMin();
            currentCell = this.maze.get(currentCellInfo.cellIndex);
            currentCell.setVisited(true);
            this.maze_inputFormat.mapCharAt(Maze_InputFormat.PATH_TRACE, this.maze_inputFormat.getInputIndexFromMazeIndex(currentCellInfo.cellIndex));
            if(processAdjacent(currentCellInfo, Maze.NORTH_VALUE))
                break;
            if(processAdjacent(currentCellInfo, Maze.EAST_VALUE))
                break;
            if(processAdjacent(currentCellInfo, Maze.SOUTH_VALUE))
                break;
            if(processAdjacent(currentCellInfo, Maze.WEST_VALUE))
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
        } while(!this.solveArray.isEmpty());
        appWindow.getMazeContainer().repaint();
        if(this.solveArray.isEmpty())
            throw new IllegalStateException("Couldn't find path between given start and end cells.");
        if (this.isRunning)
            this.showSolution();
    }
    
    // Returns true if processed cell is end cell
    private boolean processAdjacent(CellInfo cellInfo, byte direction) {
        Cell adjacentCell;
        byte adjacents = this.maze.getAdjacentsAt(cellInfo.cellIndex);
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
                adjacentCell = this.maze.get(cellInfo.cellIndex + offset);
            }
            catch(IndexOutOfBoundsException e){
                return false;
            }
            if(!adjacentCell.isVisited()){
                
                this.solveArray.add(new CellInfo(cellInfo.cellIndex + offset, cellInfo.distanceFromSource + 1, 
                    this.useEuclidianCalc ? calculateEuclidian(cellInfo.cellIndex) : calculateManhattan(cellInfo.cellIndex)));
                    this.maze.setParentIndexAt(cellInfo.cellIndex + offset, cellInfo.cellIndex);
                int offsetBetween = (this.maze_inputFormat.getInputIndexFromMazeIndex(cellInfo.cellIndex + offset) - this.maze_inputFormat.getInputIndexFromMazeIndex(cellInfo.cellIndex)) / 2;
                this.maze_inputFormat.mapCharAt(Maze_InputFormat.PATH_TRACE, this.maze_inputFormat.getInputIndexFromMazeIndex(cellInfo.cellIndex + offset));
                this.maze_inputFormat.mapCharAt(Maze_InputFormat.PATH_TRACE, this.maze_inputFormat.getInputIndexFromMazeIndex(cellInfo.cellIndex) + offsetBetween);
                if(cellInfo.cellIndex + offset == this.maze.getEndIndex())
                    return true;
            }
        }
        return false;
    }
    private Double calculateEuclidian(int cellIndex) {
        Integer cellX = cellIndex % this.maze.getWidth();
        Integer cellY = cellIndex / this.maze.getWidth();
        Integer targetX = this.maze.getEndIndex() % this.maze.getWidth();
        Integer targetY = this.maze.getEndIndex() / this.maze.getWidth(); 
        return Math.sqrt(Math.pow(cellX - targetX, 2) + Math.pow(cellY - targetY, 2));
    }

    private Integer calculateManhattan(int cellIndex) {
        Integer cellX = cellIndex % this.maze.getWidth();
        Integer cellY = cellIndex / this.maze.getWidth();
        Integer targetX = this.maze.getEndIndex() % this.maze.getWidth();
        Integer targetY = this.maze.getEndIndex() / this.maze.getWidth(); 
        return Math.abs(cellX - targetX) + Math.abs(cellY - targetY);
    }
}
