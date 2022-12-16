package keystrokesmod.client.mixin.mixins;

import net.minecraft.world.*;
import net.minecraft.item.*;
import net.minecraftforge.common.*;
import net.minecraft.enchantment.*;
import net.minecraft.potion.*;
import net.minecraft.util.*;
import keystrokesmod.client.main.*;
import keystrokesmod.client.module.modules.movement.*;
import keystrokesmod.client.module.*;
import net.minecraft.entity.player.*;
import net.minecraft.network.play.server.*;
import net.minecraft.network.*;
import net.minecraft.entity.boss.*;
import net.minecraft.stats.*;
import net.minecraft.entity.*;
import org.spongepowered.asm.mixin.*;

@Mixin(priority = 995, value = { EntityPlayer.class })
public abstract class MixinEntityPlayer extends EntityLivingBase
{
    public MixinEntityPlayer(final World p_i1594_1_) {
        super(p_i1594_1_);
    }
    
    @Shadow
    public abstract ItemStack func_70694_bm();
    
    @Shadow
    public abstract void func_71009_b(final Entity p0);
    
    @Shadow
    public abstract void func_71047_c(final Entity p0);
    
    @Shadow
    public abstract void func_71029_a(final StatBase p0);
    
    @Shadow
    public abstract ItemStack func_71045_bC();
    
    @Shadow
    public abstract void func_71028_bD();
    
    @Shadow
    public abstract void func_71064_a(final StatBase p0, final int p1);
    
    @Shadow
    public abstract void func_71020_j(final float p0);
    
    @Overwrite
    public void func_71059_n(final Entity p_attackTargetEntityWithCurrentItem_1_) {
        if (ForgeHooks.onPlayerAttackTarget((EntityPlayer)this, p_attackTargetEntityWithCurrentItem_1_) && p_attackTargetEntityWithCurrentItem_1_.func_70075_an() && !p_attackTargetEntityWithCurrentItem_1_.func_85031_j((Entity)this)) {
            float f = (float)this.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111126_e();
            float f2 = 0.0f;
            if (p_attackTargetEntityWithCurrentItem_1_ instanceof EntityLivingBase) {
                f2 = EnchantmentHelper.func_152377_a(this.func_70694_bm(), ((EntityLivingBase)p_attackTargetEntityWithCurrentItem_1_).func_70668_bt());
            }
            else {
                f2 = EnchantmentHelper.func_152377_a(this.func_70694_bm(), EnumCreatureAttribute.UNDEFINED);
            }
            int i = EnchantmentHelper.func_77501_a((EntityLivingBase)this);
            if (this.func_70051_ag()) {
                ++i;
            }
            if (f > 0.0f || f2 > 0.0f) {
                final boolean flag = this.field_70143_R > 0.0f && !this.field_70122_E && !this.func_70617_f_() && !this.func_70090_H() && !this.func_70644_a(Potion.field_76440_q) && this.field_70154_o == null && p_attackTargetEntityWithCurrentItem_1_ instanceof EntityLivingBase;
                if (flag && f > 0.0f) {
                    f *= 1.5f;
                }
                f += f2;
                boolean flag2 = false;
                final int j = EnchantmentHelper.func_90036_a((EntityLivingBase)this);
                if (p_attackTargetEntityWithCurrentItem_1_ instanceof EntityLivingBase && j > 0 && !p_attackTargetEntityWithCurrentItem_1_.func_70027_ad()) {
                    flag2 = true;
                    p_attackTargetEntityWithCurrentItem_1_.func_70015_d(1);
                }
                final double d0 = p_attackTargetEntityWithCurrentItem_1_.field_70159_w;
                final double d2 = p_attackTargetEntityWithCurrentItem_1_.field_70181_x;
                final double d3 = p_attackTargetEntityWithCurrentItem_1_.field_70179_y;
                final boolean flag3 = p_attackTargetEntityWithCurrentItem_1_.func_70097_a(DamageSource.func_76365_a((EntityPlayer)this), f);
                if (flag3) {
                    if (i > 0) {
                        p_attackTargetEntityWithCurrentItem_1_.func_70024_g((double)(-MathHelper.func_76126_a(this.field_70177_z * 3.1415927f / 180.0f) * i * 0.5f), 0.1, (double)(MathHelper.func_76134_b(this.field_70177_z * 3.1415927f / 180.0f) * i * 0.5f));
                        final Module keepSprint = Raven.moduleManager.getModuleByClazz(KeepSprint.class);
                        if (keepSprint != null && keepSprint.isEnabled()) {
                            KeepSprint.slowdown(p_attackTargetEntityWithCurrentItem_1_);
                        }
                        else {
                            this.field_70159_w *= 0.6;
                            this.field_70179_y *= 0.6;
                            this.func_70031_b(false);
                        }
                    }
                    if (p_attackTargetEntityWithCurrentItem_1_ instanceof EntityPlayerMP && p_attackTargetEntityWithCurrentItem_1_.field_70133_I) {
                        ((EntityPlayerMP)p_attackTargetEntityWithCurrentItem_1_).field_71135_a.func_147359_a((Packet)new S12PacketEntityVelocity(p_attackTargetEntityWithCurrentItem_1_));
                        p_attackTargetEntityWithCurrentItem_1_.field_70133_I = false;
                        p_attackTargetEntityWithCurrentItem_1_.field_70159_w = d0;
                        p_attackTargetEntityWithCurrentItem_1_.field_70181_x = d2;
                        p_attackTargetEntityWithCurrentItem_1_.field_70179_y = d3;
                    }
                    if (flag) {
                        this.func_71009_b(p_attackTargetEntityWithCurrentItem_1_);
                    }
                    if (f2 > 0.0f) {
                        this.func_71047_c(p_attackTargetEntityWithCurrentItem_1_);
                    }
                    if (f >= 18.0f) {
                        this.func_71029_a((StatBase)AchievementList.field_75999_E);
                    }
                    this.func_130011_c(p_attackTargetEntityWithCurrentItem_1_);
                    if (p_attackTargetEntityWithCurrentItem_1_ instanceof EntityLivingBase) {
                        EnchantmentHelper.func_151384_a((EntityLivingBase)p_attackTargetEntityWithCurrentItem_1_, (Entity)this);
                    }
                    EnchantmentHelper.func_151385_b((EntityLivingBase)this, p_attackTargetEntityWithCurrentItem_1_);
                    final ItemStack itemstack = this.func_71045_bC();
                    Entity entity = p_attackTargetEntityWithCurrentItem_1_;
                    if (p_attackTargetEntityWithCurrentItem_1_ instanceof EntityDragonPart) {
                        final IEntityMultiPart ientitymultipart = ((EntityDragonPart)p_attackTargetEntityWithCurrentItem_1_).field_70259_a;
                        if (ientitymultipart instanceof EntityLivingBase) {
                            entity = (Entity)ientitymultipart;
                        }
                    }
                    if (itemstack != null && entity instanceof EntityLivingBase) {
                        itemstack.func_77961_a((EntityLivingBase)entity, (EntityPlayer)this);
                        if (itemstack.field_77994_a <= 0) {
                            this.func_71028_bD();
                        }
                    }
                    if (p_attackTargetEntityWithCurrentItem_1_ instanceof EntityLivingBase) {
                        this.func_71064_a(StatList.field_75951_w, Math.round(f * 10.0f));
                        if (j > 0) {
                            p_attackTargetEntityWithCurrentItem_1_.func_70015_d(j * 4);
                        }
                    }
                    this.func_71020_j(0.3f);
                }
                else if (flag2) {
                    p_attackTargetEntityWithCurrentItem_1_.func_70066_B();
                }
            }
        }
    }
}
