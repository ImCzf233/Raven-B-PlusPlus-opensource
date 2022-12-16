package keystrokesmod.client.module.modules.movement;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.event.impl.*;
import keystrokesmod.client.utils.*;
import net.minecraft.client.settings.*;
import net.minecraft.client.entity.*;
import com.google.common.eventbus.*;

public class SpeedTest extends Module
{
    private CoolDown coolDown;
    private SliderSetting delay;
    private SliderSetting stopPercent;
    
    public SpeedTest() {
        super("SpeedTest", ModuleCategory.movement);
        this.coolDown = new CoolDown(1L);
        this.registerSetting(this.delay = new SliderSetting("Delay", 20.0, 0.0, 300.0, 1.0));
        this.registerSetting(this.stopPercent = new SliderSetting("Stop Percent", 0.0, 0.0, 200.0, 1.0));
    }
    
    @Subscribe
    public void onRender2D(final Render2DEvent e) {
        if (!Utils.Player.isPlayerInGame()) {
            return;
        }
        if (SpeedTest.mc.field_71439_g.field_70122_E && this.coolDown.hasFinished()) {
            KeyBinding.func_74510_a(SpeedTest.mc.field_71474_y.field_74314_A.func_151463_i(), true);
            this.coolDown.setCooldown((long)this.delay.getInput());
            this.coolDown.start();
        }
        if (this.coolDown.firstFinish()) {
            final EntityPlayerSP field_71439_g = SpeedTest.mc.field_71439_g;
            field_71439_g.field_70181_x *= this.stopPercent.getInput() / 100.0;
        }
    }
    
    @Override
    public void onDisable() {
        KeyBinding.func_74510_a(SpeedTest.mc.field_71474_y.field_74314_A.func_151463_i(), false);
    }
}
