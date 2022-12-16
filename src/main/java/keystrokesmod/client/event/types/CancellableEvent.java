package keystrokesmod.client.event.types;

public class CancellableEvent
{
    private boolean cancelled;
    
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    public void cancel() {
        this.setCancelled(false);
    }
}
