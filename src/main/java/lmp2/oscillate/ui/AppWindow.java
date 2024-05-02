package lmp2.oscillate.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import lmp2.oscillate.AppLogger;
import lmp2.oscillate.Maze_InputFormat;
import lmp2.oscillate.parser.BinaryMazeParser;
import lmp2.oscillate.parser.RegularMazeParser;

public class AppWindow implements ActionListener {
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
    private JButton solveButton;

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

        MazeFilePicker mazeFilePicker = new MazeFilePicker(this);
        this.statusPanel.add(mazeFilePicker);

        this.toolSelector = new ToolSelector();
        this.statusPanel.add(toolSelector);

        ZoomUtility zoomUtility = new ZoomUtility(this);
        this.statusPanel.add(zoomUtility);

        AlgorithmSelectionField algSelector = new AlgorithmSelectionField();
        this.statusPanel.add(algSelector);

        this.solveButton = new JButton("Solve maze");
        this.solveButton.addActionListener(this);
        this.statusPanel.add(solveButton);
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
        // notify scrollpane about zoom change
        this.mazeContainer.revalidate();
    }

    public AppWindow.Tool getSelectedTool() {
        return this.toolSelector.getSelectedTool();
    }

    public void loadMaze(String inputFilePath, boolean isInputBinary) { 
        Logger logger = AppLogger.getLogger();

        if(isInputBinary) {
            try {
                new BinaryMazeParser().parseInto(m, inputFilePath);
                mazeContainer.repaint();
            } catch (IOException | IllegalStateException ex) {
                logger.log(Level.SEVERE, ex.getMessage());
                System.exit(1);
            }
        }
        else
            try {
                new RegularMazeParser().parseInto(m, inputFilePath);
                mazeContainer.repaint();
            } catch (IOException | IllegalStateException ex) {
                logger.log(Level.SEVERE, ex.getMessage());
                System.exit(1);
            }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if(event.getSource() == this.solveButton)
            // Solve here
            throw new UnsupportedOperationException();
    }
}
