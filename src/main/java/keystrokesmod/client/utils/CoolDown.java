package keystrokesmod.client.utils;

public class CoolDown
{
    private long start;
    private long lasts;
    private boolean checkedFinish;
    
    public CoolDown(final long lasts) {
        this.lasts = lasts;
    }
    
    public void start() {
        this.start = System.currentTimeMillis();
        this.checkedFinish = false;
    }
    
    public boolean hasFinished() {
        return System.currentTimeMillis() >= this.start + this.lasts;
    }
    
    public boolean firstFinish() {
        return System.currentTimeMillis() >= this.start + this.lasts && !this.checkedFinish && (this.checkedFinish = true);
    }
    
    public void setCooldown(final long time) {
        this.lasts = time;
    }
    
    public long getCooldownTime() {
        return this.lasts;
    }
    
    public long getElapsedTime() {
        return System.currentTimeMillis() - this.start;
    }
    
    public long getTimeLeft() {
        return this.lasts - (System.currentTimeMillis() - this.start);
    }
}
