package keystrokesmod.client.module.modules.player;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.utils.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import net.minecraft.client.entity.*;
import org.lwjgl.input.*;
import com.google.common.eventbus.*;
import keystrokesmod.client.event.impl.*;
import java.awt.*;
import net.minecraftforge.client.event.*;

public class Freecam extends Module
{
    public static SliderSetting a;
    public static TickSetting b;
    private final double toRad = 0.017453292519943295;
    public static EntityOtherPlayerMP en;
    private int[] lcc;
    private final float[] sAng;
    
    public Freecam() {
        super("Freecam", ModuleCategory.player);
        this.lcc = new int[] { Integer.MAX_VALUE, 0 };
        this.sAng = new float[] { 0.0f, 0.0f };
        this.registerSetting(Freecam.a = new SliderSetting("Speed", 2.5, 0.5, 10.0, 0.5));
        this.registerSetting(Freecam.b = new TickSetting("Disable on damage", true));
    }
    
    @Override
    public void onEnable() {
        if (!Utils.Player.isPlayerInGame()) {
            return;
        }
        if (!Freecam.mc.field_71439_g.field_70122_E) {
            this.disable();
        }
        else {
            (Freecam.en = new EntityOtherPlayerMP((World)Freecam.mc.field_71441_e, Freecam.mc.field_71439_g.func_146103_bH())).func_82149_j((Entity)Freecam.mc.field_71439_g);
            this.sAng[0] = (Freecam.en.field_70759_as = Freecam.mc.field_71439_g.field_70759_as);
            this.sAng[1] = Freecam.mc.field_71439_g.field_70125_A;
            Freecam.en.func_70016_h(0.0, 0.0, 0.0);
            Freecam.en.func_82142_c(true);
            Freecam.mc.field_71441_e.func_73027_a(-8008, (Entity)Freecam.en);
            Freecam.mc.func_175607_a((Entity)Freecam.en);
        }
    }
    
    @Override
    public void onDisable() {
        if (Freecam.en != null) {
            Freecam.mc.func_175607_a((Entity)Freecam.mc.field_71439_g);
            final EntityPlayerSP field_71439_g = Freecam.mc.field_71439_g;
            final EntityPlayerSP field_71439_g2 = Freecam.mc.field_71439_g;
            final float n = this.sAng[0];
            field_71439_g2.field_70759_as = n;
            field_71439_g.field_70177_z = n;
            Freecam.mc.field_71439_g.field_70125_A = this.sAng[1];
            Freecam.mc.field_71441_e.func_72900_e((Entity)Freecam.en);
            Freecam.en = null;
        }
        this.lcc = new int[] { Integer.MAX_VALUE, 0 };
        final int rg = 1;
        final int x = Freecam.mc.field_71439_g.field_70176_ah;
        final int z = Freecam.mc.field_71439_g.field_70164_aj;
        for (int x2 = -1; x2 <= 1; ++x2) {
            for (int z2 = -1; z2 <= 1; ++z2) {
                final int a = x + x2;
                final int b = z + z2;
                Freecam.mc.field_71441_e.func_147458_c(a * 16, 0, b * 16, a * 16 + 15, 256, b * 16 + 15);
            }
        }
    }
    
    @Subscribe
    public void onTick(final TickEvent e) {
        if (!Utils.Player.isPlayerInGame() || Freecam.en == null) {
            return;
        }
        if (Freecam.b.isToggled() && Freecam.mc.field_71439_g.field_70737_aN != 0) {
            this.disable();
        }
        else {
            Freecam.mc.field_71439_g.func_70031_b(false);
            Freecam.mc.field_71439_g.field_70701_bs = 0.0f;
            Freecam.mc.field_71439_g.field_70702_br = 0.0f;
            final EntityOtherPlayerMP en = Freecam.en;
            final EntityOtherPlayerMP en2 = Freecam.en;
            final float field_70177_z = Freecam.mc.field_71439_g.field_70177_z;
            en2.field_70759_as = field_70177_z;
            en.field_70177_z = field_70177_z;
            Freecam.en.field_70125_A = Freecam.mc.field_71439_g.field_70125_A;
            final double s = 0.215 * Freecam.a.getInput();
            if (Keyboard.isKeyDown(Freecam.mc.field_71474_y.field_74351_w.func_151463_i())) {
                final double rad = Freecam.en.field_70759_as * 0.017453292519943295;
                final double dx = -1.0 * Math.sin(rad) * s;
                final double dz = Math.cos(rad) * s;
                final EntityOtherPlayerMP en3;
                EntityOtherPlayerMP var10000 = en3 = Freecam.en;
                en3.field_70165_t += dx;
                final EntityOtherPlayerMP en4;
                var10000 = (en4 = Freecam.en);
                en4.field_70161_v += dz;
            }
            if (Keyboard.isKeyDown(Freecam.mc.field_71474_y.field_74368_y.func_151463_i())) {
                final double rad = Freecam.en.field_70759_as * 0.017453292519943295;
                final double dx = -1.0 * Math.sin(rad) * s;
                final double dz = Math.cos(rad) * s;
                final EntityOtherPlayerMP en5;
                EntityOtherPlayerMP var10000 = en5 = Freecam.en;
                en5.field_70165_t -= dx;
                final EntityOtherPlayerMP en6;
                var10000 = (en6 = Freecam.en);
                en6.field_70161_v -= dz;
            }
            if (Keyboard.isKeyDown(Freecam.mc.field_71474_y.field_74370_x.func_151463_i())) {
                final double rad = (Freecam.en.field_70759_as - 90.0f) * 0.017453292519943295;
                final double dx = -1.0 * Math.sin(rad) * s;
                final double dz = Math.cos(rad) * s;
                final EntityOtherPlayerMP en7;
                EntityOtherPlayerMP var10000 = en7 = Freecam.en;
                en7.field_70165_t += dx;
                final EntityOtherPlayerMP en8;
                var10000 = (en8 = Freecam.en);
                en8.field_70161_v += dz;
            }
            if (Keyboard.isKeyDown(Freecam.mc.field_71474_y.field_74366_z.func_151463_i())) {
                final double rad = (Freecam.en.field_70759_as + 90.0f) * 0.017453292519943295;
                final double dx = -1.0 * Math.sin(rad) * s;
                final double dz = Math.cos(rad) * s;
                final EntityOtherPlayerMP en9;
                EntityOtherPlayerMP var10000 = en9 = Freecam.en;
                en9.field_70165_t += dx;
                final EntityOtherPlayerMP en10;
                var10000 = (en10 = Freecam.en);
                en10.field_70161_v += dz;
            }
            if (Keyboard.isKeyDown(Freecam.mc.field_71474_y.field_74314_A.func_151463_i())) {
                final EntityOtherPlayerMP en11;
                final EntityOtherPlayerMP var10000 = en11 = Freecam.en;
                en11.field_70163_u += 0.93 * s;
            }
            if (Keyboard.isKeyDown(Freecam.mc.field_71474_y.field_74311_E.func_151463_i())) {
                final EntityOtherPlayerMP en12;
                final EntityOtherPlayerMP var10000 = en12 = Freecam.en;
                en12.field_70163_u -= 0.93 * s;
            }
            Freecam.mc.field_71439_g.func_70095_a(false);
            if (this.lcc[0] != Integer.MAX_VALUE && (this.lcc[0] != Freecam.en.field_70176_ah || this.lcc[1] != Freecam.en.field_70164_aj)) {
                final int x = Freecam.en.field_70176_ah;
                final int z = Freecam.en.field_70164_aj;
                Freecam.mc.field_71441_e.func_147458_c(x * 16, 0, z * 16, x * 16 + 15, 256, z * 16 + 15);
            }
            this.lcc[0] = Freecam.en.field_70176_ah;
            this.lcc[1] = Freecam.en.field_70164_aj;
        }
    }
    
    @Subscribe
    public void onForgeEvent(final ForgeEvent fe) {
        if (fe.getEvent() instanceof RenderWorldLastEvent) {
            if (Utils.Player.isPlayerInGame()) {
                final EntityPlayerSP field_71439_g = Freecam.mc.field_71439_g;
                final EntityPlayerSP field_71439_g2 = Freecam.mc.field_71439_g;
                final float n = 700.0f;
                field_71439_g2.field_71164_i = n;
                field_71439_g.field_71155_g = n;
                Utils.HUD.drawBoxAroundEntity((Entity)Freecam.mc.field_71439_g, 1, 0.0, 0.0, Color.green.getRGB(), false);
                Utils.HUD.drawBoxAroundEntity((Entity)Freecam.mc.field_71439_g, 2, 0.0, 0.0, Color.green.getRGB(), false);
            }
        }
        else if (fe.getEvent() instanceof MouseEvent) {
            final MouseEvent e = (MouseEvent)fe.getEvent();
            if (Utils.Player.isPlayerInGame() && e.button != -1) {
                e.setCanceled(true);
            }
        }
    }
}
