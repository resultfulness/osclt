package lmp2.oscillate.generator;

import java.util.ArrayList;
import java.util.Random;

import lmp2.oscillate.Maze;

public class MazeGenerator {
    private Maze maze;
    private int width;
    private int height;
    private boolean visited[];
    
    public MazeGenerator(int width, int height) throws IllegalArgumentException {
        if(width > 1024 || height > 1024 || height < 3 || width < 3)
            throw new IllegalArgumentException("Width and height need to be between 3 and 1024 inclusive");
        this.width = width;
        this.height = height;
        this.visited = new boolean[this.width * this.height];
    }

    private void recursiveDFS(int currentIndex) {
        Random rnd = new Random();
        this.visited[currentIndex] = true;
        byte currentAdjacents = this.maze.getAdjacentsAt(currentIndex);
        ArrayList<Byte> directions = new ArrayList<>(){
            {
                add(Maze.EAST_VALUE);
                add(Maze.NORTH_VALUE);
                add(Maze.SOUTH_VALUE);
                add(Maze.WEST_VALUE);
            }
        };
        while(directions.size() > 0) {
            int randomIndex = Math.abs(rnd.nextInt()) % directions.size();
            switch(directions.get(randomIndex)) {
                case Maze.EAST_VALUE:
                    if(currentIndex % this.maze.getWidth() != this.maze.getWidth() - 1 && !this.visited[currentIndex + 1]) {
                        currentAdjacents += Maze.EAST_VALUE;
                        int nextIndex = processAdjacent(currentIndex, Maze.EAST_VALUE);
                        this.recursiveDFS(nextIndex);
                    }
                    break;
                case Maze.WEST_VALUE:
                    if(currentIndex % this.maze.getWidth() != 0 && !this.visited[currentIndex - 1]) {
                        currentAdjacents += Maze.WEST_VALUE;
                        int nextIndex = processAdjacent(currentIndex, Maze.WEST_VALUE);
                        this.recursiveDFS(nextIndex);
                    }
                    break;
                case Maze.NORTH_VALUE:
                    if(currentIndex / this.maze.getWidth() != 0 && !this.visited[currentIndex - this.maze.getWidth()]) {
                        currentAdjacents += Maze.NORTH_VALUE;
                        int nextIndex = processAdjacent(currentIndex, Maze.NORTH_VALUE);
                        this.recursiveDFS(nextIndex);
                    }
                    break;
                case Maze.SOUTH_VALUE:
                    if(currentIndex / this.maze.getWidth() < this.maze.getHeight() - 1 && !this.visited[currentIndex + this.maze.getWidth()]) {
                        currentAdjacents += Maze.SOUTH_VALUE;
                        int nextIndex = processAdjacent(currentIndex, Maze.SOUTH_VALUE);
                        this.recursiveDFS(nextIndex);
                    }
                    break;
            }
            directions.remove(randomIndex);
        }
        this.maze.setAdjacentsAt(currentIndex, currentAdjacents);
    }

    public Maze generateMaze(int wallRemovalPercentage) throws IllegalArgumentException{
        if(wallRemovalPercentage < 0 || wallRemovalPercentage > 20)
            throw new IllegalArgumentException("Wall removal percentage can't be less than 0 and can not exceed 20");
        this.maze = new Maze(this.width, this.height, 0);
        this.maze.initializeEmptyCells();
        this.recursiveDFS(0);
        this.maze.setStartIndex(0);
        this.maze.setEndIndex(this.maze.getSize()-1);
        removeWalls(wallRemovalPercentage);
        return this.maze;
    }

    private void removeWalls(int wallRemovalPercentage){
        System.out.println("Walls percentage: " + wallRemovalPercentage);
        if(wallRemovalPercentage == 0)
            return; 
        int wallsRemoved = 0;
        Random rnd = new Random();
        System.out.println("Walls removed: " + wallsRemoved + ", expected: " + this.maze.getSize() * ((double)wallRemovalPercentage / 100));
        while(wallsRemoved < this.maze.getSize() * ((double)wallRemovalPercentage / 100)) {
            System.out.println("Walls removed: " + wallsRemoved + ", expected: " + this.maze.getSize() * ((double)wallRemovalPercentage / 100));
            ArrayList<Byte> directions = new ArrayList<>()
            {
                {
                    add(Maze.EAST_VALUE);
                    add(Maze.NORTH_VALUE);
                    add(Maze.SOUTH_VALUE);
                    add(Maze.WEST_VALUE);
                }
            };
            int randomCellIndex = rnd.nextInt(this.maze.getSize() - 1);
            while(directions.size() > 0) {
                int randomDirectionIndex = Math.abs(rnd.nextInt()) % directions.size();
                byte randomDirection = directions.get(randomDirectionIndex);
                byte adjacents = this.maze.getAdjacentsAt(randomCellIndex);
                if((adjacents & randomDirection) == 0){
                    System.out.println("Removing " + randomCellIndex + " at " + directions.get(randomDirectionIndex));
                    wallsRemoved += removeWall(randomCellIndex, randomDirection);
                    break;
                }
                directions.remove(randomDirectionIndex);
            }
        }
    }

    private int removeWall(int cellIndex, byte direction) {
        byte currentAdjacents;

        switch(direction) {
            case Maze.EAST_VALUE:
                if(cellIndex % this.maze.getWidth() == this.maze.getWidth() - 1)
                    return 0;
                currentAdjacents = this.maze.getAdjacentsAt(cellIndex + 1);
                currentAdjacents += Maze.WEST_VALUE;
                this.maze.setAdjacentsAt(cellIndex + 1, currentAdjacents);
                break;
            case Maze.NORTH_VALUE:
                if(cellIndex / this.maze.getWidth() == 0)
                    return 0;
                currentAdjacents = this.maze.getAdjacentsAt(cellIndex - this.maze.getWidth());
                currentAdjacents += Maze.SOUTH_VALUE;
                this.maze.setAdjacentsAt(cellIndex - this.maze.getWidth(), currentAdjacents);
                break;
            case Maze.WEST_VALUE:
                if(cellIndex % this.maze.getWidth() == 0)
                    return 0;
                currentAdjacents = this.maze.getAdjacentsAt(cellIndex - 1);
                currentAdjacents += Maze.EAST_VALUE;
                this.maze.setAdjacentsAt(cellIndex - 1, currentAdjacents);
                break;
            case Maze.SOUTH_VALUE:
                if(cellIndex / this.maze.getWidth() == this.maze.getHeight() - 1)
                    return 0;
                currentAdjacents = this.maze.getAdjacentsAt(cellIndex + this.maze.getWidth());
                currentAdjacents += Maze.NORTH_VALUE;
                this.maze.setAdjacentsAt(cellIndex + this.maze.getWidth(), currentAdjacents);
                break;
        }
        currentAdjacents = this.maze.getAdjacentsAt(cellIndex);
        currentAdjacents += direction;
        this.maze.setAdjacentsAt(cellIndex, currentAdjacents);
        return 1;
    }

    private int processAdjacent(int cellIndex, byte direction) {
        
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

        byte currentAdjacents = this.maze.getAdjacentsAt(cellIndex + offset);
        switch(direction) {
            case Maze.NORTH_VALUE:
                currentAdjacents += Maze.SOUTH_VALUE;
                break;
            case Maze.SOUTH_VALUE:
                currentAdjacents += Maze.NORTH_VALUE;
                break;
            case Maze.EAST_VALUE:
                currentAdjacents += Maze.WEST_VALUE;
                break;
            case Maze.WEST_VALUE:
                currentAdjacents += Maze.EAST_VALUE;
                break;
        }
        this.maze.setAdjacentsAt(cellIndex + offset, currentAdjacents);
        return cellIndex + offset;
    }
}
