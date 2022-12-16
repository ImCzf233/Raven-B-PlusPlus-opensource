package keystrokesmod.client.event.impl;

import keystrokesmod.client.event.types.*;

public class ForgeEvent extends Event
{
    private final net.minecraftforge.fml.common.eventhandler.Event event;
    
    public ForgeEvent(final net.minecraftforge.fml.common.eventhandler.Event event) {
        this.event = event;
    }
    
    public net.minecraftforge.fml.common.eventhandler.Event getEvent() {
        return this.event;
    }
}
