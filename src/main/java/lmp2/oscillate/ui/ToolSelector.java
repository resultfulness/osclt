package lmp2.oscillate.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ToolSelector extends JPanel implements ActionListener {
    private AppWindow.Tool selectedTool;
    private JLabel toolInfo;
    private JLabel toolLabel;
    private JComboBox<AppWindow.Tool> toolList;
    private AppWindow window;

    public ToolSelector(AppWindow window) {
        super();
        this.window = window;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(createToolSelectArea());
        this.toolInfo = new JLabel();
        this.toolInfo.setHorizontalAlignment(JLabel.CENTER);
        // a bit hacky but at least it's no longer off
        JPanel p = new JPanel();
        p.add(this.toolInfo);
        this.add(p);
        setTool(AppWindow.Tool.NONE);
    }

    private JPanel createToolSelectArea() {
        JPanel toolSelectArea = new JPanel(new FlowLayout());
        this.toolLabel = new JLabel();
        this.toolLabel.setHorizontalAlignment(JLabel.CENTER);
        this.toolLabel.setText("TOOL: ");

        toolSelectArea.add(this.toolLabel);
        createToolList();
        toolSelectArea.add(this.toolList);
        return toolSelectArea;
    }

    private void createToolList() {
        this.toolList = new JComboBox<AppWindow.Tool>(AppWindow.Tool.values());
        this.toolList.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        setTool((AppWindow.Tool) this.toolList.getSelectedItem());
        this.window.onToolChange();
    }

    private void setTool(AppWindow.Tool tool){
        this.selectedTool = tool;
        switch (this.selectedTool) {
            case WALL:
                this.toolInfo.setText(
                    "INFO: Add walls by clicking on the highlighted regions."
                );
                break;
            case PATH:
                this.toolInfo.setText(
                    "INFO: Add paths by clicking on the highlighted regions."
                );
                break;
            case START:
                this.toolInfo.setText(
                    "INFO: Set the start position by clicking on a highlighted cell."
                );
                break;
            case END:
                this.toolInfo.setText(
                    "INFO: Set the end position by clicking on a highlighted cell."
                );
                break;
            case NONE:
                this.toolInfo.setText("INFO: Select a tool to get started.");
                break;
            default:
                break;
        }
    }

    public AppWindow.Tool getSelectedTool() {
        return selectedTool;
    }
}
