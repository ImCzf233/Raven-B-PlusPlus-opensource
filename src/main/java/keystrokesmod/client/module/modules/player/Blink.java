package keystrokesmod.client.module.modules.player;

import keystrokesmod.client.module.*;
import net.minecraft.network.*;
import keystrokesmod.client.event.impl.*;
import com.google.common.eventbus.*;
import java.util.*;

public class Blink extends Module
{
    private ArrayList<Packet> packets;
    
    public Blink() {
        super("Blink", ModuleCategory.player);
        this.packets = new ArrayList<Packet>();
    }
    
    @Subscribe
    public void packetEvent(final PacketEvent p) {
        this.packets.add(p.getPacket());
    }
    
    @Override
    public void onEnable() {
        this.packets.clear();
    }
    
    @Override
    public void onDisable() {
        for (final Packet packet : this.packets) {
            Blink.mc.func_147114_u().func_147297_a(packet);
        }
        this.packets.clear();
    }
}
