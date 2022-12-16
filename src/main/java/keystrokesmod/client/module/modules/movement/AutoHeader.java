package keystrokesmod.client.module.modules.movement;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.event.impl.*;
import keystrokesmod.client.utils.*;
import org.lwjgl.input.*;
import io.netty.util.internal.*;
import com.google.common.eventbus.*;

public class AutoHeader extends Module
{
    public static DescriptionSetting desc;
    public static TickSetting cancelDuringShift;
    public static TickSetting onlyWhenHoldingSpacebar;
    public static SliderSetting pbs;
    private double startWait;
    
    public AutoHeader() {
        super("AutoHeadHitter", ModuleCategory.movement);
        this.registerSetting(AutoHeader.desc = new DescriptionSetting("Spams spacebar when under blocks"));
        this.registerSetting(AutoHeader.cancelDuringShift = new TickSetting("Cancel if snkeaing", true));
        this.registerSetting(AutoHeader.onlyWhenHoldingSpacebar = new TickSetting("Only when holding jump", true));
        this.registerSetting(AutoHeader.pbs = new SliderSetting("Jump Presses per second", 12.0, 1.0, 20.0, 1.0));
    }
    
    @Override
    public void onEnable() {
        this.startWait = (double)System.currentTimeMillis();
        super.onEnable();
    }
    
    @Subscribe
    public void onTick(final TickEvent e) {
        if (!Utils.Player.isPlayerInGame() || AutoHeader.mc.field_71462_r != null) {
            return;
        }
        if (AutoHeader.cancelDuringShift.isToggled() && AutoHeader.mc.field_71439_g.func_70093_af()) {
            return;
        }
        if (AutoHeader.onlyWhenHoldingSpacebar.isToggled() && !Keyboard.isKeyDown(AutoHeader.mc.field_71474_y.field_74314_A.func_151463_i())) {
            return;
        }
        if (Utils.Player.playerUnderBlock() && AutoHeader.mc.field_71439_g.field_70122_E && this.startWait + 1000.0 / ThreadLocalRandom.current().nextDouble(AutoHeader.pbs.getInput() - 0.543543, AutoHeader.pbs.getInput() + 1.32748923) < System.currentTimeMillis()) {
            AutoHeader.mc.field_71439_g.func_70664_aZ();
            this.startWait = (double)System.currentTimeMillis();
        }
    }
}
