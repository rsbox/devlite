package net.runelite.client.plugins.spawndumper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayUtil;

import static java.lang.Math.abs;

public class SpawnDumperNPCOverlay extends Overlay {
    private final Client client;
    private final SpawnDumperPlugin plugin;

    @Inject
    private SpawnDumperNPCOverlay(Client client, SpawnDumperPlugin plugin) {
        this.client = client;
        this.plugin = plugin;
        this.setPosition(OverlayPosition.DYNAMIC);
        this.setLayer(OverlayLayer.ABOVE_SCENE);
        this.setPriority(OverlayPriority.MED);
    }

    public Dimension render(Graphics2D graphics) {
        if (this.plugin.getSelectedNPCIndex() == -1) {
            return null;
        } else {
            NPCSpawn spawn = (NPCSpawn)this.plugin.getSpawns().get(this.plugin.getSelectedNPCIndex());
            if (spawn == null) {
                return null;
            } else {
                Set points = spawn.getPoints();
                Iterator var4 = points.iterator();

                while(var4.hasNext()) {
                    WorldPoint point = (WorldPoint)var4.next();
                    this.renderTile(graphics, point, Color.BLUE);
                }

                int spawnX = spawn.getMinX() + (int)Math.ceil((double)(spawn.getMaxX() - spawn.getMinX()) / 2.0);
                int spawnY = spawn.getMinY() + (int)Math.ceil((double)(spawn.getMaxY() - spawn.getMinY()) / 2.0);
                int spawnZ = spawn.getMinZ();

                this.renderTile(graphics, new WorldPoint(spawnX, spawnY, spawnZ), Color.RED);
                return null;
            }
        }
    }

    private void renderTile(Graphics2D graphics, WorldPoint dest, Color color) {
        if (dest != null) {
            LocalPoint localPoint = LocalPoint.fromWorld(this.client, dest);
            if (localPoint != null) {
                Polygon poly = Perspective.getCanvasTilePoly(this.client, localPoint);
                if (poly != null) {
                    OverlayUtil.renderPolygon(graphics, poly, color);
                }
            }
        }
    }
}
