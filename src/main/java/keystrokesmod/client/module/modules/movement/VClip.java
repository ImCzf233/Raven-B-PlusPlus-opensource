package keystrokesmod.client.module.modules.movement;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;

public class VClip extends Module
{
    public static SliderSetting a;
    
    public VClip() {
        super("VClip", ModuleCategory.movement);
        this.registerSetting(VClip.a = new SliderSetting("Distace", 2.0, -10.0, 10.0, 0.5));
    }
    
    @Override
    public void onEnable() {
        if (VClip.a.getInput() != 0.0) {
            VClip.mc.field_71439_g.func_70107_b(VClip.mc.field_71439_g.field_70165_t, VClip.mc.field_71439_g.field_70163_u + VClip.a.getInput(), VClip.mc.field_71439_g.field_70161_v);
        }
        this.disable();
    }
}
