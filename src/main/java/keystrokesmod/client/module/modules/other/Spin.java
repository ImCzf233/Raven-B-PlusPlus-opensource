package keystrokesmod.client.module.modules.other;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.event.impl.*;
import keystrokesmod.client.utils.*;
import java.util.*;
import com.google.common.eventbus.*;

public class Spin extends Module
{
    private SliderSetting time;
    private SliderSetting fov;
    private CoolDown cd;
    private ComboSetting mode;
    private float yaw;
    private float finalYaw;
    private float lastYaw;
    private float yawOffSet;
    
    public Spin() {
        super("Spin", ModuleCategory.other);
        this.cd = new CoolDown(1L);
        this.registerSetting(this.mode = new ComboSetting("Mode", (T)aimMode.SMOOTH));
        this.registerSetting(this.fov = new SliderSetting("fov", 30.0, -360.0, 360.0, 1.0));
        this.registerSetting(this.time = new SliderSetting("time (ms)", 200.0, 0.0, 1000.0, 1.0));
    }
    
    @Override
    public void onEnable() {
        this.cd.setCooldown((long)this.time.getInput());
        this.cd.start();
        this.yawOffSet = 0.0f;
        this.lastYaw = Spin.mc.field_71439_g.field_70177_z;
        this.yaw = Spin.mc.field_71439_g.field_70177_z;
        this.finalYaw = (float)(Spin.mc.field_71439_g.field_70177_z + this.fov.getInput());
    }
    
    @Subscribe
    public void onUpdate(final UpdateEvent e) {
        if (this.cd.hasFinished()) {
            this.disable();
            return;
        }
        final float timeleft = this.cd.getElapsedTime() / (float)this.cd.getCooldownTime();
        float percent = 0.0f;
        if (this.mode.getMode() == aimMode.LINEAR) {
            percent = timeleft;
            Utils.Player.sendMessageToSelf(" " + timeleft);
            this.yawOffSet += this.lastYaw - Spin.mc.field_71439_g.field_70177_z;
            Spin.mc.field_71439_g.field_70177_z = this.yaw + (this.finalYaw - this.yaw) * timeleft;
        }
        else if (this.mode.getMode() == aimMode.SMOOTH) {
            percent = (float)(0.5 * Math.sin(Math.toRadians(180.0f * (timeleft - 0.5f))) + 0.5);
            final int times = (int)(percent * 100.0f);
            final String[] arr = new String[times];
            Arrays.fill(arr, "l");
            Utils.Player.sendMessageToSelf(String.join("", (CharSequence[])arr));
            this.yawOffSet += Spin.mc.field_71439_g.field_70177_z - this.lastYaw;
            Spin.mc.field_71439_g.field_70177_z = this.yaw + (this.finalYaw - this.yaw) * percent;
        }
        this.lastYaw = Spin.mc.field_71439_g.field_70177_z;
    }
    
    public enum aimMode
    {
        SMOOTH, 
        LINEAR;
    }
}
