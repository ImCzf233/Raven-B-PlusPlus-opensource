package keystrokesmod.client.utils;

import net.minecraft.client.*;
import net.minecraftforge.fml.common.gameevent.*;
import keystrokesmod.client.main.*;
import net.minecraft.client.gui.*;
import keystrokesmod.client.module.modules.player.*;
import net.minecraft.entity.*;
import java.awt.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class DebugInfoRenderer extends Gui
{
    private static final Minecraft mc;
    
    @SubscribeEvent
    public void onRenderTick(final TickEvent.RenderTickEvent ev) {
        if (Raven.debugger && ev.phase == TickEvent.Phase.END && Utils.Player.isPlayerInGame() && DebugInfoRenderer.mc.field_71462_r == null) {
            final ScaledResolution res = new ScaledResolution(DebugInfoRenderer.mc);
            final double bps = Utils.Player.getPlayerBPS((Entity)((Freecam.en == null) ? DebugInfoRenderer.mc.field_71439_g : Freecam.en), 2);
            int rgb;
            if (bps < 10.0) {
                rgb = Color.green.getRGB();
            }
            else if (bps < 30.0) {
                rgb = Color.yellow.getRGB();
            }
            else if (bps < 60.0) {
                rgb = Color.orange.getRGB();
            }
            else if (bps < 160.0) {
                rgb = Color.red.getRGB();
            }
            else {
                rgb = Color.black.getRGB();
            }
            final String t = bps + "bps";
            final int x = res.func_78326_a() / 2 - DebugInfoRenderer.mc.field_71466_p.func_78256_a(t) / 2;
            final int y = res.func_78328_b() / 2 + 15;
            DebugInfoRenderer.mc.field_71466_p.func_175065_a(t, (float)x, (float)y, rgb, false);
        }
    }
    
    static {
        mc = Minecraft.func_71410_x();
    }
}
