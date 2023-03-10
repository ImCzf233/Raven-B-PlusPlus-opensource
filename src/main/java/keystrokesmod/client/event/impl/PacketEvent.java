package keystrokesmod.client.event.impl;

import keystrokesmod.client.event.types.*;
import net.minecraft.network.*;
import keystrokesmod.client.event.*;

public class PacketEvent extends CancellableEvent implements IEventDirection
{
    private final Packet<?> packet;
    private final EventDirection direction;
    
    public PacketEvent(final Packet<?> packet, final EventDirection direction) {
        this.packet = packet;
        this.direction = direction;
    }
    
    public <T extends Packet<?>> T getPacket() {
        return (T)this.packet;
    }
    
    @Override
    public EventDirection getDirection() {
        return this.direction;
    }
}
