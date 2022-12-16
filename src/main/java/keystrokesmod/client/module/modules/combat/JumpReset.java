package keystrokesmod.client.module.modules.combat;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.event.impl.*;
import net.minecraft.network.play.server.*;
import com.google.common.eventbus.*;

public class JumpReset extends Module
{
    public JumpReset() {
        super("JumpReset", ModuleCategory.combat);
        this.registerSetting(new DescriptionSetting("Auto Jump Reset. That's it."));
    }
    
    @Subscribe
    public void onPacket(final PacketEvent e) {
        if (e.isIncoming() && e.getPacket() instanceof S12PacketEntityVelocity && e.getPacket().func_149412_c() == JumpReset.mc.field_71439_g.func_145782_y() && JumpReset.mc.field_71439_g.field_70122_E) {
            JumpReset.mc.field_71439_g.func_70664_aZ();
        }
    }
}
