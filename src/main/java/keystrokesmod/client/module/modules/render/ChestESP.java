package keystrokesmod.client.module.modules.render;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.event.impl.*;
import net.minecraftforge.client.event.*;
import keystrokesmod.client.utils.*;
import java.awt.*;
import net.minecraft.tileentity.*;
import java.util.*;
import com.google.common.eventbus.*;

public class ChestESP extends Module
{
    public static SliderSetting a;
    public static SliderSetting b;
    public static SliderSetting c;
    public static TickSetting d;
    
    public ChestESP() {
        super("ChestESP", ModuleCategory.render);
        ChestESP.a = new SliderSetting("Red", 0.0, 0.0, 255.0, 1.0);
        ChestESP.b = new SliderSetting("Green", 0.0, 0.0, 255.0, 1.0);
        ChestESP.c = new SliderSetting("Blue", 255.0, 0.0, 255.0, 1.0);
        ChestESP.d = new TickSetting("Rainbow", false);
        this.registerSetting(ChestESP.a);
        this.registerSetting(ChestESP.b);
        this.registerSetting(ChestESP.c);
        this.registerSetting(ChestESP.d);
    }
    
    @Subscribe
    public void onForgeEvent(final ForgeEvent fe) {
        if (fe.getEvent() instanceof RenderWorldLastEvent && Utils.Player.isPlayerInGame()) {
            final int rgb = ChestESP.d.isToggled() ? Utils.Client.rainbowDraw(2L, 0L) : new Color((int)ChestESP.a.getInput(), (int)ChestESP.b.getInput(), (int)ChestESP.c.getInput()).getRGB();
            for (final TileEntity te : ChestESP.mc.field_71441_e.field_147482_g) {
                if (te instanceof TileEntityChest || te instanceof TileEntityEnderChest) {
                    Utils.HUD.re(te.func_174877_v(), rgb, true);
                }
            }
        }
    }
}
