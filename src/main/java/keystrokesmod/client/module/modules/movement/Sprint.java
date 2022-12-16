package keystrokesmod.client.module.modules.movement;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.event.impl.*;
import keystrokesmod.client.utils.*;
import keystrokesmod.client.main.*;
import keystrokesmod.client.module.modules.world.*;
import net.minecraft.client.settings.*;
import com.google.common.eventbus.*;

public class Sprint extends Module
{
    public static TickSetting multiDir;
    public static TickSetting ignoreBlindness;
    
    public Sprint() {
        super("Sprint", ModuleCategory.movement);
        this.registerSetting(Sprint.multiDir = new TickSetting("All Directions", false));
        this.registerSetting(Sprint.ignoreBlindness = new TickSetting("Ignore Blindness", false));
    }
    
    @Subscribe
    public void p(final TickEvent e) {
        KeyBinding.func_74510_a(Sprint.mc.field_71474_y.field_151444_V.func_151463_i(), Utils.Player.isPlayerInGame() && Sprint.mc.field_71415_G && (!Raven.moduleManager.getModuleByClazz(Scaffold.class).isEnabled() || Scaffold.sprint.isToggled()));
    }
}
