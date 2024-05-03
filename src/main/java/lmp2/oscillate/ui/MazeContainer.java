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

public class MazeContainer extends JPanel {
    private AppWindow window;

    public MazeContainer(AppWindow window) {
        this.window = window;
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
                        break;
                    case PATH:
                        if(!window.m.canBePath(cellXY))
                            break;
                        window.m.mapCharAt(Maze_InputFormat.PATH, cellXY);
                        break;
                    case WALL:
                        if(!window.m.canBeWall(cellXY))
                            break;
                        window.m.mapCharAt(Maze_InputFormat.WALL, cellXY);
                        break;
                    case START:
                        if(!window.m.canBeStartEndIndex(cellXY))
                            break;
                        window.m.mapCharAt(Maze_InputFormat.START, cellXY);
                        break;
                    case END:
                        if(!window.m.canBeStartEndIndex(cellXY))
                            break;
                        window.m.mapCharAt(Maze_InputFormat.END, cellXY);
                        break;
                }
                window.m.clearSolution();

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

                int w = window.getCellSize();
                int x = j * w;
                int y = i * w;
                switch (mazeCharacter) {
                    case (Maze_InputFormat.END):
                        g.setColor(new Color(250,128,114));
                        break;
                    case (Maze_InputFormat.START):
                        g.setColor(new Color(114, 168, 250));
                        break;
                    case (Maze_InputFormat.WALL):
                        g.setColor(Color.black);
                        break;
                    case (Maze_InputFormat.PATH_TRACE):
                        g.setColor(Color.gray);
                        break;
                    case (Maze_InputFormat.PATH_SOLUTION):
                        g.setColor(new Color(114, 250, 128));
                        break;
                    default:
                        g.setColor(Color.white);
                }
                g.fillRect(x, y, w, w);

                g.setColor(Color.GREEN);
                ((java.awt.Graphics2D) g).setStroke(new java.awt.BasicStroke(4));
                switch (window.getSelectedTool()) {
                    case NONE:
                        break;
                    case WALL:
                        if (window.m.canBeWall(k) &&
                            mazeCharacter == Maze_InputFormat.PATH) {
                            g.drawOval(x+2, y+2 ,w-4, w-4);
                        }
                        break;
                    case PATH:
                        if (window.m.canBePath(k) &&
                            mazeCharacter == Maze_InputFormat.WALL) {
                            g.drawOval(x+2, y+2 ,w-4, w-4);
                        }
                        break;
                    case START:
                    case END:
                        if (window.m.canBeStartEndIndex(k)) {
                            g.drawOval(x+2, y+2 ,w-4, w-4);
                        }
                        break;
                }
            }
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
