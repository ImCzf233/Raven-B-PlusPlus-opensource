package keystrokesmod.client.module.modules.movement;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import net.minecraft.entity.*;
import keystrokesmod.client.main.*;
import keystrokesmod.client.module.modules.combat.*;
import net.minecraft.client.entity.*;

public class KeepSprint extends Module
{
    public static DescriptionSetting a;
    public static DescriptionSetting a2;
    public static SliderSetting b;
    public static TickSetting c;
    public static TickSetting sprint;
    
    public KeepSprint() {
        super("KeepSprint", ModuleCategory.movement);
        this.registerSetting(KeepSprint.a = new DescriptionSetting("Default is 40% motion reduction"));
        this.registerSetting(KeepSprint.a2 = new DescriptionSetting("and stopping sprint."));
        this.registerSetting(KeepSprint.b = new SliderSetting("Slow %", 40.0, 0.0, 100.0, 1.0));
        this.registerSetting(KeepSprint.c = new TickSetting("Only reduce reach hits", false));
        this.registerSetting(KeepSprint.sprint = new TickSetting("Stop Sprint", true));
    }
    
    public static void slowdown(final Entity en) {
        final Module reach = Raven.moduleManager.getModuleByClazz(Reach.class);
        if (KeepSprint.c.isToggled() && reach != null && reach.isEnabled() && !KeepSprint.mc.field_71439_g.field_71075_bZ.field_75098_d) {
            final double dist = KeepSprint.mc.field_71476_x.field_72307_f.func_72438_d(KeepSprint.mc.func_175606_aa().func_174824_e(1.0f));
            double val;
            if (dist > 3.0) {
                val = (100.0 - (float)KeepSprint.b.getInput()) / 100.0;
            }
            else {
                val = 0.6;
            }
            final EntityPlayerSP field_71439_g = KeepSprint.mc.field_71439_g;
            field_71439_g.field_70159_w *= val;
            final EntityPlayerSP field_71439_g2 = KeepSprint.mc.field_71439_g;
            field_71439_g2.field_70179_y *= val;
        }
        else {
            final double dist = (100.0 - (float)KeepSprint.b.getInput()) / 100.0;
            final EntityPlayerSP field_71439_g3 = KeepSprint.mc.field_71439_g;
            field_71439_g3.field_70159_w *= dist;
            final EntityPlayerSP field_71439_g4 = KeepSprint.mc.field_71439_g;
            field_71439_g4.field_70179_y *= dist;
        }
        if (KeepSprint.sprint.isToggled()) {
            KeepSprint.mc.field_71439_g.func_70031_b(false);
        }
    }
}
