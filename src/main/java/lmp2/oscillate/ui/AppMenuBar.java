package lmp2.oscillate.ui;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class AppMenuBar extends JMenuBar {
    public AppMenuBar(AppWindow window) {
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

        this.add(appMenu);
    }
}
