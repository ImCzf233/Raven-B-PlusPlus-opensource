package keystrokesmod.client.event.impl;

import keystrokesmod.client.event.types.*;

public class MoveInputEvent extends Event
{
    private float strafe;
    private float forward;
    private float friction;
    private float yaw;
    
    public MoveInputEvent(final float strafe, final float forward, final float friction, final float yaw) {
        this.strafe = strafe;
        this.forward = forward;
        this.friction = friction;
        this.yaw = yaw;
    }
    
    public float getStrafe() {
        return this.strafe;
    }
    
    public void setStrafe(final float strafe) {
        this.strafe = strafe;
    }
    
    public float getForward() {
        return this.forward;
    }
    
    public void setForward(final float forward) {
        this.forward = forward;
    }
    
    public float getFriction() {
        return this.friction;
    }
    
    public void setFriction(final float friction) {
        this.friction = friction;
    }
    
    public float getYaw() {
        return this.yaw;
    }
    
    public void setYaw(final float yaw) {
        this.yaw = yaw;
    }
}
