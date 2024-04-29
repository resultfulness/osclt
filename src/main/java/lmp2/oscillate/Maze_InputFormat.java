package lmp2.oscillate;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Maze_InputFormat {
    private int fileWidth;
    private int fileHeight;
    private int startIndex;
    private int endIndex;
    private int solutionOffset = -1;
    private boolean charMap[]; /* 'false' for wall, 'true' for path */

    public static final char START = 'P';
    public static final char END = 'K';
    public static final char WALL = 'X';
    public static final char PATH = ' ';

    Maze_InputFormat() {
    }

    public void initialise(int fileWidth, int fileHeight)
    throws IllegalArgumentException {
        this.setFileWidth(fileWidth);
        this.setFileHeight(fileHeight);
        this.charMap = new boolean[fileWidth * fileHeight];
    }

    public int getFileWidth() {
        return this.fileWidth;
    }

    public int getFileHeight() {
        return this.fileHeight;
    }

    public int getMazeWidth() {
        return (this.fileWidth - 1) / 2;
    }

    public int getMazeHeight() {
        return (this.fileHeight - 1) / 2;
    }
    
    public int getSolutionOffset() {
        return this.solutionOffset;
    }

    public void setSolutionOffset(int solutionOffset) {
        this.solutionOffset = solutionOffset;
    }

    public char getCharAt(int index) throws IndexOutOfBoundsException {
        if (index == this.getStartIndex()) {
            return Maze_InputFormat.START;
        } else if (index == this.getEndIndex()) {
            return Maze_InputFormat.END;
        } else if (this.charMap[index] == false) {
            return Maze_InputFormat.WALL;
        } else {
            return Maze_InputFormat.PATH;
        }
    }

    public void mapCharAt(char c, int index)
    throws IllegalArgumentException, IndexOutOfBoundsException {
        switch (c) {
            case Maze_InputFormat.WALL:
                this.charMap[index] = false;
                break;
            case Maze_InputFormat.PATH:
                this.charMap[index] = true;
                break;
            case Maze_InputFormat.START:
                this.setStartIndex(index);
                break;
            case Maze_InputFormat.END:
                this.setEndIndex(index);
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

    public int getStartIndex() {
        return this.startIndex;
    }

    public int getEndIndex() {
        return this.endIndex;
    }

    private void setStartIndex(int startIndex)
    throws IndexOutOfBoundsException {
        if (!isIndexWithinMaze(startIndex)) {
            throw new IndexOutOfBoundsException("start index outside of maze");
        }
        this.startIndex = startIndex;
    }

    private void setEndIndex(int endIndex) throws IndexOutOfBoundsException {
        if (!isIndexWithinMaze(endIndex)) {
            throw new IndexOutOfBoundsException("end index outside of maze");
        }
        this.endIndex = endIndex;
    }

    private boolean isIndexWithinMaze(int index) {
        return index >= 0 && index < this.fileWidth * this.fileHeight;
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
            this.startIndex,
            this.endIndex,
            this.charMap.length,
            this.solutionOffset
        );
    }
}
