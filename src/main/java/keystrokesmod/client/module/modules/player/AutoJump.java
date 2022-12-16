package keystrokesmod.client.module.modules.player;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.event.impl.*;
import keystrokesmod.client.utils.*;
import net.minecraft.entity.*;
import com.google.common.eventbus.*;
import net.minecraft.client.settings.*;

public class AutoJump extends Module
{
    public static TickSetting b;
    private boolean c;
    
    public AutoJump() {
        super("AutoJump", ModuleCategory.player);
        this.registerSetting(AutoJump.b = new TickSetting("Cancel when shifting", true));
    }
    
    @Override
    public void onDisable() {
        this.ju(this.c = false);
    }
    
    @Subscribe
    public void onTick(final TickEvent e) {
        if (Utils.Player.isPlayerInGame()) {
            if (AutoJump.mc.field_71439_g.field_70122_E && (!AutoJump.b.isToggled() || !AutoJump.mc.field_71439_g.func_70093_af())) {
                if (AutoJump.mc.field_71441_e.func_72945_a((Entity)AutoJump.mc.field_71439_g, AutoJump.mc.field_71439_g.func_174813_aQ().func_72317_d(AutoJump.mc.field_71439_g.field_70159_w / 3.0, -1.0, AutoJump.mc.field_71439_g.field_70179_y / 3.0)).isEmpty()) {
                    this.ju(this.c = true);
                }
                else if (this.c) {
                    this.ju(this.c = false);
                }
            }
            else if (this.c) {
                this.ju(this.c = false);
            }
        }
    }
    
    private void ju(final boolean ju) {
        KeyBinding.func_74510_a(AutoJump.mc.field_71474_y.field_74314_A.func_151463_i(), ju);
    }
}
