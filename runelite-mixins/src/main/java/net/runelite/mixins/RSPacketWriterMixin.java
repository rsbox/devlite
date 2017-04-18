package net.runelite.mixins;

import net.runelite.api.events.ServerPacketReadCompleteEvent;
import net.runelite.api.events.ServerPacketReadStartedEvent;
import net.runelite.api.mixins.FieldHook;
import net.runelite.api.mixins.Inject;
import net.runelite.api.mixins.Mixin;
import net.runelite.api.mixins.Shadow;
import net.runelite.api.packets.PacketWriter;
import net.runelite.api.packets.ServerPacket;
import net.runelite.rs.api.RSClient;
import net.runelite.rs.api.RSPacketWriter;

@Mixin(RSPacketWriter.class)
public abstract class RSPacketWriterMixin implements RSPacketWriter
{
    @Shadow("client")
    private static RSClient client;

    @FieldHook("serverPacket")
    @Inject
    public void onServerPacketChanged(int idx) {
        if (client == null) return;
        final PacketWriter writer = client.getPacketWriter();
        if (writer == null) return;
        final ServerPacket packet = writer.getServerPacket();
        if (packet == null) {
            client.getCallbacks().post(ServerPacketReadCompleteEvent.INSTANCE);
        } else {
            client.getCallbacks().post(ServerPacketReadStartedEvent.INSTANCE);
        }
    }
}