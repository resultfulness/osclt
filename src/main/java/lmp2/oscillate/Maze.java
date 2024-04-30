package lmp2.oscillate;

import java.util.ArrayList;
import java.util.Arrays;

import lmp2.oscillate.pathfinder.BFS;
import lmp2.oscillate.pathfinder.PathFinder;

public class Maze {
    private int width;
    private int height;
    private int startIndex;
    private int endIndex;
    private int solutionOffset;
    private ArrayList<Cell> cells;

    public static final byte NORTH_VALUE = 0b1000;
    public static final byte EAST_VALUE = 0b0100;
    public static final byte SOUTH_VALUE = 0b0010;
    public static final byte WEST_VALUE = 0b0001;

    Maze(
        int width,
        int height,
        int solutionOffset
    ) {
        this.setWidth(width);
        this.setHeight(height);
        this.setSolutionOffset(solutionOffset);
        this.cells = new ArrayList<>(this.width * this.height);
    }

    public static Maze fromInputFormat(Maze_InputFormat maze_InputFormat)
    throws IndexOutOfBoundsException, IllegalStateException {
        Maze maze = new Maze(
            maze_InputFormat.getMazeWidth(),
            maze_InputFormat.getMazeHeight(),
            maze_InputFormat.getSolutionOffset()
        );

        int fw = maze_InputFormat.getFileWidth();
        int fh = maze_InputFormat.getFileHeight();

        for (int i = 1; i < fh; i += 2) {
            for (int j = 1; j < fw; j += 2) {
                int cellIndex = i * fw + j;
                char cell = maze_InputFormat.getCharAt(cellIndex);
                if (cell != Maze_InputFormat.PATH) {
                    throw new IllegalStateException(
                        "maze in input format corrupted"
                    );
                }
                char adjN = maze_InputFormat.getCharAt(cellIndex - fh);
                char adjE = maze_InputFormat.getCharAt(cellIndex + 1);
                char adjS = maze_InputFormat.getCharAt(cellIndex + fh);
                char adjW = maze_InputFormat.getCharAt(cellIndex - 1);

                byte cellAdjacencies = 0;
                cellAdjacencies = Maze.cell_handle_adj(
                    cellAdjacencies, adjN, NORTH_VALUE);
                cellAdjacencies = Maze.cell_handle_adj(
                    cellAdjacencies, adjE, EAST_VALUE);
                cellAdjacencies = Maze.cell_handle_adj(
                    cellAdjacencies, adjS, SOUTH_VALUE);
                cellAdjacencies = Maze.cell_handle_adj(
                    cellAdjacencies, adjW, WEST_VALUE);

                if (Arrays
                        .asList(adjN, adjE, adjS,  adjW)
                        .contains(Maze_InputFormat.START)
                ) {
                    maze.setStartIndex(maze.getCellCount());
                }
                if (Arrays
                        .asList(adjN, adjE, adjS,  adjW)
                        .contains(Maze_InputFormat.END)
                ) {
                    maze.setEndIndex(maze.getCellCount());
                }

                maze.add(cellAdjacencies);
            }
        }

        return maze;
    }

    private static byte cell_handle_adj(
        byte cellAdjacencies,
        char adj,
        byte direction
    ) {
        if (!Arrays
                .asList(NORTH_VALUE, EAST_VALUE, SOUTH_VALUE, WEST_VALUE)
                .contains(direction)
        ) {
            throw new IllegalArgumentException("invalid direction");
        }
        if (adj == Maze_InputFormat.PATH) {
            return (byte)(cellAdjacencies | direction);
        }
        if (Arrays
                .asList(
                    Maze_InputFormat.WALL,
                    Maze_InputFormat.START,
                    Maze_InputFormat.END
                )
                .contains(adj)) {
            return cellAdjacencies;
        }
        throw new IllegalStateException("maze in input format corrupted");
    }

    public void add(byte adjacents) throws IndexOutOfBoundsException {
        if (this.getCellCount() >= this.width * this.height) {
            throw new IndexOutOfBoundsException(
                "maze already has the maximum number of cells added"
            );
        }
        this.cells.add(new Cell(adjacents));
    }

    public void addMany(byte[] adjacents) throws IndexOutOfBoundsException {
        for (byte adj : adjacents) {
            this.add(adj);
        }
    }

    private Cell get(int index) throws IndexOutOfBoundsException {
        return this.cells.get(index);
    }

    public int getCellCount() {
        return this.cells.size();
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getSize() {
        return this.getWidth() * this.getHeight();
    }

    private void setWidth(int width) throws IllegalArgumentException {
        if (width < 0) {
            throw new IllegalArgumentException("width must be greater than 0");
        }
        this.width = width;
    }

    private void setHeight(int height) throws IllegalArgumentException {
        if (height < 0) {
            throw new IllegalArgumentException("height must be greater than 0");
        }
        this.height = height;
    }

    public byte getAdjacentsAt(int index) throws IndexOutOfBoundsException {
        return this.get(index).getAdjacents();
    }

    public void setAdjacentsAt(int index, byte adjacents)
    throws IndexOutOfBoundsException {
        Cell old = this.get(index);
        this.cells.set(index, new Cell(adjacents));
        if (old != null) {
            this.setParentIndexAt(index, old.getParentIndex());
        }
    }

    public int getParentIndexAt(int index) throws IndexOutOfBoundsException {
        return this.get(index).getParentIndex();
    }

    public void setParentIndexAt(int index, int parentIndex)
    throws IndexOutOfBoundsException {
        this.get(index).setParentIndex(parentIndex);
    }

    public int getStartIndex() {
        return this.startIndex;
    }

    public int getEndIndex() {
        return this.endIndex;
    }

    public void setStartIndex(int startIndex) throws IndexOutOfBoundsException {
        if (!isIndexWithinMaze(startIndex)) {
            throw new IndexOutOfBoundsException("start index outside of maze");
        }
        this.startIndex = startIndex;
    }

    public void setEndIndex(int endIndex) throws IndexOutOfBoundsException {
        if (!isIndexWithinMaze(endIndex)) {
            throw new IndexOutOfBoundsException("end index outside of maze");
        }
        this.endIndex = endIndex;
    }

    private boolean isIndexWithinMaze(int index) {
        return index >= 0 && index < this.getSize();
    }

    public int getSolutionOffset() {
        return this.solutionOffset;
    }

    private void setSolutionOffset(int solutionOffset) {
        this.solutionOffset = solutionOffset;
    }

    @Override
    public String toString() {
        final int truncateAfter = 3;
        return String.format("""
            Maze {
                width: %s,
                height: %s,
                startIndex: %s,
                endIndex: %s,
                solutionOffset: %s,
                cells: \n%s,
            }\
            """,
            this.width,
            this.height,
            this.startIndex,
            this.endIndex,
            this.solutionOffset,
            this.cells
            .stream()
            .limit(truncateAfter)
            .collect(java.util.stream.Collectors.toList())
            .toString() +
            "\t+" + (this.cells.size() - truncateAfter) + " more..."
        );
    }

    public class Cell {
        private byte adjacents;
        private int parentIndex;
        private boolean visited;

        protected Cell(byte adjacents) {
            this.adjacents = adjacents;
            this.visited = false;
        }

        protected byte getAdjacents() {
            return this.adjacents;
        }

        protected int getParentIndex() {
            return this.parentIndex;
        }

        protected void setParentIndex(int parentIndex) {
            this.parentIndex = parentIndex;
        }
        
        public boolean isVisited() {
            return this.visited;
        }

        public void setVisited(boolean visited) {
            if(this.visited)
                return;
            this.visited = visited;
        }

        @Override
        public String toString() {
            return String.format("""
                Cell {
                    adjacents: %s,
                    parentIndex: %s,
                }\
                """,
                Integer.toBinaryString(this.adjacents),
                this.parentIndex
            );
        }
    }
}
