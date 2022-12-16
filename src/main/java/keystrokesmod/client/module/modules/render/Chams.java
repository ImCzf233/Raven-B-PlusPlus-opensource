package keystrokesmod.client.module.modules.render;

import keystrokesmod.client.module.*;
import keystrokesmod.client.event.impl.*;
import net.minecraftforge.client.event.*;
import org.lwjgl.opengl.*;
import com.google.common.eventbus.*;

public class Chams extends Module
{
    public Chams() {
        super("Chams", ModuleCategory.render);
    }
    
    @Subscribe
    public void onForgeEvent(final ForgeEvent fe) {
        if (fe.getEvent() instanceof RenderPlayerEvent.Pre) {
            if (((RenderPlayerEvent.Pre)fe.getEvent()).entity != Chams.mc.field_71439_g) {
                GL11.glEnable(32823);
                GL11.glPolygonOffset(1.0f, -1100000.0f);
            }
        }
        else if (fe.getEvent() instanceof RenderPlayerEvent.Post && ((RenderPlayerEvent.Post)fe.getEvent()).entity != Chams.mc.field_71439_g) {
            GL11.glDisable(32823);
            GL11.glPolygonOffset(1.0f, 1100000.0f);
        }
    }
}
