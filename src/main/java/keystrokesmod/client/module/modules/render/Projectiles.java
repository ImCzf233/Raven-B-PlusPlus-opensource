package keystrokesmod.client.module.modules.render;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.event.impl.*;
import net.minecraftforge.client.event.*;
import keystrokesmod.client.utils.*;
import org.lwjgl.opengl.*;
import net.minecraft.util.*;
import net.minecraft.client.entity.*;
import net.minecraft.item.*;
import net.minecraft.client.renderer.entity.*;
import com.google.common.eventbus.*;

public class Projectiles extends Module
{
    public static SliderSetting w;
    
    public Projectiles() {
        super("Projectiles", ModuleCategory.render);
        this.registerSetting(Projectiles.w = new SliderSetting("Thickness", 2.0, 1.0, 10.0, 1.0));
    }
    
    @Subscribe
    public void onForgeEvent(final ForgeEvent fe) {
        if (fe.getEvent() instanceof RenderWorldLastEvent) {
            if (!Utils.Player.isPlayerInGame()) {
                return;
            }
            final EntityPlayerSP player = Projectiles.mc.field_71439_g;
            if (Projectiles.mc.field_71439_g.func_71045_bC() == null) {
                return;
            }
            final Item stack = Projectiles.mc.field_71439_g.func_71045_bC().func_77973_b();
            if (stack == null) {
                return;
            }
            if (!(stack instanceof ItemBow)) {
                return;
            }
            final Timer timer = new Timer(3.0f);
            double arrowPosX = player.field_70142_S + (player.field_70165_t - player.field_70142_S) * timer.field_74281_c - Math.cos((float)Math.toRadians(player.field_70177_z)) * 0.1599999964237213;
            double arrowPosY = player.field_70137_T + (player.field_70163_u - player.field_70137_T) * timer.field_74281_c + player.func_70047_e() - 0.1;
            double arrowPosZ = player.field_70136_U + (player.field_70161_v - player.field_70136_U) * timer.field_74281_c - Math.sin((float)Math.toRadians(player.field_70177_z)) * 0.1599999964237213;
            final float arrowMotionFactor = 1.0f;
            final float yaw = (float)Math.toRadians(player.field_70177_z);
            final float pitch = (float)Math.toRadians(player.field_70125_A);
            float arrowMotionX = (float)(-Math.sin(yaw) * Math.cos(pitch) * arrowMotionFactor);
            float arrowMotionY = (float)(-Math.sin(pitch) * arrowMotionFactor);
            float arrowMotionZ = (float)(Math.cos(yaw) * Math.cos(pitch) * arrowMotionFactor);
            final double arrowMotion = Math.sqrt(arrowMotionX * arrowMotionX + arrowMotionY * arrowMotionY + arrowMotionZ * arrowMotionZ);
            arrowMotionX /= (float)arrowMotion;
            arrowMotionY /= (float)arrowMotion;
            arrowMotionZ /= (float)arrowMotion;
            float bowPower = (72000 - player.func_71052_bv()) / 20.0f;
            bowPower = (bowPower * bowPower + bowPower * 2.0f) / 3.0f;
            if (bowPower > 1.0f || bowPower <= 0.1f) {
                bowPower = 1.0f;
            }
            bowPower *= 3.0f;
            arrowMotionX *= bowPower;
            arrowMotionY *= bowPower;
            arrowMotionZ *= bowPower;
            GL11.glPushMatrix();
            GL11.glDisable(3553);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            GL11.glEnable(2848);
            GL11.glLineWidth((float)(int)Projectiles.w.getInput());
            final RenderManager renderManager = Projectiles.mc.func_175598_ae();
            final double gravity = 0.05;
            final Vec3 playerVector = new Vec3(player.field_70165_t, player.field_70163_u + player.func_70047_e(), player.field_70161_v);
            GL11.glColor4f(0.0f, 1.0f, 0.0f, 0.75f);
            GL11.glBegin(3);
            for (int i = 0; i < 1000; ++i) {
                GL11.glVertex3d(arrowPosX - renderManager.field_78730_l, arrowPosY - renderManager.field_78731_m, arrowPosZ - renderManager.field_78728_n);
                arrowPosX += arrowMotionX * 0.1;
                arrowPosY += arrowMotionY * 0.1;
                arrowPosZ += arrowMotionZ * 0.1;
                arrowMotionX *= (float)0.999;
                arrowMotionY *= (float)0.999;
                arrowMotionZ *= (float)0.999;
                arrowMotionY -= (float)(gravity * 0.1);
                if (Projectiles.mc.field_71441_e.func_72933_a(playerVector, new Vec3(arrowPosX, arrowPosY, arrowPosZ)) != null) {
                    break;
                }
            }
            GL11.glEnd();
            final double renderX = arrowPosX - renderManager.field_78730_l;
            final double renderY = arrowPosY - renderManager.field_78731_m;
            final double renderZ = arrowPosZ - renderManager.field_78728_n;
            GL11.glPushMatrix();
            GL11.glTranslated(renderX - 0.5, renderY - 0.5, renderZ - 0.5);
            GL11.glColor4f(0.0f, 1.0f, 0.0f, 0.25f);
            GL11.glColor4f(0.0f, 1.0f, 0.0f, 0.75f);
            GL11.glPopMatrix();
            GL11.glDisable(3042);
            GL11.glEnable(3553);
            GL11.glEnable(2929);
            GL11.glDepthMask(true);
            GL11.glDisable(2848);
            GL11.glPopMatrix();
        }
    }
}
