package net.runelite.client.plugins.spawndumper;

import java.awt.GridLayout;
import javax.inject.Inject;
import javax.swing.JPanel;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

public class SpawnDumperPanel extends PluginPanel {
    private final SpawnDumperPlugin plugin;

    @Inject
    private SpawnDumperPanel(SpawnDumperPlugin plugin) {
        this.plugin = plugin;
        this.setBackground(ColorScheme.DARK_GRAY_COLOR);
        this.add(this.createOptionsPanel());
    }

    private JPanel createOptionsPanel() {
        JPanel container = new JPanel();
        container.setBackground(ColorScheme.DARK_GRAY_COLOR);
        container.setLayout(new GridLayout(0, 2, 3, 3));
        container.add(this.plugin.getEnabled());
        container.add(this.plugin.getSaveSpawns());
        container.add(this.plugin.getLoadSpawns());
        container.add(this.plugin.getClearSpawns());
        return container;
    }
}
