package keystrokesmod.client.event.impl;

import keystrokesmod.client.event.types.*;
import keystrokesmod.client.event.*;

public class UpdateEvent extends Event implements IEventTiming
{
    private final EventTiming timing;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private boolean onGround;
    
    public UpdateEvent(final EventTiming timing, final double x, final double y, final double z, final float yaw, final float pitch, final boolean onGround) {
        this.timing = timing;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }
    
    public static UpdateEvent convertPost(final UpdateEvent e) {
        return new UpdateEvent(EventTiming.POST, e.getX(), e.getY(), e.getZ(), e.getYaw(), e.getPitch(), e.isOnGround());
    }
    
    @Override
    public EventTiming getTiming() {
        return this.timing;
    }
    
    public double getX() {
        return this.x;
    }
    
    public void setX(final double x) {
        this.x = x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public void setY(final double y) {
        this.y = y;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public void setZ(final double z) {
        this.z = z;
    }
    
    public float getYaw() {
        return this.yaw;
    }
    
    public void setYaw(final float yaw) {
        this.yaw = yaw;
    }
    
    public float getPitch() {
        return this.pitch;
    }
    
    public void setPitch(final float pitch) {
        this.pitch = pitch;
    }
    
    public boolean isOnGround() {
        return this.onGround;
    }
    
    public void setOnGround(final boolean onGround) {
        this.onGround = onGround;
    }
}
