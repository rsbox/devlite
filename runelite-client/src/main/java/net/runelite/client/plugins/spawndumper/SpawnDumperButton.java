package net.runelite.client.plugins.spawndumper;

import java.awt.Color;
import javax.swing.JButton;

class SpawnDumperButton extends JButton {
    private boolean active;

    SpawnDumperButton(String title) {
        super(title);
        this.addActionListener((ev) -> {
            this.setActive(!this.active);
        });
        this.setToolTipText(title);
    }

    void setActive(boolean active) {
        this.active = active;
        if (active) {
            this.setBackground(Color.GREEN);
        } else {
            this.setBackground((Color)null);
        }

    }

    public boolean isActive() {
        return this.active;
    }
}
