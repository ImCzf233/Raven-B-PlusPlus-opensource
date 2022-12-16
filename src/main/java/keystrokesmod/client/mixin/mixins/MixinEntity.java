package keystrokesmod.client.mixin.mixins;

import net.minecraft.entity.*;
import net.minecraft.world.*;
import net.minecraft.client.*;
import keystrokesmod.client.main.*;
import keystrokesmod.client.module.modules.player.*;
import keystrokesmod.client.module.*;
import keystrokesmod.client.module.modules.world.*;
import net.minecraft.block.material.*;
import net.minecraft.block.*;
import net.minecraft.init.*;
import net.minecraft.crash.*;
import net.minecraft.util.*;
import net.minecraft.item.*;
import java.util.*;
import org.spongepowered.asm.mixin.*;
import keystrokesmod.client.event.impl.*;

@Mixin(priority = 995, value = { Entity.class })
public abstract class MixinEntity
{
    @Shadow
    public boolean field_70145_X;
    @Shadow
    public World field_70170_p;
    @Shadow
    public double field_70165_t;
    @Shadow
    public double field_70163_u;
    @Shadow
    public double field_70161_v;
    @Shadow
    protected boolean field_70134_J;
    @Shadow
    public double field_70159_w;
    @Shadow
    public double field_70181_x;
    @Shadow
    public double field_70179_y;
    @Shadow
    public boolean field_70122_E;
    @Shadow
    public float field_70138_W;
    @Shadow
    public boolean field_70123_F;
    @Shadow
    public boolean field_70124_G;
    @Shadow
    public boolean field_70132_H;
    @Shadow
    public Entity field_70154_o;
    @Shadow
    public float field_70140_Q;
    @Shadow
    public float field_82151_R;
    @Shadow
    private int field_70150_b;
    @Shadow
    protected Random field_70146_Z;
    @Shadow
    private int field_70151_c;
    @Shadow
    public int field_70174_ab;
    @Shadow
    public float field_70177_z;
    
    @Shadow
    public abstract void func_174826_a(final AxisAlignedBB p0);
    
    @Shadow
    public abstract AxisAlignedBB func_174813_aQ();
    
    @Shadow
    protected abstract void func_174829_m();
    
    @Shadow
    public abstract boolean func_70093_af();
    
    @Shadow
    protected abstract void func_180433_a(final double p0, final boolean p1, final Block p2, final BlockPos p3);
    
    @Shadow
    protected abstract boolean func_70041_e_();
    
    @Shadow
    public abstract boolean func_70090_H();
    
    @Shadow
    public abstract void func_85030_a(final String p0, final float p1, final float p2);
    
    @Shadow
    protected abstract String func_145776_H();
    
    @Shadow
    protected abstract void func_180429_a(final BlockPos p0, final Block p1);
    
    @Shadow
    protected abstract void func_145775_I();
    
    @Shadow
    public abstract void func_85029_a(final CrashReportCategory p0);
    
    @Shadow
    public abstract boolean func_70026_G();
    
    @Shadow
    protected abstract void func_70081_e(final int p0);
    
    @Shadow
    public abstract void func_70015_d(final int p0);
    
    @Overwrite
    public void func_70091_d(double p_moveEntity_1_, double p_moveEntity_3_, double p_moveEntity_5_) {
        final Minecraft mc = Minecraft.func_71410_x();
        if (this == mc.field_71439_g) {
            final MoveEvent e = new MoveEvent(p_moveEntity_1_, p_moveEntity_3_, p_moveEntity_5_);
            Raven.eventBus.post((Object)e);
            p_moveEntity_1_ = e.getX();
            p_moveEntity_3_ = e.getY();
            p_moveEntity_5_ = e.getZ();
        }
        if (this.field_70145_X) {
            this.func_174826_a(this.func_174813_aQ().func_72317_d(p_moveEntity_1_, p_moveEntity_3_, p_moveEntity_5_));
            this.func_174829_m();
        }
        else {
            this.field_70170_p.field_72984_F.func_76320_a("move");
            final double d0 = this.field_70165_t;
            final double d2 = this.field_70163_u;
            final double d3 = this.field_70161_v;
            if (this.field_70134_J) {
                this.field_70134_J = false;
                p_moveEntity_1_ *= 0.25;
                p_moveEntity_3_ *= 0.05000000074505806;
                p_moveEntity_5_ *= 0.25;
                this.field_70159_w = 0.0;
                this.field_70181_x = 0.0;
                this.field_70179_y = 0.0;
            }
            double d4 = p_moveEntity_1_;
            final double d5 = p_moveEntity_3_;
            double d6 = p_moveEntity_5_;
            boolean flag;
            if (this == mc.field_71439_g && mc.field_71439_g.field_70122_E) {
                final Module safeWalk = Raven.moduleManager.getModuleByClazz(SafeWalk.class);
                final Module scaffold = Raven.moduleManager.getModuleByClazz(Scaffold.class);
                if (safeWalk != null && safeWalk.isEnabled() && !SafeWalk.doShift.isToggled()) {
                    flag = true;
                    if (SafeWalk.blocksOnly.isToggled()) {
                        final ItemStack i = mc.field_71439_g.func_70694_bm();
                        if (i == null || !(i.func_77973_b() instanceof ItemBlock)) {
                            flag = mc.field_71439_g.func_70093_af();
                        }
                    }
                    if (SafeWalk.lookDown.isToggled() && (mc.field_71439_g.field_70125_A < SafeWalk.pitchRange.getInputMin() || mc.field_71439_g.field_70125_A > SafeWalk.pitchRange.getInputMax())) {
                        flag = mc.field_71439_g.func_70093_af();
                    }
                    if (SafeWalk.shawtyMoment.isToggled() && mc.field_71439_g.field_71158_b.field_78900_b > 0.0f && mc.field_71439_g.field_71158_b.field_78902_a == 0.0f) {
                        flag = mc.field_71439_g.func_70093_af();
                    }
                }
                else {
                    flag = mc.field_71439_g.func_70093_af();
                }
                if (scaffold != null && Scaffold.safewalk()) {
                    flag = true;
                }
            }
            else {
                flag = false;
            }
            if (flag) {
                final double d7 = 0.05;
                while (p_moveEntity_1_ != 0.0 && this.field_70170_p.func_72945_a((Entity)this, this.func_174813_aQ().func_72317_d(p_moveEntity_1_, -1.0, 0.0)).isEmpty()) {
                    if (p_moveEntity_1_ < d7 && p_moveEntity_1_ >= -d7) {
                        p_moveEntity_1_ = 0.0;
                    }
                    else if (p_moveEntity_1_ > 0.0) {
                        p_moveEntity_1_ -= d7;
                    }
                    else {
                        p_moveEntity_1_ += d7;
                    }
                    d4 = p_moveEntity_1_;
                }
                while (p_moveEntity_5_ != 0.0 && this.field_70170_p.func_72945_a((Entity)this, this.func_174813_aQ().func_72317_d(0.0, -1.0, p_moveEntity_5_)).isEmpty()) {
                    if (p_moveEntity_5_ < d7 && p_moveEntity_5_ >= -d7) {
                        p_moveEntity_5_ = 0.0;
                    }
                    else if (p_moveEntity_5_ > 0.0) {
                        p_moveEntity_5_ -= d7;
                    }
                    else {
                        p_moveEntity_5_ += d7;
                    }
                    d6 = p_moveEntity_5_;
                }
                while (p_moveEntity_1_ != 0.0 && p_moveEntity_5_ != 0.0 && this.field_70170_p.func_72945_a((Entity)this, this.func_174813_aQ().func_72317_d(p_moveEntity_1_, -1.0, p_moveEntity_5_)).isEmpty()) {
                    if (p_moveEntity_1_ < d7 && p_moveEntity_1_ >= -d7) {
                        p_moveEntity_1_ = 0.0;
                    }
                    else if (p_moveEntity_1_ > 0.0) {
                        p_moveEntity_1_ -= d7;
                    }
                    else {
                        p_moveEntity_1_ += d7;
                    }
                    d4 = p_moveEntity_1_;
                    if (p_moveEntity_5_ < d7 && p_moveEntity_5_ >= -d7) {
                        p_moveEntity_5_ = 0.0;
                    }
                    else if (p_moveEntity_5_ > 0.0) {
                        p_moveEntity_5_ -= d7;
                    }
                    else {
                        p_moveEntity_5_ += d7;
                    }
                    d6 = p_moveEntity_5_;
                }
            }
            final List<AxisAlignedBB> list1 = (List<AxisAlignedBB>)this.field_70170_p.func_72945_a((Entity)this, this.func_174813_aQ().func_72321_a(p_moveEntity_1_, p_moveEntity_3_, p_moveEntity_5_));
            final AxisAlignedBB axisalignedbb = this.func_174813_aQ();
            for (final AxisAlignedBB axisalignedbb2 : list1) {
                p_moveEntity_3_ = axisalignedbb2.func_72323_b(this.func_174813_aQ(), p_moveEntity_3_);
            }
            this.func_174826_a(this.func_174813_aQ().func_72317_d(0.0, p_moveEntity_3_, 0.0));
            final boolean flag2 = this.field_70122_E || (d5 != p_moveEntity_3_ && d5 < 0.0);
            for (final AxisAlignedBB axisalignedbb3 : list1) {
                p_moveEntity_1_ = axisalignedbb3.func_72316_a(this.func_174813_aQ(), p_moveEntity_1_);
            }
            this.func_174826_a(this.func_174813_aQ().func_72317_d(p_moveEntity_1_, 0.0, 0.0));
            for (final AxisAlignedBB axisalignedbb3 : list1) {
                p_moveEntity_5_ = axisalignedbb3.func_72322_c(this.func_174813_aQ(), p_moveEntity_5_);
            }
            this.func_174826_a(this.func_174813_aQ().func_72317_d(0.0, 0.0, p_moveEntity_5_));
            if (this.field_70138_W > 0.0f && flag2 && (d4 != p_moveEntity_1_ || d6 != p_moveEntity_5_)) {
                final double d8 = p_moveEntity_1_;
                final double d9 = p_moveEntity_3_;
                final double d10 = p_moveEntity_5_;
                final AxisAlignedBB axisalignedbb4 = this.func_174813_aQ();
                this.func_174826_a(axisalignedbb);
                p_moveEntity_3_ = this.field_70138_W;
                final List<AxisAlignedBB> list2 = (List<AxisAlignedBB>)this.field_70170_p.func_72945_a((Entity)this, this.func_174813_aQ().func_72321_a(d4, p_moveEntity_3_, d6));
                AxisAlignedBB axisalignedbb5 = this.func_174813_aQ();
                final AxisAlignedBB axisalignedbb6 = axisalignedbb5.func_72321_a(d4, 0.0, d6);
                double d11 = p_moveEntity_3_;
                for (final AxisAlignedBB axisalignedbb7 : list2) {
                    d11 = axisalignedbb7.func_72323_b(axisalignedbb6, d11);
                }
                axisalignedbb5 = axisalignedbb5.func_72317_d(0.0, d11, 0.0);
                double d12 = d4;
                for (final AxisAlignedBB axisalignedbb8 : list2) {
                    d12 = axisalignedbb8.func_72316_a(axisalignedbb5, d12);
                }
                axisalignedbb5 = axisalignedbb5.func_72317_d(d12, 0.0, 0.0);
                double d13 = d6;
                for (final AxisAlignedBB axisalignedbb9 : list2) {
                    d13 = axisalignedbb9.func_72322_c(axisalignedbb5, d13);
                }
                axisalignedbb5 = axisalignedbb5.func_72317_d(0.0, 0.0, d13);
                AxisAlignedBB axisalignedbb10 = this.func_174813_aQ();
                double d14 = p_moveEntity_3_;
                for (final AxisAlignedBB axisalignedbb11 : list2) {
                    d14 = axisalignedbb11.func_72323_b(axisalignedbb10, d14);
                }
                axisalignedbb10 = axisalignedbb10.func_72317_d(0.0, d14, 0.0);
                double d15 = d4;
                for (final AxisAlignedBB axisalignedbb12 : list2) {
                    d15 = axisalignedbb12.func_72316_a(axisalignedbb10, d15);
                }
                axisalignedbb10 = axisalignedbb10.func_72317_d(d15, 0.0, 0.0);
                double d16 = d6;
                for (final AxisAlignedBB axisalignedbb13 : list2) {
                    d16 = axisalignedbb13.func_72322_c(axisalignedbb10, d16);
                }
                axisalignedbb10 = axisalignedbb10.func_72317_d(0.0, 0.0, d16);
                final double d17 = d12 * d12 + d13 * d13;
                final double d18 = d15 * d15 + d16 * d16;
                if (d17 > d18) {
                    p_moveEntity_1_ = d12;
                    p_moveEntity_5_ = d13;
                    p_moveEntity_3_ = -d11;
                    this.func_174826_a(axisalignedbb5);
                }
                else {
                    p_moveEntity_1_ = d15;
                    p_moveEntity_5_ = d16;
                    p_moveEntity_3_ = -d14;
                    this.func_174826_a(axisalignedbb10);
                }
                for (final AxisAlignedBB axisalignedbb14 : list2) {
                    p_moveEntity_3_ = axisalignedbb14.func_72323_b(this.func_174813_aQ(), p_moveEntity_3_);
                }
                this.func_174826_a(this.func_174813_aQ().func_72317_d(0.0, p_moveEntity_3_, 0.0));
                if (d8 * d8 + d10 * d10 >= p_moveEntity_1_ * p_moveEntity_1_ + p_moveEntity_5_ * p_moveEntity_5_) {
                    p_moveEntity_1_ = d8;
                    p_moveEntity_3_ = d9;
                    p_moveEntity_5_ = d10;
                    this.func_174826_a(axisalignedbb4);
                }
            }
            this.field_70170_p.field_72984_F.func_76319_b();
            this.field_70170_p.field_72984_F.func_76320_a("rest");
            this.func_174829_m();
            this.field_70123_F = (d4 != p_moveEntity_1_ || d6 != p_moveEntity_5_);
            this.field_70124_G = (d5 != p_moveEntity_3_);
            this.field_70122_E = (this.field_70124_G && d5 < 0.0);
            this.field_70132_H = (this.field_70123_F || this.field_70124_G);
            final int j = MathHelper.func_76128_c(this.field_70165_t);
            final int k = MathHelper.func_76128_c(this.field_70163_u - 0.20000000298023224);
            final int l = MathHelper.func_76128_c(this.field_70161_v);
            BlockPos blockpos = new BlockPos(j, k, l);
            Block block1 = this.field_70170_p.func_180495_p(blockpos).func_177230_c();
            if (block1.func_149688_o() == Material.field_151579_a) {
                final Block block2 = this.field_70170_p.func_180495_p(blockpos.func_177977_b()).func_177230_c();
                if (block2 instanceof BlockFence || block2 instanceof BlockWall || block2 instanceof BlockFenceGate) {
                    block1 = block2;
                    blockpos = blockpos.func_177977_b();
                }
            }
            this.func_180433_a(p_moveEntity_3_, this.field_70122_E, block1, blockpos);
            if (d4 != p_moveEntity_1_) {
                this.field_70159_w = 0.0;
            }
            if (d6 != p_moveEntity_5_) {
                this.field_70179_y = 0.0;
            }
            if (d5 != p_moveEntity_3_) {
                block1.func_176216_a(this.field_70170_p, (Entity)this);
            }
            if (this.func_70041_e_() && !flag && this.field_70154_o == null) {
                final double d19 = this.field_70165_t - d0;
                double d20 = this.field_70163_u - d2;
                final double d21 = this.field_70161_v - d3;
                if (block1 != Blocks.field_150468_ap) {
                    d20 = 0.0;
                }
                if (block1 != null && this.field_70122_E) {
                    block1.func_176199_a(this.field_70170_p, blockpos, (Entity)this);
                }
                this.field_70140_Q += (float)(MathHelper.func_76133_a(d19 * d19 + d21 * d21) * 0.6);
                this.field_82151_R += (float)(MathHelper.func_76133_a(d19 * d19 + d20 * d20 + d21 * d21) * 0.6);
                if (this.field_82151_R > this.field_70150_b && block1.func_149688_o() != Material.field_151579_a) {
                    this.field_70150_b = (int)this.field_82151_R + 1;
                    if (this.func_70090_H()) {
                        float f = MathHelper.func_76133_a(this.field_70159_w * this.field_70159_w * 0.20000000298023224 + this.field_70181_x * this.field_70181_x + this.field_70179_y * this.field_70179_y * 0.20000000298023224) * 0.35f;
                        if (f > 1.0f) {
                            f = 1.0f;
                        }
                        this.func_85030_a(this.func_145776_H(), f, 1.0f + (this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.4f);
                    }
                    this.func_180429_a(blockpos, block1);
                }
            }
            try {
                this.func_145775_I();
            }
            catch (Throwable var31) {
                final CrashReport crashreport = CrashReport.func_85055_a(var31, "Checking entity block collision");
                final CrashReportCategory crashreportcategory = crashreport.func_85058_a("Entity being checked for collision");
                this.func_85029_a(crashreportcategory);
                throw new ReportedException(crashreport);
            }
            final boolean flag3 = this.func_70026_G();
            if (this.field_70170_p.func_147470_e(this.func_174813_aQ().func_72331_e(0.001, 0.001, 0.001))) {
                this.func_70081_e(1);
                if (!flag3) {
                    ++this.field_70151_c;
                    if (this.field_70151_c == 0) {
                        this.func_70015_d(8);
                    }
                }
            }
            else if (this.field_70151_c <= 0) {
                this.field_70151_c = -this.field_70174_ab;
            }
            if (flag3 && this.field_70151_c > 0) {
                this.func_85030_a("random.fizz", 0.7f, 1.6f + (this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.4f);
                this.field_70151_c = -this.field_70174_ab;
            }
            this.field_70170_p.field_72984_F.func_76319_b();
        }
    }
    
    @Overwrite
    public void func_70060_a(float strafe, float forward, float fric) {
        final MoveInputEvent e = new MoveInputEvent(strafe, forward, fric, this.field_70177_z);
        if (this == Minecraft.func_71410_x().field_71439_g) {
            Raven.eventBus.post((Object)e);
        }
        strafe = e.getStrafe();
        forward = e.getForward();
        fric = e.getFriction();
        final float yaw = e.getYaw();
        float f = strafe * strafe + forward * forward;
        if (f >= 1.0E-4f) {
            f = MathHelper.func_76129_c(f);
            if (f < 1.0f) {
                f = 1.0f;
            }
            f = fric / f;
            strafe *= f;
            forward *= f;
            final float f2 = MathHelper.func_76126_a(yaw * 3.1415927f / 180.0f);
            final float f3 = MathHelper.func_76134_b(yaw * 3.1415927f / 180.0f);
            this.field_70159_w += strafe * f3 - forward * f2;
            this.field_70179_y += forward * f3 + strafe * f2;
        }
    }
}
