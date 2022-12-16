package keystrokesmod.client.module.modules.movement;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.event.impl.*;
import keystrokesmod.client.clickgui.raven.*;
import keystrokesmod.client.utils.*;
import com.google.common.eventbus.*;

public class Timer extends Module
{
    public static SliderSetting a;
    public static TickSetting b;
    
    public Timer() {
        super("Timer", ModuleCategory.movement);
        Timer.a = new SliderSetting("Speed", 1.0, 0.5, 2.5, 0.01);
        Timer.b = new TickSetting("Strafe only", false);
        this.registerSetting(Timer.a);
        this.registerSetting(Timer.b);
    }
    
    @Subscribe
    public void onTick(final TickEvent e) {
        if (!(Timer.mc.field_71462_r instanceof ClickGui)) {
            if (Timer.b.isToggled() && Timer.mc.field_71439_g.field_70702_br == 0.0f) {
                Utils.Client.resetTimer();
                return;
            }
            Utils.Client.getTimer().field_74278_d = (float)Timer.a.getInput();
        }
        else {
            Utils.Client.resetTimer();
        }
    }
    
    @Override
    public void onDisable() {
        Utils.Client.resetTimer();
    }
}
