package keystrokesmod.client.event.types;

import keystrokesmod.client.event.*;

public interface IEventDirection
{
    EventDirection getDirection();
    
    default boolean isIncoming() {
        return this.getDirection() == EventDirection.INCOMING;
    }
    
    default boolean isOutgoing() {
        return this.getDirection() == EventDirection.OUTGOING;
    }
}
