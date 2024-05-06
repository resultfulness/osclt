package lmp2.oscillate.ui;

import java.awt.Dimension;

import javax.swing.JFrame;

public class AppFrame extends JFrame {
    public AppFrame(int width, int height, AppWindow appWindow) {
        super();
        this.setTitle("osclt-maze");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(
            new Dimension(width, height)
        );
        this.setLocationRelativeTo(null);
        this.setJMenuBar(new AppMenuBar(appWindow));
    }
}
