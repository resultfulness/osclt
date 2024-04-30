package lmp2.oscillate.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.*;

import lmp2.oscillate.Maze_InputFormat;

public class AppWindow2 {
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final int MIN_CELL_SIZE = 1;
    private static final int MAX_CELL_SIZE = 128;
    private int cellSize = 16;

    private JFrame appFrame;
    private JPanel appContainer;
    private JScrollPane appSPContainer;
    private JPanel statusPanel;
    private JLabel toolLabel;
    private JLabel statusLabel;

    protected static enum Tool {
        NONE,
        WALL,
        PATH,
        START,
        END
    };
    private AppWindow2.Tool selectedTool = AppWindow2.Tool.NONE;

    protected Maze_InputFormat m;

    public AppWindow2() {
        this.appFrame = new JFrame();
        this.appFrame.setTitle("osclt-maze");
        this.appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.appFrame.setSize(
            new Dimension(AppWindow2.WINDOW_WIDTH, AppWindow2.WINDOW_HEIGHT)
        );
        this.appFrame.setLocationRelativeTo(null);
        this.appFrame.setJMenuBar(new AppMenuBar(this));
    }

    public void displayMaze(Maze_InputFormat m) {
        this.m = m;
        this.appFrame.setVisible(true);

        this.appContainer = new AppContainer(this);
        this.appSPContainer = new JScrollPane(this.appContainer);
        this.appSPContainer.setHorizontalScrollBarPolicy(
            JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
        );
        this.appSPContainer.setVerticalScrollBarPolicy(
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
        );
        this.appFrame.getContentPane().add(
            this.appSPContainer, BorderLayout.CENTER
        );
        
        this.statusPanel = new JPanel();
        this.statusPanel.setLayout(
            new BoxLayout(statusPanel, BoxLayout.Y_AXIS)
        );
        this.toolLabel = new JLabel();
        this.toolLabel.setHorizontalAlignment(JLabel.LEFT);
        this.statusLabel = new JLabel();
        this.statusLabel.setHorizontalAlignment(JLabel.LEFT);
        this.statusPanel.add(toolLabel);
        this.statusPanel.add(statusLabel);
        this.appFrame.getContentPane().add(
            this.statusPanel, BorderLayout.NORTH
        );

        this.selectTool(AppWindow2.Tool.NONE);
    }

    public int getCellSize() {
        return this.cellSize;
    }

    public void setCellSize(int cellSize) throws IllegalArgumentException {
        if (cellSize > AppWindow2.MAX_CELL_SIZE ||
            cellSize < AppWindow2.MIN_CELL_SIZE
        ) {
            throw new IllegalArgumentException("cellsize exceeds limit");
        }
        this.cellSize = cellSize;
        this.appFrame.repaint();
    }

    public AppWindow2.Tool getSelectedTool() {
        return this.selectedTool;
    }

    public void selectTool(AppWindow2.Tool tool) {
        this.selectedTool = tool;
        this.toolLabel.setText("TOOL: " + this.selectedTool);
        switch (tool) {
            case WALL:
                this.statusLabel.setText(
                    "INFO: Add walls by clicking on the highlighted regions."
                );
                break;
            case PATH:
                this.statusLabel.setText(
                    "INFO: Add paths by clicking on the highlighted regions."
                );
                break;
            case START:
                this.statusLabel.setText(
                    "INFO: Set the start position by clicking on a highlighted cell."
                );
                break;
            case END:
                this.statusLabel.setText(
                    "INFO: Set the end position by clicking on a highlighted cell."
                );
                break;
            case NONE:
                this.statusLabel.setText("INFO: Select a tool to get started.");
                break;
            default:
                break;
        }
    }

    public JPanel getAppContainer(){
        return this.appContainer;
    }
}
