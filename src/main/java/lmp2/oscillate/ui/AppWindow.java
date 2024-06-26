package lmp2.oscillate.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
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

import lmp2.oscillate.AppLogger;
import lmp2.oscillate.Maze;
import lmp2.oscillate.Maze_InputFormat;
import lmp2.oscillate.generator.MazeGenerator;
import lmp2.oscillate.parser.BinaryMazeParser;
import lmp2.oscillate.parser.MazeParser;
import lmp2.oscillate.parser.RegularMazeParser;
import lmp2.oscillate.pathfinder.AStar;
import lmp2.oscillate.pathfinder.BFS;
import lmp2.oscillate.pathfinder.DFS;
import lmp2.oscillate.pathfinder.PathFinder;
import lmp2.oscillate.ui.generator.GenerateInputArea;

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
    private JButton generateButton;
    private GenerateInputArea generateInputArea;
    AlgorithmSelectionField algSelector;

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
        
        this.pathFinder = new DFS(this.mazeStruct, this.m, this);
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
        JPanel generateSolvePanel = PanelFactory.create("Generate & Solve");
        this.statusPanel.add(generateSolvePanel);

        this.appFrame.getContentPane().add(
            this.statusPanel, BorderLayout.NORTH
        );

        MazeFilePicker mazeFilePicker = new MazeFilePicker(this);
        c.gridy = 0;
        filePanel.add(mazeFilePicker, c);

        editPanel.setLayout(new BorderLayout());
        this.toolSelector = new ToolSelector(this);
        editPanel.add(toolSelector, BorderLayout.NORTH);

        ZoomUtility zoomUtility = new ZoomUtility(this);
        editPanel.add(zoomUtility, BorderLayout.SOUTH);

        // Generate & Solve section
        this.generateInputArea = new GenerateInputArea();
        c.gridy = 0;
        generateSolvePanel.add(generateInputArea, c);

        this.algSelector = new AlgorithmSelectionField(this.m, this);
        c.gridy = 1;
        generateSolvePanel.add(algSelector, c);

        JPanel generateSolveButtonsPanel = new JPanel(new FlowLayout());

        this.generateButton = new JButton("Generate maze");
        this.generateButton.addActionListener(this);
        generateSolveButtonsPanel.add(this.generateButton);

        this.solveButton = new JButton("Solve maze");
        this.solveButton.addActionListener(this);
        generateSolveButtonsPanel.add(this.solveButton);

        c.gridy = 2;
        generateSolvePanel.add(generateSolveButtonsPanel, c);
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

    public void stopSolve() {
        this.pathFinder.interrupt();
        this.mazeStruct.resetMaze();
        this.m.clearSolution();
        this.mazeContainer.repaint();
        this.mazeContainer.revalidate();
    }

    public void loadMaze(String inputFilePath, boolean isInputBinary) { 
        stopSolve();
        Logger logger = AppLogger.getLogger();

        MazeParser parser = isInputBinary
            ? new BinaryMazeParser()
            : new RegularMazeParser();

        try {
            parser.parseInto(this.m, inputFilePath);
            this.mazeStruct = Maze.fromInputFormat(this.m);

            LogDialog.show(
                "`" + inputFilePath + "` loaded!", LogDialog.Level.INFO);
        } catch (
            IllegalStateException | IOException | IndexOutOfBoundsException e
        ) {
            logger.log(Level.SEVERE, e.getMessage());
            LogDialog.show(e.getMessage(), LogDialog.Level.CRITICAL);
            System.exit(1);
        }
    }

    public void setMaze(Maze maze) {
        this.mazeStruct = maze;
    }

    public Maze getMaze() {
        return this.mazeStruct;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        stopSolve();
        if(event.getSource() == this.solveButton) {
            Maze tempMaze = Maze.fromInputFormat(this.m);
            tempMaze.setEndIndex(this.mazeStruct.getEndIndex());
            tempMaze.setStartIndex(this.mazeStruct.getStartIndex());
            this.mazeStruct = tempMaze;
            switch(this.algSelector.getSelectedAlgorithm()){
                case DFS:
                    this.pathFinder = new DFS(this.mazeStruct, this.m, this);
                    break;
                case BFS:
                    this.pathFinder = new BFS(this.mazeStruct, this.m, this);
                    break;
                case AStarEuclidian:
                    this.pathFinder = new AStar(this.mazeStruct, this.m, this, true);
                    break;
                case AStarManhattan:
                    this.pathFinder = new AStar(this.mazeStruct, this.m, this, false);
                    break;
                default:
                    this.pathFinder = new BFS(this.mazeStruct, this.m, this);
                    break;
            }
            this.mazeStruct.resetMaze();
            this.pathFinder.start();
        }
        if(event.getSource() == this.generateButton) {
            try {
                MazeGenerator generator = new MazeGenerator(this.generateInputArea.getMazeWidth(), this.generateInputArea.getMazeHeight());
                this.mazeStruct = generator.generateMaze(this.generateInputArea.getMazeWallRemovalPercentage());
                System.out.println(this.mazeStruct);
                this.m.fromMaze(this.mazeStruct);
                LogDialog.show("Maze generated successfully!", LogDialog.Level.INFO);
            } catch (IllegalArgumentException e) {
                LogDialog.show(e.getMessage(), LogDialog.Level.ERROR);
            }
        }
    }

    public void onToolChange() {
        this.mazeContainer.repaint();
    }
}
