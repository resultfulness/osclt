package lmp2.oscillate;

import java.util.ArrayList;

public class Maze {
    private int width;
    private int height;
    private int startIndex;
    private int endIndex;
    private ArrayList<Cell> cells;

    Maze(int width, int height, int startIndex, int endIndex) {
        this.setWidth(width);
        this.setHeight(height);
        this.setStartIndex(startIndex);
        this.setEndIndex(endIndex);
        this.cells = new ArrayList<>(this.width * this.height);
    }

    public void add(byte adjacents) throws IndexOutOfBoundsException {
        if (this.getCellCount() >= this.width * this.height) {
            throw new IndexOutOfBoundsException(
                "maze already has the maximum number of cells added"
            );
        }
        this.cells.add(new Cell(adjacents));
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
        return index >= 0 && index < this.width * this.height;
    }

    @Override
    public String toString() {
        return String.format(
            """
            Maze {
                cells: %s,
                width: %s,
                height: %s,
                startIndex: %s,
                endIndex: %s,
            }\
            """
        );
    }

    public class Cell {
        private byte adjacents;
        private int parentIndex;

        protected Cell(byte adjacents) {
            this.adjacents = adjacents;
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

        @Override
        public String toString() {
            return String.format(
                """
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
