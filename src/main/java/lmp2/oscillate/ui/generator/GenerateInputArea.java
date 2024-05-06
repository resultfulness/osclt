package lmp2.oscillate.ui.generator;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class GenerateInputArea extends JPanel {

    private MazeSizeChooser widthChooser;
    private MazeSizeChooser heightChooser;
    private MazeSizeChooser wallRemovalPercentageChooser;
    public GenerateInputArea() {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.initComponent();
    }

    private void initComponent() {
        this.widthChooser = new MazeSizeChooser("Maze width: ");
        this.heightChooser = new MazeSizeChooser("Maze height: ");
        this.wallRemovalPercentageChooser = new MazeSizeChooser("% walls removed (0-20): ");

        this.add(this.widthChooser);
        this.add(this.heightChooser);
        this.add(this.wallRemovalPercentageChooser);
    }

    public int getMazeWidth() {
        return this.widthChooser.getMazeSizeValue();
    }

    public int getMazeHeight() {
        return this.heightChooser.getMazeSizeValue();
    }

    public int getMazeWallRemovalPercentage() {
        return this.wallRemovalPercentageChooser.getMazeSizeValue();
    }
}
