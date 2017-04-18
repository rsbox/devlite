package net.runelite.client.plugins.spawndumper;

import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;

public class SpawnDumperOverlay extends Overlay {
    private final SpawnDumperPlugin plugin;
    private final PanelComponent panelComponent = new PanelComponent();

    @Inject
    private SpawnDumperOverlay(SpawnDumperPlugin plugin) {
        this.plugin = plugin;
    }

    public Dimension render(Graphics2D graphics) {
        this.panelComponent.getChildren().clear();
        this.panelComponent.getChildren().add(LineComponent.builder().left("Spawn Dumper").build());
        this.panelComponent.getChildren().add(LineComponent.builder().left("Total Spawns").right(String.valueOf(this.plugin.getSpawns().size())).build());
        this.panelComponent.getChildren().add(LineComponent.builder().left("NPC Updates").right(String.valueOf(this.plugin.getUpdatedThisTick())).build());
        return this.panelComponent.render(graphics);
    }
}

