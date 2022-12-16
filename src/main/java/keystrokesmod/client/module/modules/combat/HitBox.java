package keystrokesmod.client.module.modules.combat;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.main.*;
import keystrokesmod.client.module.modules.world.*;
import java.awt.*;
import net.minecraft.entity.*;
import keystrokesmod.client.utils.*;
import net.minecraft.util.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.*;

public class HitBox extends Module
{
    public static SliderSetting a;
    public static TickSetting b;
    private static MovingObjectPosition mv;
    
    public HitBox() {
        super("HitBox", ModuleCategory.combat);
        this.registerSetting(new DescriptionSetting("Changed from multiplier to extra blocks!"));
        this.registerSetting(HitBox.a = new SliderSetting("Extra Blocks", 0.2, 0.05, 2.0, 0.05));
        this.registerSetting(HitBox.b = new TickSetting("Vertical", false));
    }
    
    public static double exp(final Entity en) {
        final Module hitBox = Raven.moduleManager.getModuleByClazz(HitBox.class);
        return (hitBox != null && hitBox.isEnabled() && !AntiBot.bot(en)) ? HitBox.a.getInput() : 0.0;
    }
    
    private void rh(final Entity e, final Color c) {
        if (e instanceof EntityLivingBase) {
            final double x = e.field_70142_S + (e.field_70165_t - e.field_70142_S) * Utils.Client.getTimer().field_74281_c - HitBox.mc.func_175598_ae().field_78730_l;
            final double y = e.field_70137_T + (e.field_70163_u - e.field_70137_T) * Utils.Client.getTimer().field_74281_c - HitBox.mc.func_175598_ae().field_78731_m;
            final double z = e.field_70136_U + (e.field_70161_v - e.field_70136_U) * Utils.Client.getTimer().field_74281_c - HitBox.mc.func_175598_ae().field_78728_n;
            final float ex = (float)(e.func_70111_Y() * HitBox.a.getInput());
            final AxisAlignedBB bbox = e.func_174813_aQ().func_72314_b((double)ex, (double)ex, (double)ex);
            final AxisAlignedBB axis = new AxisAlignedBB(bbox.field_72340_a - e.field_70165_t + x, bbox.field_72338_b - e.field_70163_u + y, bbox.field_72339_c - e.field_70161_v + z, bbox.field_72336_d - e.field_70165_t + x, bbox.field_72337_e - e.field_70163_u + y, bbox.field_72334_f - e.field_70161_v + z);
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(3042);
            GL11.glDisable(3553);
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            GL11.glLineWidth(2.0f);
            GL11.glColor3d((double)c.getRed(), (double)c.getGreen(), (double)c.getBlue());
            RenderGlobal.func_181561_a(axis);
            GL11.glEnable(3553);
            GL11.glEnable(2929);
            GL11.glDepthMask(true);
            GL11.glDisable(3042);
        }
    }
}
