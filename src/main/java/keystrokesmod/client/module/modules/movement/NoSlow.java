package keystrokesmod.client.module.modules.movement;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.event.impl.*;
import net.minecraft.network.play.server.*;
import com.google.common.eventbus.*;

public class NoSlow extends Module
{
    public static DescriptionSetting a;
    public static DescriptionSetting c;
    public static SliderSetting b;
    public static TickSetting noReset;
    
    public NoSlow() {
        super("NoSlow", ModuleCategory.movement);
        this.registerSetting(NoSlow.a = new DescriptionSetting("Default is 80% motion reduction."));
        this.registerSetting(NoSlow.c = new DescriptionSetting("Use 'No Reset' on Hypixel."));
        this.registerSetting(NoSlow.b = new SliderSetting("Slow %", 80.0, 0.0, 80.0, 1.0));
        this.registerSetting(NoSlow.noReset = new TickSetting("No Reset", false));
    }
    
    @Subscribe
    public void onPacket(final PacketEvent e) {
        if (NoSlow.noReset.isToggled() && e.getPacket() instanceof S30PacketWindowItems && NoSlow.mc.field_71439_g.func_71039_bw()) {
            e.cancel();
        }
    }
}
