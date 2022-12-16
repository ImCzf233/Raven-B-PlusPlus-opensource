package keystrokesmod.client.utils;

public class MillisTimer
{
    private long currentMs;
    
    public MillisTimer() {
        this.reset();
    }
    
    public long lastReset() {
        return this.currentMs;
    }
    
    public boolean hasElapsed(final long milliseconds) {
        return this.elapsed() > milliseconds;
    }
    
    public long elapsed() {
        return System.currentTimeMillis() - this.currentMs;
    }
    
    public MillisTimer reset() {
        this.currentMs = System.currentTimeMillis();
        return this;
    }
}
