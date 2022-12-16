package keystrokesmod.client.module.modules.player;

import keystrokesmod.client.module.*;
import keystrokesmod.client.event.impl.*;
import keystrokesmod.client.utils.*;
import org.lwjgl.input.*;
import net.minecraft.client.settings.*;
import com.google.common.eventbus.*;

public class Parkour extends Module
{
    private final CoolDown cd;
    
    public Parkour() {
        super("Parkour", ModuleCategory.player);
        this.cd = new CoolDown(1L);
    }
    
    @Subscribe
    public void onTick(final TickEvent e) {
        if (!Utils.Player.isPlayerInGame()) {
            return;
        }
        if (!Keyboard.isKeyDown(Parkour.mc.field_71474_y.field_74314_A.func_151463_i()) && this.cd.firstFinish()) {
            KeyBinding.func_74510_a(Parkour.mc.field_71474_y.field_74314_A.func_151463_i(), false);
        }
        if (Parkour.mc.field_71439_g.field_70122_E && Utils.Player.playerOverAir() && (Parkour.mc.field_71439_g.field_70159_w != 0.0 || Parkour.mc.field_71439_g.field_70179_y != 0.0)) {
            KeyBinding.func_74510_a(Parkour.mc.field_71474_y.field_74314_A.func_151463_i(), true);
            this.cd.setCooldown(10L);
            this.cd.start();
        }
    }
}
