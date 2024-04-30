package lmp2.oscillate.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.awt.*;

import javax.swing.JPanel;

import lmp2.oscillate.AppLogger;
import lmp2.oscillate.Maze_InputFormat;

public class AppContainer extends JPanel {
    private AppWindow2 window;

    public AppContainer(AppWindow2 window) {
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
                    case WALL:
                        break;
                    case PATH:
                        break;
                    case START:
                        break;
                    case END:
                        break;
                }

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
                boolean isWall = window.m.getCharAt(k) == Maze_InputFormat.WALL;
                if (isWall) {
                    int w = window.getCellSize();
                    int x = j * w;
                    int y = i * w;
                    g.setColor(Color.black);
                    g.fillRect(x, y, w, w);
                }
            }
        }

        int w = window.getCellSize();
        int sX = window.m.getFileStartIndex() % fw;
        int sY = window.m.getFileStartIndex() / fw;
        int eX = window.m.getFileEndIndex() % fw;
        int eY = window.m.getFileEndIndex() / fw;

        g.setColor(Color.cyan);
        g.fillRect(sX * w, sY * w, w, w);

        g.setColor(Color.magenta);
        g.fillRect(eX * w, eY * w, w, w);

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
