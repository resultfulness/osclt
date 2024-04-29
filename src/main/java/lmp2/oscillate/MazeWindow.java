package lmp2.oscillate;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class MazeWindow extends JFrame {
    public MazeWindow(Maze_InputFormat maze_InputFormat) {
        super("OscltMaze");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setSize(800, 600);

        JPanel container = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        container.setMaximumSize(container.getPreferredSize());
        JScrollPane scrollPane = new JScrollPane(container);
        this.add(scrollPane);

        c.fill = GridBagConstraints.HORIZONTAL;
        for (int i = 0; i < maze_InputFormat.getFileWidth() * maze_InputFormat.getFileHeight(); i++) {
            JLabel l = new JLabel();
            l.setHorizontalAlignment(JLabel.CENTER);
            l.setPreferredSize(new Dimension(40, 40));
            l.setBackground(
                maze_InputFormat.getCharAt(i) == 'X'
                    ? Color.BLACK
                    : Color.WHITE
            );
            l.setOpaque(true);
            l.setBorder(BorderFactory.createLineBorder(Color.white, 1));

            c.gridx = i % maze_InputFormat.getFileWidth();
            c.gridy = i / maze_InputFormat.getFileWidth();
            container.add(l, c);
        }

        this.setVisible(true);
    }
}
