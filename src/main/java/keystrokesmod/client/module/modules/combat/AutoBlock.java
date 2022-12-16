package keystrokesmod.client.module.modules.combat;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.event.impl.*;
import keystrokesmod.client.utils.*;
import org.lwjgl.input.*;
import com.google.common.eventbus.*;
import net.minecraft.client.settings.*;

public class AutoBlock extends Module
{
    public static DoubleSliderSetting duration;
    public static DoubleSliderSetting distance;
    public static SliderSetting chance;
    private boolean engaged;
    private final CoolDown engagedTime;
    
    public AutoBlock() {
        super("AutoBlock", ModuleCategory.combat);
        this.engagedTime = new CoolDown(0L);
        this.registerSetting(AutoBlock.duration = new DoubleSliderSetting("Block duration (MS)", 20.0, 100.0, 1.0, 500.0, 1.0));
        this.registerSetting(AutoBlock.distance = new DoubleSliderSetting("Distance to player (blocks)", 0.0, 3.0, 0.0, 6.0, 0.01));
        this.registerSetting(AutoBlock.chance = new SliderSetting("Chance %", 100.0, 0.0, 100.0, 1.0));
    }
    
    @Subscribe
    public void onRender(final Render2DEvent e) {
        if (!Utils.Player.isPlayerInGame() || !Utils.Player.isPlayerHoldingSword()) {
            return;
        }
        if (this.engaged) {
            if ((this.engagedTime.hasFinished() || !Mouse.isButtonDown(0)) && AutoBlock.duration.getInputMin() <= this.engagedTime.getElapsedTime()) {
                this.engaged = false;
                release();
            }
            return;
        }
        if (Mouse.isButtonDown(0) && AutoBlock.mc.field_71476_x != null && AutoBlock.mc.field_71476_x.field_72308_g != null && AutoBlock.mc.field_71439_g.func_70032_d(AutoBlock.mc.field_71476_x.field_72308_g) >= AutoBlock.distance.getInputMin() && AutoBlock.mc.field_71476_x.field_72308_g != null && AutoBlock.mc.field_71439_g.func_70032_d(AutoBlock.mc.field_71476_x.field_72308_g) <= AutoBlock.distance.getInputMax() && (AutoBlock.chance.getInput() == 100.0 || Math.random() <= AutoBlock.chance.getInput() / 100.0)) {
            this.engaged = true;
            this.engagedTime.setCooldown((long)AutoBlock.duration.getInputMax());
            this.engagedTime.start();
            press();
        }
    }
    
    private static void release() {
        final int key = AutoBlock.mc.field_71474_y.field_74313_G.func_151463_i();
        KeyBinding.func_74510_a(key, false);
        Utils.Client.setMouseButtonState(1, false);
    }
    
    private static void press() {
        final int key = AutoBlock.mc.field_71474_y.field_74313_G.func_151463_i();
        KeyBinding.func_74510_a(key, true);
        KeyBinding.func_74507_a(key);
        Utils.Client.setMouseButtonState(1, true);
    }
}
