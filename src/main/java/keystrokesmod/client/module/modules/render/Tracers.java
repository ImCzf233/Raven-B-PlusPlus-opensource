package keystrokesmod.client.module.modules.render;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import com.google.common.eventbus.*;
import keystrokesmod.client.event.impl.*;
import net.minecraftforge.client.event.*;
import keystrokesmod.client.utils.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;
import java.awt.*;
import java.util.*;

public class Tracers extends Module
{
    public static TickSetting a;
    public static RGBSetting rgb;
    public static TickSetting e;
    public static TickSetting o;
    public static SliderSetting f;
    public static SliderSetting sl;
    private boolean g;
    private int rgb_c;
    
    public Tracers() {
        super("Tracers", ModuleCategory.render);
        this.registerSetting(Tracers.a = new TickSetting("Show invis", true));
        this.registerSetting(Tracers.f = new SliderSetting("Line Width", 1.0, 1.0, 5.0, 1.0));
        this.registerSetting(Tracers.sl = new SliderSetting("Distance", 1.0, 1.0, 512.0, 1.0));
        this.registerSetting(Tracers.rgb = new RGBSetting("Color:", 0, 255, 0));
        this.registerSetting(Tracers.e = new TickSetting("Rainbow", false));
        this.registerSetting(Tracers.o = new TickSetting("Redshift w distance", false));
    }
    
    @Override
    public void onEnable() {
        this.g = Tracers.mc.field_71474_y.field_74336_f;
        if (this.g) {
            Tracers.mc.field_71474_y.field_74336_f = false;
        }
    }
    
    @Override
    public void onDisable() {
        Tracers.mc.field_71474_y.field_74336_f = this.g;
    }
    
    @Subscribe
    public void onTick(final TickEvent e) {
        if (Tracers.mc.field_71474_y.field_74336_f) {
            Tracers.mc.field_71474_y.field_74336_f = false;
        }
    }
    
    @Override
    public void guiUpdate() {
        this.rgb_c = Tracers.rgb.getRGB();
    }
    
    @Subscribe
    public void onForgeEvent(final ForgeEvent fe) {
        if (fe.getEvent() instanceof RenderWorldLastEvent && Utils.Player.isPlayerInGame()) {
            final int rgb = Tracers.e.isToggled() ? Utils.Client.rainbowDraw(2L, 0L) : this.rgb_c;
            for (final EntityPlayer en : Tracers.mc.field_71441_e.field_73010_i) {
                if (en != Tracers.mc.field_71439_g && en.field_70725_aQ == 0 && (Tracers.a.isToggled() || !en.func_82150_aj())) {
                    if (Tracers.o.isToggled() && Tracers.mc.field_71439_g.func_70032_d((Entity)en) < 25.0f) {
                        final int red = (int)(Math.abs(Tracers.mc.field_71439_g.func_70032_d((Entity)en) - 25.0f) * 10.0f);
                        final int green = Math.abs(red - 255);
                        final int rgbs = new Color(red, green, Tracers.rgb.getBlue()).getRGB();
                        Utils.Player.sendMessageToSelf(red + "");
                        Utils.HUD.dtl((Entity)en, rgbs, (float)Tracers.f.getInput());
                    }
                    else {
                        Utils.HUD.dtl((Entity)en, rgb, (float)Tracers.f.getInput());
                    }
                }
            }
        }
    }
}
