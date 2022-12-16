package keystrokesmod.client.module.modules.combat;

import keystrokesmod.client.module.*;
import net.minecraft.entity.player.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import org.lwjgl.input.*;
import keystrokesmod.client.utils.*;
import keystrokesmod.client.module.modules.world.*;
import net.minecraft.entity.*;
import net.minecraft.world.*;
import com.google.common.eventbus.*;
import net.minecraftforge.client.event.*;
import keystrokesmod.client.module.modules.render.*;
import java.awt.*;
import net.minecraft.network.play.server.*;
import keystrokesmod.client.event.impl.*;
import net.minecraft.client.settings.*;

public class KvAura extends Module
{
    private EntityPlayer target;
    public DoubleSliderSetting reach;
    private final SliderSetting rotationDistance;
    private final SliderSetting fov;
    private final DoubleSliderSetting cps;
    private final TickSetting disableOnTp;
    private final TickSetting disableWhenFlying;
    private final TickSetting mouseDown;
    private final TickSetting onlySurvival;
    private final TickSetting correctStrafe;
    private CoolDown coolDown;
    private boolean leftDown;
    private boolean leftn;
    private long leftDownTime;
    private long leftUpTime;
    private long leftk;
    private long leftl;
    private float yaw;
    private float pitch;
    private double leftm;
    
    public KvAura() {
        super("KvAura", ModuleCategory.combat);
        this.coolDown = new CoolDown(1L);
        this.registerSetting(this.reach = new DoubleSliderSetting("Reach (Blocks)", 3.1, 3.3, 3.0, 6.0, 0.05));
        this.registerSetting(this.rotationDistance = new SliderSetting("Rotation Range", 3.5, 3.0, 6.0, 0.05));
        this.registerSetting(this.cps = new DoubleSliderSetting("Left CPS", 9.0, 13.0, 1.0, 60.0, 0.5));
        this.registerSetting(this.fov = new SliderSetting("Fov", 30.0, 0.0, 360.0, 1.0));
        this.registerSetting(this.onlySurvival = new TickSetting("Only Survival", true));
        this.registerSetting(this.disableOnTp = new TickSetting("Disable after tp", true));
        this.registerSetting(this.disableWhenFlying = new TickSetting("Disable when flying", true));
        this.registerSetting(this.correctStrafe = new TickSetting("Strafe Correctly", true));
        this.registerSetting(this.mouseDown = new TickSetting("Mouse Down", true));
    }
    
    @Subscribe
    public void onUpdate(final UpdateEvent e) {
        if (!e.isPre()) {
            return;
        }
        Mouse.poll();
        this.target = Utils.Player.getClosestPlayer((float)this.rotationDistance.getInput());
        if ((this.target == null || !AntiBot.bot((Entity)this.target) || KvAura.mc.field_71462_r != null || (this.onlySurvival.isToggled() && KvAura.mc.field_71442_b.func_178889_l() != WorldSettings.GameType.SURVIVAL) || !this.coolDown.hasFinished() || (this.mouseDown.isToggled() && !Mouse.isButtonDown(0)) || (this.disableWhenFlying.isToggled() && KvAura.mc.field_71439_g.field_71075_bZ.field_75100_b)) && !Utils.Player.fov((Entity)this.target, (float)this.fov.getInput())) {
            this.target = null;
            this.yaw = KvAura.mc.field_71439_g.field_70177_z;
            this.pitch = KvAura.mc.field_71439_g.field_70125_A;
            return;
        }
        this.ravenClick();
        final float[] i = Utils.Player.getTargetRotations((Entity)this.target);
        this.yaw = i[0];
        this.pitch = i[1] + 4.0f;
        KvAura.mc.field_71439_g.func_70034_d(this.yaw);
        e.setYaw(this.yaw);
        e.setPitch(this.pitch);
    }
    
    @Subscribe
    public void renderWorldLast(final ForgeEvent fe) {
        if (fe.getEvent() instanceof RenderWorldLastEvent && this.target != null && !PlayerESP.t2.isToggled()) {
            final int red = (int)(((20.0f - this.target.func_110143_aJ()) * 13.0f > 255.0f) ? 255.0f : ((20.0f - this.target.func_110143_aJ()) * 13.0f));
            final int green = 255 - red;
            final int rgb = new Color(red, green, 0).getRGB();
            Utils.HUD.drawBoxAroundEntity((Entity)this.target, 2, 0.0, 0.0, rgb, false);
        }
    }
    
    @Subscribe
    public void packetEvent(final PacketEvent e) {
        if (e.getPacket() instanceof S08PacketPlayerPosLook) {
            if (this.mouseDown.isToggled()) {
                if (this.coolDown.getTimeLeft() < 2000L) {
                    this.coolDown.setCooldown(2000L);
                    this.coolDown.start();
                }
            }
            else if (this.disableOnTp.isToggled()) {
                this.setToggled(false);
            }
        }
    }
    
    @Subscribe
    public void move(final MoveInputEvent e) {
        if (this.target != null && this.correctStrafe.isToggled()) {
            e.setYaw(this.yaw);
        }
    }
    
    public void click() {
        if (this.target == null || KvAura.mc.field_71439_g.func_70032_d((Entity)this.target) >= Utils.Client.ranModuleVal(this.reach, Utils.Java.rand()) || !KvAura.mc.field_71439_g.func_70632_aY()) {}
    }
    
    private void ravenClick() {
        Mouse.poll();
        if (!Mouse.isButtonDown(0)) {
            KeyBinding.func_74510_a(KvAura.mc.field_71474_y.field_74312_F.func_151463_i(), false);
            Utils.Client.setMouseButtonState(0, false);
        }
        if (Mouse.isButtonDown(0)) {
            this.leftClickExecute(KvAura.mc.field_71474_y.field_74312_F.func_151463_i());
        }
    }
    
    public void leftClickExecute(final int key) {
        if (this.leftUpTime > 0L && this.leftDownTime > 0L) {
            if (System.currentTimeMillis() > this.leftUpTime && this.leftDown) {
                KeyBinding.func_74510_a(key, true);
                this.click();
                KeyBinding.func_74507_a(key);
                this.genLeftTimings();
                Utils.Client.setMouseButtonState(0, true);
                this.leftDown = false;
            }
            else if (System.currentTimeMillis() > this.leftDownTime) {
                KeyBinding.func_74510_a(key, false);
                this.leftDown = true;
                Utils.Client.setMouseButtonState(0, false);
            }
        }
        else {
            this.genLeftTimings();
        }
    }
    
    public void genLeftTimings() {
        final double clickSpeed = Utils.Client.ranModuleVal(this.cps, Utils.Java.rand()) + 0.4 * Utils.Java.rand().nextDouble();
        long delay = (int)Math.round(1000.0 / clickSpeed);
        if (System.currentTimeMillis() > this.leftk) {
            if (!this.leftn && Utils.Java.rand().nextInt(100) >= 85) {
                this.leftn = true;
                this.leftm = 1.1 + Utils.Java.rand().nextDouble() * 0.15;
            }
            else {
                this.leftn = false;
            }
            this.leftk = System.currentTimeMillis() + 500L + Utils.Java.rand().nextInt(1500);
        }
        if (this.leftn) {
            delay *= (long)this.leftm;
        }
        if (System.currentTimeMillis() > this.leftl) {
            if (Utils.Java.rand().nextInt(100) >= 80) {
                delay += 50L + Utils.Java.rand().nextInt(100);
            }
            this.leftl = System.currentTimeMillis() + 500L + Utils.Java.rand().nextInt(1500);
        }
        this.leftUpTime = System.currentTimeMillis() + delay;
        this.leftDownTime = System.currentTimeMillis() + delay / 2L - Utils.Java.rand().nextInt(10);
    }
}
