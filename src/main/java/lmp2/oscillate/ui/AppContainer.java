package lmp2.oscillate.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;

import lmp2.oscillate.AppLogger;
import lmp2.oscillate.Maze_InputFormat;

public class AppContainer extends JPanel {
    private AppWindow2 window;
    private Maze_InputFormat maze_inputFormat;

    public AppContainer(AppWindow2 window, Maze_InputFormat maze_inputFormat) {
        this.window = window;
        this.maze_inputFormat = maze_inputFormat;
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fw = window.m.getFileWidth();
                int fh = window.m.getFileHeight();

                int cellX = e.getX() / window.getCellSize();
                int cellY = e.getY() / window.getCellSize();

                if (cellX >= fw || cellY >= fh ||
                    window.getSelectedTool() == null
                ) {
                    return;
                }

                int cellXY = cellY * fw + cellX;

                Logger logger = AppLogger.getLogger();
                logger.log(
                    Level.WARNING,
                    "Mouse pressed on cell " +
                    cellXY +
                    " with tool " +
                    window.getSelectedTool()
                );

                // handle using tools on cells
                switch (window.getSelectedTool()) {
                    case NONE:
                    case PATH:
                        if(!maze_inputFormat.canBePath(cellXY))
                            break;
                        maze_inputFormat.mapCharAt(Maze_InputFormat.PATH, cellXY);
                        break;
                    case WALL:
                        if(!maze_inputFormat.canBeWall(cellXY))
                            break;
                        maze_inputFormat.mapCharAt(Maze_InputFormat.WALL, cellXY);
                        break;
                    case START:
                        if(!maze_inputFormat.canBeTarget(cellXY))
                            break;
                        maze_inputFormat.mapCharAt(Maze_InputFormat.START, cellXY);
                        break;
                    case END:
                        if(!maze_inputFormat.canBeTarget(cellXY))
                            break;
                        maze_inputFormat.mapCharAt(Maze_InputFormat.END, cellXY);
                        break;
                }
                maze_inputFormat.clearSolution();

                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int fh = window.m.getFileHeight();
        int fw = window.m.getFileWidth();
        for (int i = 0; i < fh; i++) {
            for (int j = 0; j < fw; j++) {
                int k = i * fw + j;
                char mazeCharacter = window.m.getCharAt(k);
                if(mazeCharacter == Maze_InputFormat.PATH)
                    continue;

                int w = window.getCellSize();
                int x = j * w;
                int y = i * w;
                switch(mazeCharacter){
                    case (Maze_InputFormat.END):
                        g.setColor(Color.magenta);
                        break;
                    case (Maze_InputFormat.START):
                        g.setColor(Color.cyan);
                        break;
                    case (Maze_InputFormat.WALL):
                        g.setColor(Color.black);
                        break;
                    case (Maze_InputFormat.PATH_TRACE):
                        g.setColor(Color.green);
                        break;
                    case (Maze_InputFormat.PATH_SOLUTION):
                        g.setColor(Color.red);
                        break;
                }
                g.fillRect(x, y, w, w);
            }
        }

        // highlight available editable cells
        switch (window.getSelectedTool()) {
            case NONE:
                break;
            case WALL:
                break;
            case PATH:
                break;
            case START:
                break;
            case END:
                break;
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(
            window.m.getFileWidth() * window.getCellSize(),
            window.m.getFileHeight() * window.getCellSize()
        );
    }
}
