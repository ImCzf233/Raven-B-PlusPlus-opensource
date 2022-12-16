package keystrokesmod.client.module.modules.movement;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.utils.*;
import net.minecraft.util.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import keystrokesmod.client.module.modules.world.*;
import java.util.*;

public class SlyPort extends Module
{
    public static DescriptionSetting f;
    public static SliderSetting r;
    public static TickSetting b;
    public static TickSetting d;
    public static TickSetting e;
    private final boolean s = false;
    
    public SlyPort() {
        super("SlyPort", ModuleCategory.movement);
        this.registerSetting(SlyPort.f = new DescriptionSetting("Teleport behind enemies."));
        this.registerSetting(SlyPort.r = new SliderSetting("Range", 6.0, 2.0, 15.0, 1.0));
        this.registerSetting(SlyPort.e = new TickSetting("Aim", true));
        this.registerSetting(SlyPort.b = new TickSetting("Play sound", true));
        this.registerSetting(SlyPort.d = new TickSetting("Players only", true));
    }
    
    @Override
    public void onEnable() {
        final Entity en = this.ge();
        if (en != null) {
            this.tp(en);
        }
        this.disable();
    }
    
    private void tp(final Entity en) {
        if (SlyPort.b.isToggled()) {
            SlyPort.mc.field_71439_g.func_85030_a("mob.endermen.portal", 1.0f, 1.0f);
        }
        final Vec3 vec = en.func_70040_Z();
        final double x = en.field_70165_t - vec.field_72450_a * 2.5;
        final double z = en.field_70161_v - vec.field_72449_c * 2.5;
        SlyPort.mc.field_71439_g.func_70107_b(x, SlyPort.mc.field_71439_g.field_70163_u, z);
        if (SlyPort.e.isToggled()) {
            Utils.Player.aim(en, 0.0f, false);
        }
    }
    
    private Entity ge() {
        Entity en = null;
        final double r = Math.pow(SlyPort.r.getInput(), 2.0);
        double dist = r + 1.0;
        for (final Entity ent : SlyPort.mc.field_71441_e.field_72996_f) {
            if (ent != SlyPort.mc.field_71439_g && ent instanceof EntityLivingBase && ((EntityLivingBase)ent).field_70725_aQ == 0 && (!SlyPort.d.isToggled() || ent instanceof EntityPlayer)) {
                if (AntiBot.bot(ent)) {
                    continue;
                }
                final double d = SlyPort.mc.field_71439_g.func_70068_e(ent);
                if (d > r || dist < d) {
                    continue;
                }
                dist = d;
                en = ent;
            }
        }
        return en;
    }
}
