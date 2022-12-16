package keystrokesmod.client.module.modules.movement;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.event.impl.*;
import keystrokesmod.client.utils.*;
import net.minecraft.client.entity.*;
import com.google.common.eventbus.*;

public class LegitSpeed extends Module
{
    public static TickSetting speed;
    public static TickSetting fastFall;
    public static TickSetting legitStrafe;
    public static SliderSetting speedInc;
    
    public LegitSpeed() {
        super("LegitSpeed", ModuleCategory.movement);
        this.registerSetting(LegitSpeed.speed = new TickSetting("Increase Speed", true));
        this.registerSetting(LegitSpeed.speedInc = new SliderSetting("Speed", 1.12, 1.0, 1.4, 0.01));
        this.registerSetting(LegitSpeed.fastFall = new TickSetting("Fast Fall", false));
        this.registerSetting(LegitSpeed.legitStrafe = new TickSetting("Legit Strafe", false));
    }
    
    @Subscribe
    public void onMoveInput(final MoveInputEvent e) {
        if (LegitSpeed.speed.isToggled()) {
            e.setFriction((float)(e.getFriction() * LegitSpeed.speedInc.getInput()));
        }
        if (LegitSpeed.fastFall.isToggled() && LegitSpeed.mc.field_71439_g.field_70143_R > 1.5) {
            final EntityPlayerSP field_71439_g = LegitSpeed.mc.field_71439_g;
            field_71439_g.field_70181_x *= 1.075;
        }
        if (LegitSpeed.legitStrafe.isToggled() && !LegitSpeed.mc.field_71439_g.field_70122_E && (e.getStrafe() != 0.0f || e.getForward() != 0.0f)) {
            e.setYaw(Utils.Player.getStrafeYaw(e.getForward(), e.getStrafe()));
            e.setForward(1.0f);
            e.setStrafe(0.0f);
        }
    }
}
