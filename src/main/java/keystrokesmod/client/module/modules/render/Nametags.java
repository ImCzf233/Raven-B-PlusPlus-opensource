package keystrokesmod.client.module.modules.render;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.event.impl.*;
import net.minecraftforge.client.event.*;
import net.minecraft.entity.player.*;
import keystrokesmod.client.module.modules.world.*;
import net.minecraft.entity.*;
import keystrokesmod.client.utils.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.vertex.*;
import net.minecraft.client.renderer.*;
import com.google.common.eventbus.*;

public class Nametags extends Module
{
    public static SliderSetting a;
    public static TickSetting b;
    public static TickSetting c;
    public static TickSetting d;
    public static TickSetting rm;
    public static TickSetting e;
    
    public Nametags() {
        super("Nametags", ModuleCategory.render);
        this.registerSetting(Nametags.a = new SliderSetting("Offset", 0.0, -40.0, 40.0, 1.0));
        this.registerSetting(Nametags.b = new TickSetting("Rect", true));
        this.registerSetting(Nametags.c = new TickSetting("Show health", true));
        this.registerSetting(Nametags.d = new TickSetting("Show invis", true));
        this.registerSetting(Nametags.rm = new TickSetting("Remove tags", false));
    }
    
    @Subscribe
    public void onForgeEvent(final ForgeEvent fe) {
        if (fe.getEvent() instanceof RenderLivingEvent.Specials.Pre) {
            final RenderLivingEvent.Specials.Pre e = (RenderLivingEvent.Specials.Pre)fe.getEvent();
            if (Nametags.rm.isToggled()) {
                e.setCanceled(true);
            }
            else if (e.entity instanceof EntityPlayer && e.entity != Nametags.mc.field_71439_g && e.entity.field_70725_aQ == 0) {
                final EntityPlayer en = (EntityPlayer)e.entity;
                if (!Nametags.d.isToggled() && en.func_82150_aj()) {
                    return;
                }
                if (AntiBot.bot((Entity)en) || en.getDisplayNameString().isEmpty()) {
                    return;
                }
                e.setCanceled(true);
                String str = en.func_145748_c_().func_150254_d();
                if (Nametags.c.isToggled()) {
                    final double r = en.func_110143_aJ() / en.func_110138_aP();
                    final String h = ((r < 0.3) ? "¡ìc" : ((r < 0.5) ? "¡ì6" : ((r < 0.7) ? "¡ìe" : "¡ìa"))) + Utils.Java.round(en.func_110143_aJ(), 1);
                    str = str + " " + h;
                }
                GlStateManager.func_179094_E();
                GlStateManager.func_179109_b((float)e.x + 0.0f, (float)e.y + en.field_70131_O + 0.5f, (float)e.z);
                GL11.glNormal3f(0.0f, 1.0f, 0.0f);
                GlStateManager.func_179114_b(-Nametags.mc.func_175598_ae().field_78735_i, 0.0f, 1.0f, 0.0f);
                GlStateManager.func_179114_b(Nametags.mc.func_175598_ae().field_78732_j, 1.0f, 0.0f, 0.0f);
                final float f1 = 0.02666667f;
                GlStateManager.func_179152_a(-f1, -f1, f1);
                if (en.func_70093_af()) {
                    GlStateManager.func_179109_b(0.0f, 9.374999f, 0.0f);
                }
                GlStateManager.func_179140_f();
                GlStateManager.func_179132_a(false);
                GlStateManager.func_179097_i();
                GlStateManager.func_179147_l();
                GlStateManager.func_179120_a(770, 771, 1, 0);
                final Tessellator tessellator = Tessellator.func_178181_a();
                final WorldRenderer worldrenderer = tessellator.func_178180_c();
                final int i = (int)(-Nametags.a.getInput());
                final int j = Nametags.mc.field_71466_p.func_78256_a(str) / 2;
                GlStateManager.func_179090_x();
                if (Nametags.b.isToggled()) {
                    worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181706_f);
                    worldrenderer.func_181662_b((double)(-j - 1), (double)(-1 + i), 0.0).func_181666_a(0.0f, 0.0f, 0.0f, 0.25f).func_181675_d();
                    worldrenderer.func_181662_b((double)(-j - 1), (double)(8 + i), 0.0).func_181666_a(0.0f, 0.0f, 0.0f, 0.25f).func_181675_d();
                    worldrenderer.func_181662_b((double)(j + 1), (double)(8 + i), 0.0).func_181666_a(0.0f, 0.0f, 0.0f, 0.25f).func_181675_d();
                    worldrenderer.func_181662_b((double)(j + 1), (double)(-1 + i), 0.0).func_181666_a(0.0f, 0.0f, 0.0f, 0.25f).func_181675_d();
                    tessellator.func_78381_a();
                }
                GlStateManager.func_179098_w();
                Nametags.mc.field_71466_p.func_78276_b(str, -Nametags.mc.field_71466_p.func_78256_a(str) / 2, i, -1);
                GlStateManager.func_179126_j();
                GlStateManager.func_179132_a(true);
                GlStateManager.func_179145_e();
                GlStateManager.func_179084_k();
                GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
                GlStateManager.func_179121_F();
            }
        }
    }
}
