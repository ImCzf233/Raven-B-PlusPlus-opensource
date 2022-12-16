package keystrokesmod.client.module.modules.player;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.event.impl.*;
import keystrokesmod.client.main.*;
import keystrokesmod.client.module.modules.movement.*;
import net.minecraft.client.entity.*;
import com.google.common.eventbus.*;

public class FallSpeed extends Module
{
    public static DescriptionSetting dc;
    public static SliderSetting a;
    public static TickSetting b;
    
    public FallSpeed() {
        super("FallSpeed", ModuleCategory.player);
        this.registerSetting(FallSpeed.dc = new DescriptionSetting("Vanilla max: 3.92"));
        this.registerSetting(FallSpeed.a = new SliderSetting("Motion", 5.0, 0.0, 8.0, 0.1));
        this.registerSetting(FallSpeed.b = new TickSetting("Disable XZ motion", true));
    }
    
    @Subscribe
    public void onTick(final TickEvent e) {
        if (FallSpeed.mc.field_71439_g.field_70143_R >= 2.5) {
            final Module fly = Raven.moduleManager.getModuleByClazz(Fly.class);
            final Module noFall = Raven.moduleManager.getModuleByClazz(NoFall.class);
            if ((fly != null && fly.isEnabled()) || (noFall != null && noFall.isEnabled())) {
                return;
            }
            if (FallSpeed.mc.field_71439_g.field_71075_bZ.field_75098_d || FallSpeed.mc.field_71439_g.field_71075_bZ.field_75100_b) {
                return;
            }
            if (FallSpeed.mc.field_71439_g.func_70617_f_() || FallSpeed.mc.field_71439_g.func_70090_H() || FallSpeed.mc.field_71439_g.func_180799_ab()) {
                return;
            }
            FallSpeed.mc.field_71439_g.field_70181_x = -FallSpeed.a.getInput();
            if (FallSpeed.b.isToggled()) {
                final EntityPlayerSP field_71439_g = FallSpeed.mc.field_71439_g;
                final EntityPlayerSP field_71439_g2 = FallSpeed.mc.field_71439_g;
                final double n = 0.0;
                field_71439_g2.field_70179_y = n;
                field_71439_g.field_70159_w = n;
            }
        }
    }
}
