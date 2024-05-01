package lmp2.oscillate.ui;

import java.awt.BorderLayout;

import javax.swing.*;

import lmp2.oscillate.Maze_InputFormat;

public class AppWindow {
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final int MIN_CELL_SIZE = 1;
    private static final int MAX_CELL_SIZE = 128;
    private int cellSize = 16;

    private JFrame appFrame;
    private JPanel mazeContainer;
    private JScrollPane appSPContainer;
    private JPanel statusPanel;
    private JLabel statusLabel;
    private ToolSelector toolSelector;

    protected static enum Tool {
        NONE,
        WALL,
        PATH,
        START,
        END
    };

    protected Maze_InputFormat m;

    public AppWindow() {
        this.appFrame = new AppFrame(AppWindow.WINDOW_WIDTH, AppWindow.WINDOW_HEIGHT, this);
    }

    public void displayMaze(Maze_InputFormat m) {
        this.m = m;
        this.appFrame.setVisible(true);

        this.mazeContainer = new MazeContainer(this, m);
        this.appSPContainer = new JScrollPane(this.mazeContainer);
        this.appSPContainer.setHorizontalScrollBarPolicy(
            JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
        );
        this.appSPContainer.setVerticalScrollBarPolicy(
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
        );
        this.appFrame.getContentPane().add(
            this.appSPContainer, BorderLayout.CENTER
        );
        
        createStatusPanel();
    }

    private void createStatusPanel() {
        this.statusPanel = new JPanel();
        this.statusPanel.setLayout(
            new BoxLayout(statusPanel, BoxLayout.Y_AXIS)
        );
        this.statusLabel = new JLabel();
        this.statusLabel.setHorizontalAlignment(JLabel.LEFT);
        this.statusPanel.add(statusLabel);
        this.appFrame.getContentPane().add(
            this.statusPanel, BorderLayout.NORTH
        );
        MazeInputField mazeInputField = new MazeInputField();
        this.statusPanel.add(mazeInputField);
        this.toolSelector = new ToolSelector();
        this.statusPanel.add(toolSelector);
        ZoomUtility zoomUtility = new ZoomUtility(this);
        this.statusPanel.add(zoomUtility);
    }

    public int getCellSize() {
        return this.cellSize;
    }

    public void setCellSize(int cellSize) throws IllegalArgumentException {
        if (cellSize > AppWindow.MAX_CELL_SIZE ||
            cellSize < AppWindow.MIN_CELL_SIZE
        ) {
            throw new IllegalArgumentException("cellsize exceeds limit");
        }
        this.cellSize = cellSize;
        this.appFrame.repaint();
    }

    public AppWindow.Tool getSelectedTool() {
        return this.toolSelector.getSelectedTool();
    }
}
