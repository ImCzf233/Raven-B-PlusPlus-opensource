package keystrokesmod.client.module.modules.movement;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.utils.*;

public class StopMotion extends Module
{
    public static TickSetting a;
    public static TickSetting b;
    public static TickSetting c;
    
    public StopMotion() {
        super("Stop Motion", ModuleCategory.movement);
        this.registerSetting(StopMotion.a = new TickSetting("Stop X", true));
        this.registerSetting(StopMotion.b = new TickSetting("Stop Y", true));
        this.registerSetting(StopMotion.c = new TickSetting("Stop Z", true));
    }
    
    @Override
    public void onEnable() {
        if (!Utils.Player.isPlayerInGame()) {
            this.disable();
            return;
        }
        if (StopMotion.a.isToggled()) {
            StopMotion.mc.field_71439_g.field_70159_w = 0.0;
        }
        if (StopMotion.b.isToggled()) {
            StopMotion.mc.field_71439_g.field_70181_x = 0.0;
        }
        if (StopMotion.c.isToggled()) {
            StopMotion.mc.field_71439_g.field_70179_y = 0.0;
        }
        this.disable();
    }
}
