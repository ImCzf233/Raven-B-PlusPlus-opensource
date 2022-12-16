package keystrokesmod.client.module.modules.movement;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.main.*;
import keystrokesmod.client.utils.*;
import keystrokesmod.client.event.impl.*;
import com.google.common.eventbus.*;

public class Boost extends Module
{
    public static DescriptionSetting c;
    public static SliderSetting a;
    public static SliderSetting b;
    private int i;
    private boolean t;
    
    public Boost() {
        super("Boost", ModuleCategory.movement);
        this.registerSetting(Boost.c = new DescriptionSetting("20 ticks are in 1 second"));
        this.registerSetting(Boost.a = new SliderSetting("Multiplier", 2.0, 1.0, 3.0, 0.05));
        this.registerSetting(Boost.b = new SliderSetting("Time (ticks)", 15.0, 1.0, 80.0, 1.0));
    }
    
    @Override
    public void onEnable() {
        final Module timer = Raven.moduleManager.getModuleByClazz(Timer.class);
        if (timer != null && timer.isEnabled()) {
            this.t = true;
            timer.disable();
        }
    }
    
    @Override
    public void onDisable() {
        this.i = 0;
        if (Utils.Client.getTimer().field_74278_d != 1.0f) {
            Utils.Client.resetTimer();
        }
        if (this.t) {
            final Module timer = Raven.moduleManager.getModuleByClazz(Timer.class);
            if (timer != null) {
                timer.enable();
            }
        }
        this.t = false;
    }
    
    @Subscribe
    public void onTick(final TickEvent e) {
        if (this.i == 0) {
            this.i = Boost.mc.field_71439_g.field_70173_aa;
        }
        Utils.Client.getTimer().field_74278_d = (float)Boost.a.getInput();
        if (this.i == Boost.mc.field_71439_g.field_70173_aa - Boost.b.getInput()) {
            Utils.Client.resetTimer();
            this.disable();
        }
    }
}
