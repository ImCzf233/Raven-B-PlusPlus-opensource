package keystrokesmod.client.mixin.mixins;

import net.minecraft.client.renderer.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.entity.*;
import keystrokesmod.client.main.*;
import keystrokesmod.client.module.*;
import com.google.common.base.*;
import keystrokesmod.client.module.modules.combat.*;
import net.minecraft.entity.item.*;
import java.util.*;
import net.minecraft.util.*;
import org.spongepowered.asm.mixin.*;
import keystrokesmod.client.module.modules.render.*;
import net.minecraft.potion.*;
import net.minecraft.world.*;

@Mixin(priority = 999, value = { EntityRenderer.class })
public abstract class MixinEntityRenderer
{
    @Shadow
    private Minecraft field_78531_r;
    @Shadow
    private Entity field_78528_u;
    @Shadow
    private boolean field_78536_aa;
    @Shadow
    private float field_78514_e;
    @Shadow
    private float field_82831_U;
    @Shadow
    private float field_82832_V;
    @Shadow
    @Final
    private int[] field_78504_Q;
    @Shadow
    @Final
    private DynamicTexture field_78513_d;
    
    @Shadow
    protected abstract float func_180438_a(final EntityLivingBase p0, final float p1);
    
    @Overwrite
    public void func_78473_a(final float p_getMouseOver_1_) {
        final Entity entity = this.field_78531_r.func_175606_aa();
        if (entity != null && this.field_78531_r.field_71441_e != null) {
            this.field_78531_r.field_71424_I.func_76320_a("pick");
            this.field_78531_r.field_147125_j = null;
            double reach = this.field_78531_r.field_71442_b.func_78757_d();
            this.field_78531_r.field_71476_x = entity.func_174822_a(reach, p_getMouseOver_1_);
            double distanceToVec = reach;
            final Vec3 vec3 = entity.func_174824_e(p_getMouseOver_1_);
            boolean flag = false;
            final Module reachMod = Raven.moduleManager.getModuleByClazz(Reach.class);
            if (!reachMod.isEnabled()) {
                if (this.field_78531_r.field_71442_b.func_78749_i()) {
                    reach = 6.0;
                    distanceToVec = 6.0;
                }
                else if (reach > 3.0) {
                    flag = true;
                }
            }
            else if (this.field_78531_r.field_71442_b.func_78749_i()) {
                reach = 6.0;
                distanceToVec = 6.0;
            }
            else {
                reach = Reach.getReach();
            }
            if (this.field_78531_r.field_71476_x != null) {
                distanceToVec = this.field_78531_r.field_71476_x.field_72307_f.func_72438_d(vec3);
            }
            final Vec3 vec4 = entity.func_70676_i(p_getMouseOver_1_);
            final Vec3 vec5 = vec3.func_72441_c(vec4.field_72450_a * reach, vec4.field_72448_b * reach, vec4.field_72449_c * reach);
            this.field_78528_u = null;
            Vec3 vec6 = null;
            final float f = 1.0f;
            final List<Entity> list = (List<Entity>)this.field_78531_r.field_71441_e.func_175674_a(entity, entity.func_174813_aQ().func_72321_a(vec4.field_72450_a * reach, vec4.field_72448_b * reach, vec4.field_72449_c * reach).func_72314_b((double)f, (double)f, (double)f), Predicates.and(EntitySelectors.field_180132_d, Entity::func_70067_L));
            double d2 = distanceToVec;
            for (final Entity entity2 : list) {
                final float f2 = entity2.func_70111_Y();
                final double kms = HitBox.exp(entity2);
                final AxisAlignedBB axisalignedbb = entity2.func_174813_aQ().func_72314_b((double)f2, (double)f2, (double)f2).func_72314_b(kms, HitBox.b.isToggled() ? kms : 0.0, kms);
                final MovingObjectPosition movingobjectposition = axisalignedbb.func_72327_a(vec3, vec5);
                if (axisalignedbb.func_72318_a(vec3)) {
                    if (d2 < 0.0) {
                        continue;
                    }
                    this.field_78528_u = entity2;
                    vec6 = ((movingobjectposition == null) ? vec3 : movingobjectposition.field_72307_f);
                    d2 = 0.0;
                }
                else {
                    if (movingobjectposition == null) {
                        continue;
                    }
                    final double d3 = vec3.func_72438_d(movingobjectposition.field_72307_f);
                    if (d3 >= d2 && d2 != 0.0) {
                        continue;
                    }
                    if (entity2 == entity.field_70154_o && !entity.canRiderInteract()) {
                        if (d2 != 0.0) {
                            continue;
                        }
                        this.field_78528_u = entity2;
                        vec6 = movingobjectposition.field_72307_f;
                    }
                    else {
                        this.field_78528_u = entity2;
                        vec6 = movingobjectposition.field_72307_f;
                        d2 = d3;
                    }
                }
            }
            if (this.field_78528_u != null && flag && vec3.func_72438_d(vec6) > (reachMod.isEnabled() ? Reach.getReach() : 3.0)) {
                this.field_78528_u = null;
                assert vec6 != null;
                this.field_78531_r.field_71476_x = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec6, (EnumFacing)null, new BlockPos(vec6));
            }
            if (this.field_78528_u != null && (d2 < distanceToVec || this.field_78531_r.field_71476_x == null)) {
                this.field_78531_r.field_71476_x = new MovingObjectPosition(this.field_78528_u, vec6);
                if (this.field_78528_u instanceof EntityLivingBase || this.field_78528_u instanceof EntityItemFrame) {
                    this.field_78531_r.field_147125_j = this.field_78528_u;
                }
            }
            this.field_78531_r.field_71424_I.func_76319_b();
        }
    }
    
    @Overwrite
    private void func_78472_g(final float p_updateLightmap_1_) {
        if (this.field_78536_aa) {
            this.field_78531_r.field_71424_I.func_76320_a("lightTex");
            final World world = (World)this.field_78531_r.field_71441_e;
            if (world != null) {
                if (XRay.instance.isEnabled()) {
                    for (int i = 0; i < 256; ++i) {
                        this.field_78504_Q[i] = -1;
                    }
                    this.field_78513_d.func_110564_a();
                    this.field_78536_aa = false;
                    this.field_78531_r.field_71424_I.func_76319_b();
                    return;
                }
                final float f = world.func_72971_b(1.0f);
                final float f2 = f * 0.95f + 0.05f;
                for (int j = 0; j < 256; ++j) {
                    float f3 = world.field_73011_w.func_177497_p()[j / 16] * f2;
                    final float f4 = world.field_73011_w.func_177497_p()[j % 16] * (this.field_78514_e * 0.1f + 1.5f);
                    if (world.func_175658_ac() > 0) {
                        f3 = world.field_73011_w.func_177497_p()[j / 16];
                    }
                    final float f5 = f3 * (f * 0.65f + 0.35f);
                    final float f6 = f3 * (f * 0.65f + 0.35f);
                    final float f7 = f4 * ((f4 * 0.6f + 0.4f) * 0.6f + 0.4f);
                    final float f8 = f4 * (f4 * f4 * 0.6f + 0.4f);
                    float f9 = f5 + f4;
                    float f10 = f6 + f7;
                    float f11 = f3 + f8;
                    f9 = f9 * 0.96f + 0.03f;
                    f10 = f10 * 0.96f + 0.03f;
                    f11 = f11 * 0.96f + 0.03f;
                    if (this.field_82831_U > 0.0f) {
                        final float f12 = this.field_82832_V + (this.field_82831_U - this.field_82832_V) * p_updateLightmap_1_;
                        f9 = f9 * (1.0f - f12) + f9 * 0.7f * f12;
                        f10 = f10 * (1.0f - f12) + f10 * 0.6f * f12;
                        f11 = f11 * (1.0f - f12) + f11 * 0.6f * f12;
                    }
                    if (world.field_73011_w.func_177502_q() == 1) {
                        f9 = 0.22f + f4 * 0.75f;
                        f10 = 0.28f + f7 * 0.75f;
                        f11 = 0.25f + f8 * 0.75f;
                    }
                    if (this.field_78531_r.field_71439_g.func_70644_a(Potion.field_76439_r)) {
                        final float f12 = this.func_180438_a((EntityLivingBase)this.field_78531_r.field_71439_g, p_updateLightmap_1_);
                        float f13 = 1.0f / f9;
                        if (f13 > 1.0f / f10) {
                            f13 = 1.0f / f10;
                        }
                        if (f13 > 1.0f / f11) {
                            f13 = 1.0f / f11;
                        }
                        f9 = f9 * (1.0f - f12) + f9 * f13 * f12;
                        f10 = f10 * (1.0f - f12) + f10 * f13 * f12;
                        f11 = f11 * (1.0f - f12) + f11 * f13 * f12;
                    }
                    if (f9 > 1.0f) {
                        f9 = 1.0f;
                    }
                    if (f10 > 1.0f) {
                        f10 = 1.0f;
                    }
                    if (f11 > 1.0f) {
                        f11 = 1.0f;
                    }
                    final float f12 = this.field_78531_r.field_71474_y.field_74333_Y;
                    float f13 = 1.0f - f9;
                    float f14 = 1.0f - f10;
                    float f15 = 1.0f - f11;
                    f13 = 1.0f - f13 * f13 * f13 * f13;
                    f14 = 1.0f - f14 * f14 * f14 * f14;
                    f15 = 1.0f - f15 * f15 * f15 * f15;
                    f9 = f9 * (1.0f - f12) + f13 * f12;
                    f10 = f10 * (1.0f - f12) + f14 * f12;
                    f11 = f11 * (1.0f - f12) + f15 * f12;
                    f9 = f9 * 0.96f + 0.03f;
                    f10 = f10 * 0.96f + 0.03f;
                    f11 = f11 * 0.96f + 0.03f;
                    if (f9 > 1.0f) {
                        f9 = 1.0f;
                    }
                    if (f10 > 1.0f) {
                        f10 = 1.0f;
                    }
                    if (f11 > 1.0f) {
                        f11 = 1.0f;
                    }
                    if (f9 < 0.0f) {
                        f9 = 0.0f;
                    }
                    if (f10 < 0.0f) {
                        f10 = 0.0f;
                    }
                    if (f11 < 0.0f) {
                        f11 = 0.0f;
                    }
                    final int k = 255;
                    final int l = (int)(f9 * 255.0f);
                    final int m = (int)(f10 * 255.0f);
                    final int i2 = (int)(f11 * 255.0f);
                    this.field_78504_Q[j] = (k << 24 | l << 16 | m << 8 | i2);
                }
                this.field_78513_d.func_110564_a();
                this.field_78536_aa = false;
                this.field_78531_r.field_71424_I.func_76319_b();
            }
        }
    }
}
