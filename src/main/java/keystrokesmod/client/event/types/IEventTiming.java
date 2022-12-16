package keystrokesmod.client.event.types;

import keystrokesmod.client.event.*;

public interface IEventTiming
{
    EventTiming getTiming();
    
    default boolean isPre() {
        return this.getTiming() == EventTiming.PRE;
    }
    
    default boolean isPost() {
        return this.getTiming() == EventTiming.POST;
    }
}
