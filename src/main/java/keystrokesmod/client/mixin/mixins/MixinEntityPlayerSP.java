package keystrokesmod.client.mixin.mixins;

import net.minecraft.client.entity.*;
import net.minecraft.client.*;
import net.minecraft.client.network.*;
import net.minecraft.world.*;
import com.mojang.authlib.*;
import keystrokesmod.client.main.*;
import net.minecraft.entity.*;
import net.minecraft.network.*;
import keystrokesmod.client.event.*;
import keystrokesmod.client.event.impl.*;
import net.minecraft.network.play.client.*;
import org.spongepowered.asm.mixin.*;
import net.minecraft.client.gui.*;
import net.minecraft.util.*;
import net.minecraft.client.audio.*;
import net.minecraft.potion.*;
import keystrokesmod.client.module.*;
import keystrokesmod.client.module.modules.movement.*;
import keystrokesmod.client.module.modules.combat.*;

@Mixin(priority = 995, value = { EntityPlayerSP.class })
public abstract class MixinEntityPlayerSP extends AbstractClientPlayer
{
    @Shadow
    public int field_71157_e;
    @Shadow
    protected int field_71156_d;
    @Shadow
    public float field_71080_cy;
    @Shadow
    public float field_71086_bY;
    @Shadow
    protected Minecraft field_71159_c;
    @Shadow
    public MovementInput field_71158_b;
    @Shadow
    private int field_110320_a;
    @Shadow
    private float field_110321_bQ;
    @Shadow
    private boolean field_175171_bO;
    @Shadow
    @Final
    public NetHandlerPlayClient field_71174_a;
    @Shadow
    private boolean field_175170_bN;
    @Shadow
    private double field_175172_bI;
    @Shadow
    private double field_175166_bJ;
    @Shadow
    private double field_175167_bK;
    @Shadow
    private float field_175164_bL;
    @Shadow
    private float field_175165_bM;
    @Shadow
    private int field_175168_bP;
    
    public MixinEntityPlayerSP(final World p_i45074_1_, final GameProfile p_i45074_2_) {
        super(p_i45074_1_, p_i45074_2_);
    }
    
    @Shadow
    public abstract void func_70031_b(final boolean p0);
    
    @Shadow
    protected abstract boolean func_145771_j(final double p0, final double p1, final double p2);
    
    @Shadow
    public abstract void func_71016_p();
    
    @Shadow
    protected abstract boolean func_175160_A();
    
    @Shadow
    public abstract boolean func_110317_t();
    
    @Shadow
    protected abstract void func_110318_g();
    
    @Shadow
    public abstract boolean func_70093_af();
    
    @Overwrite
    public void func_175161_p() {
        Raven.eventBus.post((Object)new TickEvent());
        final boolean flag = this.func_70051_ag();
        if (flag != this.field_175171_bO) {
            if (flag) {
                this.field_71174_a.func_147297_a((Packet)new C0BPacketEntityAction((Entity)this, C0BPacketEntityAction.Action.START_SPRINTING));
            }
            else {
                this.field_71174_a.func_147297_a((Packet)new C0BPacketEntityAction((Entity)this, C0BPacketEntityAction.Action.STOP_SPRINTING));
            }
            this.field_175171_bO = flag;
        }
        final boolean flag2 = this.func_70093_af();
        if (flag2 != this.field_175170_bN) {
            if (flag2) {
                this.field_71174_a.func_147297_a((Packet)new C0BPacketEntityAction((Entity)this, C0BPacketEntityAction.Action.START_SNEAKING));
            }
            else {
                this.field_71174_a.func_147297_a((Packet)new C0BPacketEntityAction((Entity)this, C0BPacketEntityAction.Action.STOP_SNEAKING));
            }
            this.field_175170_bN = flag2;
        }
        if (this.func_175160_A()) {
            UpdateEvent e = new UpdateEvent(EventTiming.PRE, this.field_70165_t, this.func_174813_aQ().field_72338_b, this.field_70161_v, this.field_70177_z, this.field_70125_A, this.field_70122_E);
            Raven.eventBus.post((Object)e);
            final double d0 = e.getX() - this.field_175172_bI;
            final double d2 = e.getY() - this.field_175166_bJ;
            final double d3 = e.getZ() - this.field_175167_bK;
            final double d4 = e.getYaw() - this.field_175164_bL;
            final double d5 = e.getPitch() - this.field_175165_bM;
            boolean flag3 = d0 * d0 + d2 * d2 + d3 * d3 > 9.0E-4 || this.field_175168_bP >= 20;
            final boolean flag4 = d4 != 0.0 || d5 != 0.0;
            if (this.field_70154_o == null) {
                if (flag3 && flag4) {
                    this.field_71174_a.func_147297_a((Packet)new C03PacketPlayer.C06PacketPlayerPosLook(e.getX(), e.getY(), e.getZ(), e.getYaw(), e.getPitch(), e.isOnGround()));
                }
                else if (flag3) {
                    this.field_71174_a.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(e.getX(), e.getY(), e.getZ(), e.isOnGround()));
                }
                else if (flag4) {
                    this.field_71174_a.func_147297_a((Packet)new C03PacketPlayer.C05PacketPlayerLook(e.getYaw(), e.getPitch(), e.isOnGround()));
                }
                else {
                    this.field_71174_a.func_147297_a((Packet)new C03PacketPlayer(e.isOnGround()));
                }
            }
            else {
                this.field_71174_a.func_147297_a((Packet)new C03PacketPlayer.C06PacketPlayerPosLook(this.field_70159_w, -999.0, this.field_70179_y, e.getYaw(), e.getPitch(), e.isOnGround()));
                flag3 = false;
            }
            ++this.field_175168_bP;
            if (flag3) {
                this.field_175172_bI = e.getX();
                this.field_175166_bJ = e.getY();
                this.field_175167_bK = e.getZ();
                this.field_175168_bP = 0;
            }
            if (flag4) {
                this.field_175164_bL = e.getYaw();
                this.field_175165_bM = e.getPitch();
            }
            e = UpdateEvent.convertPost(e);
            Raven.eventBus.post((Object)e);
        }
    }
    
    @Overwrite
    public void func_70636_d() {
        if (this.field_71157_e > 0) {
            --this.field_71157_e;
            if (this.field_71157_e == 0) {
                this.func_70031_b(false);
            }
        }
        if (this.field_71156_d > 0) {
            --this.field_71156_d;
        }
        this.field_71080_cy = this.field_71086_bY;
        if (this.field_71087_bX) {
            if (this.field_71159_c.field_71462_r != null && !this.field_71159_c.field_71462_r.func_73868_f()) {
                this.field_71159_c.func_147108_a((GuiScreen)null);
            }
            if (this.field_71086_bY == 0.0f) {
                this.field_71159_c.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("portal.trigger"), this.field_70146_Z.nextFloat() * 0.4f + 0.8f));
            }
            this.field_71086_bY += 0.0125f;
            if (this.field_71086_bY >= 1.0f) {
                this.field_71086_bY = 1.0f;
            }
            this.field_71087_bX = false;
        }
        else if (this.func_70644_a(Potion.field_76431_k) && this.func_70660_b(Potion.field_76431_k).func_76459_b() > 60) {
            this.field_71086_bY += 0.006666667f;
            if (this.field_71086_bY > 1.0f) {
                this.field_71086_bY = 1.0f;
            }
        }
        else {
            if (this.field_71086_bY > 0.0f) {
                this.field_71086_bY -= 0.05f;
            }
            if (this.field_71086_bY < 0.0f) {
                this.field_71086_bY = 0.0f;
            }
        }
        if (this.field_71088_bW > 0) {
            --this.field_71088_bW;
        }
        final Module noSlow = Raven.moduleManager.getModuleByClazz(NoSlow.class);
        final Module sprint = Raven.moduleManager.getModuleByClazz(Sprint.class);
        final boolean flag = this.field_71158_b.field_78901_c;
        final boolean flag2 = this.field_71158_b.field_78899_d;
        final float f = 0.8f;
        final boolean flag3 = this.field_71158_b.field_78900_b >= f;
        this.field_71158_b.func_78898_a();
        if ((this.func_71039_bw() || Aura.doSlowdown()) && !this.func_70115_ae()) {
            final MovementInput var10000 = this.field_71158_b;
            if (noSlow.isEnabled()) {
                final float slowdown = (float)((100.0 - NoSlow.b.getInput()) / 100.0);
                final MovementInput movementInput = var10000;
                movementInput.field_78902_a *= slowdown;
                final MovementInput movementInput2 = var10000;
                movementInput2.field_78900_b *= slowdown;
            }
            else {
                final MovementInput movementInput3 = var10000;
                movementInput3.field_78902_a *= 0.2f;
                final MovementInput movementInput4 = var10000;
                movementInput4.field_78900_b *= 0.2f;
                this.field_71156_d = 0;
            }
        }
        this.func_145771_j(this.field_70165_t - this.field_70130_N * 0.35, this.func_174813_aQ().field_72338_b + 0.5, this.field_70161_v + this.field_70130_N * 0.35);
        this.func_145771_j(this.field_70165_t - this.field_70130_N * 0.35, this.func_174813_aQ().field_72338_b + 0.5, this.field_70161_v - this.field_70130_N * 0.35);
        this.func_145771_j(this.field_70165_t + this.field_70130_N * 0.35, this.func_174813_aQ().field_72338_b + 0.5, this.field_70161_v - this.field_70130_N * 0.35);
        this.func_145771_j(this.field_70165_t + this.field_70130_N * 0.35, this.func_174813_aQ().field_72338_b + 0.5, this.field_70161_v + this.field_70130_N * 0.35);
        final boolean flag4 = this.func_71024_bL().func_75116_a() > 6.0f || this.field_71075_bZ.field_75101_c;
        if (this.field_70122_E && !flag2 && !flag3 && (this.field_71158_b.field_78900_b >= f || (sprint.isEnabled() && Sprint.multiDir.isToggled() && (this.field_71158_b.field_78900_b != 0.0f || this.field_71158_b.field_78902_a != 0.0f))) && !this.func_70051_ag() && flag4 && (!this.func_71039_bw() || noSlow.isEnabled()) && (!this.func_70644_a(Potion.field_76440_q) || (sprint.isEnabled() && Sprint.ignoreBlindness.isToggled()))) {
            if (this.field_71156_d <= 0 && !this.field_71159_c.field_71474_y.field_151444_V.func_151470_d()) {
                this.field_71156_d = 7;
            }
            else {
                this.func_70031_b(true);
            }
        }
        if (!this.func_70051_ag() && (this.field_71158_b.field_78900_b >= f || (sprint.isEnabled() && Sprint.multiDir.isToggled() && (this.field_71158_b.field_78900_b != 0.0f || this.field_71158_b.field_78902_a != 0.0f))) && flag4 && (!this.func_71039_bw() || noSlow.isEnabled()) && (!this.func_70644_a(Potion.field_76440_q) || (sprint.isEnabled() && Sprint.ignoreBlindness.isToggled())) && this.field_71159_c.field_71474_y.field_151444_V.func_151470_d()) {
            this.func_70031_b(true);
        }
        Label_1029: {
            if (this.func_70051_ag()) {
                Label_1024: {
                    if (sprint.isEnabled() && Sprint.multiDir.isToggled()) {
                        if (this.field_71158_b.field_78900_b == 0.0f && this.field_71158_b.field_78902_a == 0.0f) {
                            break Label_1024;
                        }
                    }
                    else if (this.field_71158_b.field_78900_b < f) {
                        break Label_1024;
                    }
                    if (!this.field_70123_F && flag4) {
                        break Label_1029;
                    }
                }
                this.func_70031_b(false);
            }
        }
        if (this.field_71075_bZ.field_75101_c) {
            if (this.field_71159_c.field_71442_b.func_178887_k()) {
                if (!this.field_71075_bZ.field_75100_b) {
                    this.field_71075_bZ.field_75100_b = true;
                    this.func_71016_p();
                }
            }
            else if (!flag && this.field_71158_b.field_78901_c) {
                if (this.field_71101_bC == 0) {
                    this.field_71101_bC = 7;
                }
                else {
                    this.field_71075_bZ.field_75100_b = !this.field_71075_bZ.field_75100_b;
                    this.func_71016_p();
                    this.field_71101_bC = 0;
                }
            }
        }
        if (this.field_71075_bZ.field_75100_b && this.func_175160_A()) {
            if (this.field_71158_b.field_78899_d) {
                this.field_70181_x -= this.field_71075_bZ.func_75093_a() * 3.0f;
            }
            if (this.field_71158_b.field_78901_c) {
                this.field_70181_x += this.field_71075_bZ.func_75093_a() * 3.0f;
            }
        }
        if (this.func_110317_t()) {
            if (this.field_110320_a < 0) {
                ++this.field_110320_a;
                if (this.field_110320_a == 0) {
                    this.field_110321_bQ = 0.0f;
                }
            }
            if (flag && !this.field_71158_b.field_78901_c) {
                this.field_110320_a = -10;
                this.func_110318_g();
            }
            else if (!flag && this.field_71158_b.field_78901_c) {
                this.field_110320_a = 0;
                this.field_110321_bQ = 0.0f;
            }
            else if (flag) {
                ++this.field_110320_a;
                if (this.field_110320_a < 10) {
                    this.field_110321_bQ = this.field_110320_a * 0.1f;
                }
                else {
                    this.field_110321_bQ = 0.8f + 2.0f / (this.field_110320_a - 9) * 0.1f;
                }
            }
        }
        else {
            this.field_110321_bQ = 0.0f;
        }
        super.func_70636_d();
        if (this.field_70122_E && this.field_71075_bZ.field_75100_b && !this.field_71159_c.field_71442_b.func_178887_k()) {
            this.field_71075_bZ.field_75100_b = false;
            this.func_71016_p();
        }
    }
}
