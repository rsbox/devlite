package net.runelite.client.plugins.spawndumper;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.runelite.api.coords.WorldPoint;

public class NPCSpawn {
    private final int npc;
    private final int index;
    private final Set<WorldPoint> points = new HashSet();
    private int orientation;

    public static void main(String[] args) throws FileNotFoundException {
        Map<Integer, String> names = (new Gson()).fromJson(new InputStreamReader(new FileInputStream("npc_names.json")), new TypeToken<Map<Integer, NPCSpawn>>(){}.getType());
        List<NPCSpawn> spawns = (new Gson()).fromJson(new InputStreamReader(new FileInputStream("spawns.json")), new TypeToken<List<NPCSpawn>>(){}.getType());
        spawns.sort(Comparator.comparingInt(NPCSpawn::getNpc));
        System.out.println("[");
        Iterator var3 = spawns.iterator();

        while(var3.hasNext()) {
            NPCSpawn spawn = (NPCSpawn)var3.next();
            int deltaX = spawn.getMaxX() - spawn.getMinX();
            int deltaY = spawn.getMaxY() - spawn.getMinY();
            int walkRadius = Math.max(deltaX, deltaY);
            int spawnX = spawn.getMinX() + deltaX / 2;
            int spawnY = spawn.getMinY() + deltaY / 2;
            StringBuilder builder = new StringBuilder();
            builder.append("  { ");
            builder.append("\"id\": ").append(spawn.getNpc());
            builder.append(", \"x\": ").append(spawnX);
            builder.append(", \"y\": ").append(spawnY);
            builder.append(", \"z\": ").append(spawn.getMinZ());
            if (walkRadius != 0) {
                builder.append(", \"walkRange\": ").append(walkRadius);
            }

            if (spawn.getOrientation() != -1) {
                builder.append(", \"direction\": ").append('"').append(orientationToString(spawn.getOrientation())).append('"');
            }

            builder.append(" },");
            builder.append(" // ").append((String)names.get(spawn.getNpc()));
            System.out.println(builder);
        }

        System.out.println("]");
    }

    private static String orientationToString(int orientation) {
        switch (orientation) {
            case 0:
                return "S";
            case 256:
                return "SW";
            case 512:
                return "W";
            case 768:
                return "NW";
            case 1024:
                return "N";
            case 1280:
                return "NE";
            case 1536:
                return "E";
            case 1792:
                return "SE";
            default:
                return "S";
        }
    }

    public int getMinX() {
        int minX = Integer.MAX_VALUE;

        WorldPoint point;
        for(Iterator var2 = this.points.iterator(); var2.hasNext(); minX = Math.min(minX, point.getX())) {
            point = (WorldPoint)var2.next();
        }

        return minX;
    }

    public int getMaxX() {
        int maxX = Integer.MIN_VALUE;

        WorldPoint point;
        for(Iterator var2 = this.points.iterator(); var2.hasNext(); maxX = Math.max(maxX, point.getX())) {
            point = (WorldPoint)var2.next();
        }

        return maxX;
    }

    public int getMinY() {
        int minY = Integer.MAX_VALUE;

        WorldPoint point;
        for(Iterator var2 = this.points.iterator(); var2.hasNext(); minY = Math.min(minY, point.getY())) {
            point = (WorldPoint)var2.next();
        }

        return minY;
    }

    public int getMaxY() {
        int maxY = Integer.MIN_VALUE;

        WorldPoint point;
        for(Iterator var2 = this.points.iterator(); var2.hasNext(); maxY = Math.max(maxY, point.getY())) {
            point = (WorldPoint)var2.next();
        }

        return maxY;
    }

    public int getMinZ() {
        int minZ = Integer.MAX_VALUE;

        WorldPoint point;
        for(Iterator var2 = this.points.iterator(); var2.hasNext(); minZ = Math.min(minZ, point.getPlane())) {
            point = (WorldPoint)var2.next();
        }

        return minZ;
    }

    public int getMaxZ() {
        int maxZ = Integer.MIN_VALUE;

        WorldPoint point;
        for(Iterator var2 = this.points.iterator(); var2.hasNext(); maxZ = Math.max(maxZ, point.getPlane())) {
            point = (WorldPoint)var2.next();
        }

        return maxZ;
    }

    public NPCSpawn(int npc, int index) {
        this.npc = npc;
        this.index = index;
    }

    public int getNpc() {
        return this.npc;
    }

    public int getIndex() {
        return this.index;
    }

    public Set<WorldPoint> getPoints() {
        return this.points;
    }

    public int getOrientation() {
        return this.orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof NPCSpawn)) {
            return false;
        } else {
            NPCSpawn other = (NPCSpawn)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.getNpc() != other.getNpc()) {
                return false;
            } else if (this.getIndex() != other.getIndex()) {
                return false;
            } else if (this.getOrientation() != other.getOrientation()) {
                return false;
            } else {
                Object this$points = this.getPoints();
                Object other$points = other.getPoints();
                if (this$points == null) {
                    if (other$points != null) {
                        return false;
                    }
                } else if (!this$points.equals(other$points)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof NPCSpawn;
    }

    public int hashCode() {
        int result = 1;
        result = result * 59 + this.getNpc();
        result = result * 59 + this.getIndex();
        result = result * 59 + this.getOrientation();
        Object $points = this.getPoints();
        result = result * 59 + ($points == null ? 43 : $points.hashCode());
        return result;
    }

    public String toString() {
        int var10000 = this.getNpc();
        return "NPCSpawn(npc=" + var10000 + ", index=" + this.getIndex() + ", points=" + this.getPoints() + ", orientation=" + this.getOrientation() + ")";
    }
}
