package lmp2.oscillate.ui;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class AppMenuBar extends JMenuBar {
    public AppMenuBar(AppWindow2 window) {
        class MenuItemFactory {
            public static JMenuItem create(
                String name, int kev, String tooltip, ActionListener l
            ) {
                JMenuItem menuItem = new JMenuItem(name);
                menuItem.setMnemonic(kev);
                menuItem.setToolTipText(tooltip);
                menuItem.addActionListener(l);
                return menuItem;
            }
        };

        JMenu appMenu = new JMenu("App");
        appMenu.setMnemonic(KeyEvent.VK_A);
        appMenu.add(MenuItemFactory.create(
            "Exit",
            KeyEvent.VK_E,
            "close application",
            e -> System.exit(0)
        ));

        JMenu viewMenu = new JMenu("View");
        viewMenu.setMnemonic(KeyEvent.VK_V);
        viewMenu.add(MenuItemFactory.create(
            "+ Zoom in",
            KeyEvent.VK_PLUS,
            "zoom in the view",
            e -> {
                try {
                    window.setCellSize(window.getCellSize() * 2);
                } catch (IllegalArgumentException ex) {
                }
            }
        ));
        viewMenu.add(MenuItemFactory.create(
            "- Zoom out",
            KeyEvent.VK_MINUS,
            "zoom out the view",
            e -> {
                try {
                    window.setCellSize(window.getCellSize() / 2);
                } catch (IllegalArgumentException ex) {
                }
            }
        ));

        JMenu toolMenu = new JMenu("Tool");
        toolMenu.setMnemonic(KeyEvent.VK_T);
        toolMenu.add(MenuItemFactory.create(
            "None",
            KeyEvent.VK_N,
            "unselect currently chosen tool",
            e -> window.selectTool(AppWindow2.Tool.NONE)
        ));
        toolMenu.add(MenuItemFactory.create(
            "Wall",
            KeyEvent.VK_W,
            "place down walls",
            e -> window.selectTool(AppWindow2.Tool.WALL)
        ));
        toolMenu.add(MenuItemFactory.create(
            "Path",
            KeyEvent.VK_P,
            "place down paths",
            e -> window.selectTool(AppWindow2.Tool.PATH)
        ));
        toolMenu.add(MenuItemFactory.create(
            "Start",
            KeyEvent.VK_S,
            "select start position",
            e -> window.selectTool(AppWindow2.Tool.START)
        ));
        toolMenu.add(MenuItemFactory.create(
            "End",
            KeyEvent.VK_E,
            "select end position",
            e -> window.selectTool(AppWindow2.Tool.END)
        ));

        this.add(appMenu);
        this.add(viewMenu);
        this.add(toolMenu);
    }
}
