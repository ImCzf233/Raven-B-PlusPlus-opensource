package keystrokesmod.client.module.modules.render;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.utils.*;
import java.awt.*;
import keystrokesmod.client.event.impl.*;
import net.minecraftforge.client.event.*;
import keystrokesmod.client.main.*;
import net.minecraft.entity.player.*;
import keystrokesmod.client.module.modules.world.*;
import net.minecraft.entity.*;
import java.util.*;
import com.google.common.eventbus.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;

public class PlayerESP extends Module
{
    public static DescriptionSetting g;
    public static SliderSetting i;
    public static SliderSetting j;
    public static TickSetting d;
    public static TickSetting f;
    public static TickSetting h;
    public static TickSetting t1;
    public static TickSetting t2;
    public static TickSetting t3;
    public static TickSetting t4;
    public static TickSetting t5;
    public static TickSetting t6;
    public static TickSetting t7;
    public static RGBSetting rgb;
    private int rgb_c;
    
    public PlayerESP() {
        super("PlayerESP", ModuleCategory.render);
        this.registerSetting(PlayerESP.rgb = new RGBSetting("RGB", 0, 255, 0));
        this.registerSetting(PlayerESP.d = new TickSetting("Rainbow", false));
        this.registerSetting(PlayerESP.g = new DescriptionSetting("ESP Types"));
        this.registerSetting(PlayerESP.t3 = new TickSetting("2D", false));
        this.registerSetting(PlayerESP.t5 = new TickSetting("Arrow", false));
        this.registerSetting(PlayerESP.t1 = new TickSetting("Box", false));
        this.registerSetting(PlayerESP.t4 = new TickSetting("Health", true));
        this.registerSetting(PlayerESP.t6 = new TickSetting("Ring", false));
        this.registerSetting(PlayerESP.t2 = new TickSetting("Shaded", false));
        this.registerSetting(PlayerESP.i = new SliderSetting("Expand", 0.0, -0.3, 2.0, 0.1));
        this.registerSetting(PlayerESP.j = new SliderSetting("X-Shift", 0.0, -35.0, 10.0, 1.0));
        this.registerSetting(PlayerESP.f = new TickSetting("Show invis", true));
        this.registerSetting(PlayerESP.h = new TickSetting("Red on damage", true));
        this.registerSetting(PlayerESP.t7 = new TickSetting("Match Chestplate", false));
    }
    
    @Override
    public void onDisable() {
        Utils.HUD.ring_c = false;
    }
    
    @Override
    public void guiUpdate() {
        this.rgb_c = new Color(PlayerESP.rgb.getRed(), PlayerESP.rgb.getGreen(), PlayerESP.rgb.getBlue()).getRGB();
    }
    
    @Subscribe
    public void onForgeEvent(final ForgeEvent fe) {
        if (fe.getEvent() instanceof RenderWorldLastEvent && Utils.Player.isPlayerInGame()) {
            final int rgb = PlayerESP.d.isToggled() ? 0 : this.rgb_c;
            if (!Raven.debugger) {
                for (final EntityPlayer en : PlayerESP.mc.field_71441_e.field_73010_i) {
                    if (en != PlayerESP.mc.field_71439_g && en.field_70725_aQ == 0 && (PlayerESP.f.isToggled() || !en.func_82150_aj())) {
                        if (AntiBot.bot((Entity)en)) {
                            continue;
                        }
                        if (PlayerESP.t7.isToggled() && this.getColor(en.func_82169_q(2)) > 0) {
                            final int E = new Color(this.getColor(en.func_82169_q(2))).getRGB();
                            this.r((Entity)en, E);
                        }
                        else {
                            this.r((Entity)en, rgb);
                        }
                    }
                }
                return;
            }
            for (final Entity en2 : PlayerESP.mc.field_71441_e.field_72996_f) {
                if (en2 instanceof EntityLivingBase && en2 != PlayerESP.mc.field_71439_g) {
                    this.r(en2, rgb);
                }
            }
        }
    }
    
    public int getColor(final ItemStack stack) {
        if (stack == null) {
            return -1;
        }
        final NBTTagCompound nbttagcompound = stack.func_77978_p();
        if (nbttagcompound != null) {
            final NBTTagCompound nbttagcompound2 = nbttagcompound.func_74775_l("display");
            if (nbttagcompound2 != null && nbttagcompound2.func_150297_b("color", 3)) {
                return nbttagcompound2.func_74762_e("color");
            }
        }
        return -2;
    }
    
    private void r(final Entity en, final int rgb) {
        if (PlayerESP.t1.isToggled()) {
            Utils.HUD.drawBoxAroundEntity(en, 1, PlayerESP.i.getInput(), PlayerESP.j.getInput(), rgb, PlayerESP.h.isToggled());
        }
        if (PlayerESP.t2.isToggled()) {
            Utils.HUD.drawBoxAroundEntity(en, 2, PlayerESP.i.getInput(), PlayerESP.j.getInput(), rgb, PlayerESP.h.isToggled());
        }
        if (PlayerESP.t3.isToggled()) {
            Utils.HUD.drawBoxAroundEntity(en, 3, PlayerESP.i.getInput(), PlayerESP.j.getInput(), rgb, PlayerESP.h.isToggled());
        }
        if (PlayerESP.t4.isToggled()) {
            Utils.HUD.drawBoxAroundEntity(en, 4, PlayerESP.i.getInput(), PlayerESP.j.getInput(), rgb, PlayerESP.h.isToggled());
        }
        if (PlayerESP.t5.isToggled()) {
            Utils.HUD.drawBoxAroundEntity(en, 5, PlayerESP.i.getInput(), PlayerESP.j.getInput(), rgb, PlayerESP.h.isToggled());
        }
        if (PlayerESP.t6.isToggled()) {
            Utils.HUD.drawBoxAroundEntity(en, 6, PlayerESP.i.getInput(), PlayerESP.j.getInput(), rgb, PlayerESP.h.isToggled());
        }
    }
}
