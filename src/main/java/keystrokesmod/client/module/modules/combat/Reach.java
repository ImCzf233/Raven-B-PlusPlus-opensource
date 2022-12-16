package keystrokesmod.client.module.modules.combat;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.utils.*;

public class Reach extends Module
{
    public static DoubleSliderSetting reach;
    public static TickSetting weapon_only;
    public static TickSetting moving_only;
    public static TickSetting sprint_only;
    
    public Reach() {
        super("Reach", ModuleCategory.combat);
        this.registerSetting(Reach.reach = new DoubleSliderSetting("Reach (Blocks)", 3.1, 3.3, 3.0, 6.0, 0.05));
        this.registerSetting(Reach.weapon_only = new TickSetting("Weapon only", false));
        this.registerSetting(Reach.moving_only = new TickSetting("Moving only", false));
        this.registerSetting(Reach.sprint_only = new TickSetting("Sprint only", false));
    }
    
    @Override
    public void postApplyConfig() {
    }
    
    public static double getReach() {
        final double normal = Reach.mc.field_71442_b.func_78749_i() ? 5.0 : 3.0;
        if (!Utils.Player.isPlayerInGame()) {
            return normal;
        }
        if (Reach.weapon_only.isToggled() && !Utils.Player.isPlayerHoldingWeapon()) {
            return normal;
        }
        if (Reach.moving_only.isToggled() && Reach.mc.field_71439_g.field_70701_bs == 0.0 && Reach.mc.field_71439_g.field_70702_br == 0.0) {
            return normal;
        }
        if (Reach.sprint_only.isToggled() && !Reach.mc.field_71439_g.func_70051_ag()) {
            return normal;
        }
        return Utils.Client.ranModuleVal(Reach.reach, Utils.Java.rand()) + (Reach.mc.field_71442_b.func_78749_i() ? 2 : 0);
    }
}
