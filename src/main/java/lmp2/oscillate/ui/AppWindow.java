package lmp2.oscillate.ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import lmp2.oscillate.Maze;
import lmp2.oscillate.AppLogger;
import lmp2.oscillate.Maze_InputFormat;
import lmp2.oscillate.parser.BinaryMazeParser;
import lmp2.oscillate.parser.RegularMazeParser;
import lmp2.oscillate.pathfinder.BFS;
import lmp2.oscillate.pathfinder.DFS;
import lmp2.oscillate.pathfinder.PathFinder;
import lmp2.oscillate.pathfinder.SolutionPresenter;

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
    private ToolSelector toolSelector;
    private JButton solveButton;
    AlgorithmSelectionField algSelector;

    private SolutionPresenter solutionPresenter;
    private PathFinder pathFinder;
    private Maze mazeStruct;

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
        this.solutionPresenter = new SolutionPresenter();
        this.pathFinder = new DFS();
    }

    public void displayMaze(Maze_InputFormat m) {
        this.m = m;
        this.appFrame.setVisible(true);

        this.mazeContainer = new MazeContainer(this);
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
        this.statusPanel = new JPanel(new GridLayout(1, 3));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.anchor = GridBagConstraints.NORTH;

        class PanelFactory {
            public static JPanel create(String title) {
                JPanel p = new JPanel(new GridBagLayout());
                p.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(java.awt.Color.lightGray, 1),
                    title
                ));
                return p;
            }
        };

        JPanel filePanel = PanelFactory.create("File");
        this.statusPanel.add(filePanel);
        JPanel editPanel = PanelFactory.create("Edit");
        this.statusPanel.add(editPanel);
        JPanel solvePanel = PanelFactory.create("Solve");
        this.statusPanel.add(solvePanel);

        this.appFrame.getContentPane().add(
            this.statusPanel, BorderLayout.NORTH
        );

        MazeFilePicker mazeFilePicker = new MazeFilePicker(this);
        c.gridy = 0;
        filePanel.add(mazeFilePicker, c);

        this.toolSelector = new ToolSelector(this);
        c.gridy = 1;
        editPanel.add(toolSelector, c);

        ZoomUtility zoomUtility = new ZoomUtility(this);
        c.gridy = 2;
        editPanel.add(zoomUtility, c);

        this.algSelector = new AlgorithmSelectionField(this.m, (MazeContainer) this.mazeContainer);
        this.statusPanel.add(algSelector);
        c.gridy = 0;
        solvePanel.add(algSelector, c);

        this.solveButton = new JButton("Solve maze");
        this.solveButton.addActionListener(this);
        c.gridy = 1;
        solvePanel.add(solveButton, c);

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

    public JPanel getMazeContainer(){
        return this.mazeContainer;
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
        try {
            this.mazeStruct = Maze.fromInputFormat(m);
        } catch (IllegalStateException | IndexOutOfBoundsException e) {
            // Handle errors
        }
    }

    public void setMaze(Maze maze) {
        this.mazeStruct = maze;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if(event.getSource() == this.solveButton) {
            switch(this.algSelector.getSelectedAlgorithm()){
                case DFS:
                    this.pathFinder = new DFS();
                    break;
                case BFS:
                    this.pathFinder = new BFS();
                    break;
                default:
                    this.pathFinder = new BFS();
                    break;
            }
            this.mazeStruct.resetMaze();
            this.pathFinder.solveMaze(this.mazeStruct, m, this);
        }
    }

    public void onToolChange() {
        this.mazeContainer.repaint();
    }
}
