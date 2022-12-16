package keystrokesmod.client.utils;

import net.minecraft.network.*;
import net.minecraft.client.*;
import java.util.*;

public class PacketUtils
{
    public static final List<Packet<?>> silentPackets;
    
    public static void sendPacket(final Packet<?> packet) {
        Minecraft.func_71410_x().func_147114_u().func_147297_a((Packet)packet);
    }
    
    public static void sendPacketSilent(final Packet<?> packet) {
        PacketUtils.silentPackets.add(packet);
        Minecraft.func_71410_x().func_147114_u().func_147297_a((Packet)packet);
    }
    
    static {
        silentPackets = new ArrayList<Packet<?>>();
    }
}
