package lmp2.oscillate.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ZoomUtility extends JPanel implements ActionListener {
    private AppWindow appWindow;
    private JButton zoomInButton;
    private JButton zoomOutButton;

    public ZoomUtility(AppWindow appWindow) {
        super(new FlowLayout());
        this.appWindow = appWindow;

        initComponent();
    }

    private void initComponent() {
        this.zoomInButton = new JButton("Zoom in");
        this.zoomOutButton = new JButton("Zoom out");
        this.add(zoomInButton);
        this.add(zoomOutButton);
        this.zoomInButton.addActionListener(this);
        this.zoomOutButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if(event.getSource() == zoomInButton)
            try {
                appWindow.setCellSize(appWindow.getCellSize() + 1);
            } catch (IllegalArgumentException ex) {

            }
        else
            try {
                appWindow.setCellSize(appWindow.getCellSize() - 1);
            } catch (IllegalArgumentException ex) {

            }
    }
}
