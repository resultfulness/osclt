package lmp2.oscillate;

public class Maze_InputFormat {
    private int fileWidth;
    private int fileHeight;
    private int fileStartIndex;
    private int fileEndIndex;
    private int solutionOffset = -1;
    private byte charMap[]; /* 0 for wall, 1 for path, 2 for path_trace, 3 for path_solution */

    public static final char START = 'P';
    public static final char END = 'K';
    public static final char WALL = 'X';
    public static final char PATH = ' ';
    public static final char PATH_TRACE = 'O';
    public static final char PATH_SOLUTION = 'S';

    Maze_InputFormat() {
    }

    public void initialise(int fileWidth, int fileHeight)
    throws IllegalArgumentException {
        this.setFileWidth(fileWidth);
        this.setFileHeight(fileHeight);
        this.charMap = new byte[fileWidth * fileHeight];
    }

    public void fromMaze(Maze maze) {
        this.initialise(maze.getWidth() * 2 + 1, maze.getHeight() * 2 + 1);
        // Edges
        this.mapCharAt(WALL, 0);
        this.mapCharAt(WALL, getFileWidth() - 1);
        this.mapCharAt(WALL, getFileSize() - 1 - getFileWidth());
        this.mapCharAt(WALL, getFileSize() - 1);

        // Iterate maze for rest
        for(int cellIndex = 0; cellIndex < maze.getSize(); cellIndex++){
            int relativeIndex = getInputIndexFromMazeIndex(cellIndex);
            mapCharAt(PATH, relativeIndex);
            byte adjacents = maze.getAdjacentsAt(cellIndex);
            if(cellIndex < maze.getSize() - 2){
                if((adjacents & Maze.EAST_VALUE) != 0)
                    mapCharAt(PATH, relativeIndex + 1);
                else
                    mapCharAt(WALL, relativeIndex + 1);
            }
            if(cellIndex % maze.getWidth() != 0) {
                if((adjacents & Maze.WEST_VALUE) != 0)
                    mapCharAt(PATH, relativeIndex - 1);
                else
                    mapCharAt(WALL, relativeIndex - 1);
            }
            if(cellIndex > maze.getWidth()) {
                if((adjacents & Maze.NORTH_VALUE) != 0)
                    mapCharAt(PATH, relativeIndex - this.getFileWidth());
                else
                    mapCharAt(WALL, relativeIndex - this.getFileWidth());
            }
            if(cellIndex < maze.getSize() - maze.getWidth() - 1){
                if((adjacents & Maze.SOUTH_VALUE) != 0)
                    mapCharAt(PATH, relativeIndex + this.getFileWidth());
                else
                    mapCharAt(WALL, relativeIndex + this.getFileWidth());
            }
        }
        this.setFileEndIndex(this.getFileSize() - this.getFileWidth() - 1);
        this.setFileStartIndex(this.getFileWidth());
    }

    public int getFileWidth() {
        return this.fileWidth;
    }

    public int getFileHeight() {
        return this.fileHeight;
    }

    public int getFileSize() {
        return this.getFileWidth() * this.getFileHeight();
    }

    public int getMazeWidth() {
        return (this.fileWidth - 1) / 2;
    }

    public int getMazeHeight() {
        return (this.fileHeight - 1) / 2;
    }

    public int getMazeSize() {
        return this.getMazeWidth() * this.getMazeHeight();
    }
    
    public int getSolutionOffset() {
        return this.solutionOffset;
    }
    
    public int getInputIndexFromMazeIndex(int mazeIndex) {
        return ((mazeIndex / getMazeWidth()) * 2 + 1) * fileWidth + (mazeIndex % getMazeWidth() * 2 + 1);
    }

    public void setSolutionOffset(int solutionOffset) {
        this.solutionOffset = solutionOffset;
    }

    public char getCharAt(int index) throws IndexOutOfBoundsException {
        if (index == this.getFileStartIndex()) {
            return Maze_InputFormat.START;
        } else if (index == this.getFileEndIndex()) {
            return Maze_InputFormat.END;
        } else if (this.charMap[index] == 0) {
            return Maze_InputFormat.WALL;
        } else if (this.charMap[index] == 1){
            return Maze_InputFormat.PATH;
        } else if (this.charMap[index] == 2){
            return Maze_InputFormat.PATH_TRACE;
        } else {
            return Maze_InputFormat.PATH_SOLUTION;
        }
    }

    public void mapCharAt(char c, int index)
    throws IllegalArgumentException, IndexOutOfBoundsException {
        switch (c) {
            case Maze_InputFormat.WALL:
                this.charMap[index] = 0;
                break;
            case Maze_InputFormat.PATH:
                this.charMap[index] = 1;
                break;
            case Maze_InputFormat.START:
                this.setFileStartIndex(index);
                break;
            case Maze_InputFormat.END:
                this.setFileEndIndex(index);
                break;
            case Maze_InputFormat.PATH_TRACE:
                this.charMap[index] = 2;
                break;
            case Maze_InputFormat.PATH_SOLUTION:
                this.charMap[index] = 3;
                break;
            default:
                throw new IllegalArgumentException("invalid character");
        }
    }

    private void setFileWidth(int width) throws IllegalArgumentException {
        if (width < 0) {
            throw new IllegalArgumentException("width must be greater than 0");
        }
        this.fileWidth = width;
    }

    private void setFileHeight(int height) throws IllegalArgumentException {
        if (height < 0) {
            throw new IllegalArgumentException("height must be greater than 0");
        }
        this.fileHeight = height;
    }

    public int getFileStartIndex() {
        return this.fileStartIndex;
    }

    public int getFileEndIndex() {
        return this.fileEndIndex;
    }

    private void setFileStartIndex(int startIndex)
    throws IndexOutOfBoundsException {
        if (!isIndexWithinMaze(startIndex)) {
            throw new IndexOutOfBoundsException("start index outside of maze");
        }
        this.fileStartIndex = startIndex;
    }

    private void setFileEndIndex(int endIndex) throws IndexOutOfBoundsException {
        if (!isIndexWithinMaze(endIndex)) {
            throw new IndexOutOfBoundsException("end index outside of maze");
        }
        this.fileEndIndex = endIndex;
    }

    private boolean isIndexWithinMaze(int index) {
        return index >= 0 && index < this.getFileSize();
    }

    public void clearSolution() {
        for(int i = 0; i < getFileSize(); i++) {
            char c = getCharAt(i);
            if(c == Maze_InputFormat.PATH_SOLUTION || c == Maze_InputFormat.PATH_TRACE)
                mapCharAt(Maze_InputFormat.PATH, i);
        }
    }

    public boolean canBePath(int index) {
        return 
            !isIndexExternalWall(index) && (
                index / getFileWidth() % 2 == 1 ||
                index % getFileWidth() % 2 == 1
            );
    }

    public boolean canBeWall(int index) {
        return index / getFileWidth() % 2 == 0 || index % getFileWidth() % 2 == 0;
    }

    public boolean canBeStartEndIndex(int index) {
        return (index / getFileWidth() % 2 == 1 && index % getFileWidth() % 2 == 1) && !(this.getFileStartIndex() == index) && !(this.getFileEndIndex() == index);
    }

    public boolean isIndexExternalWall(int index) {
        int fw = this.getFileWidth();
        int fh = this.getFileHeight();
        return
            index / fw < 1 ||
            index / fw >= fh - 1 ||
            index % fw == 0 ||
            index % fw == fw - 1;
    }

    @Override
    public String toString() {
        return String.format("""
            Maze_InputFormat {
            fileWidth: %d,
            fileHeight: %d,
            startIndex: %d,
            endIndex: %d,
            charMap: boolean[%d],
            solutionOffset: %d,
            }\
            """,
            this.fileWidth,
            this.fileHeight,
            this.fileStartIndex,
            this.fileEndIndex,
            this.charMap.length,
            this.solutionOffset
        );
    }
}
