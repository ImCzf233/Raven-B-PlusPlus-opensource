package keystrokesmod.client.module.modules.player;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.utils.*;
import keystrokesmod.client.event.impl.*;
import keystrokesmod.client.main.*;
import net.minecraft.client.entity.*;
import com.google.common.eventbus.*;

public class BridgeAssist extends Module
{
    private final TickSetting setLook;
    private final TickSetting onSneak;
    private final TickSetting workWithSafeWalk;
    private final SliderSetting waitFor;
    private final SliderSetting glideTime;
    private final SliderSetting assistMode;
    private final SliderSetting assistRange;
    private final DescriptionSetting assistModeDesc;
    DescriptionSetting goodAdvice;
    private boolean waitingForAim;
    private boolean gliding;
    private long startWaitTime;
    private final float[] godbridgePos;
    private final float[] moonwalkPos;
    private final float[] breezilyPos;
    private final float[] normalPos;
    private double speedYaw;
    private double speedPitch;
    private float waitingForYaw;
    private float waitingForPitch;
    
    public BridgeAssist() {
        super("Bridge Assist", ModuleCategory.player);
        this.godbridgePos = new float[] { 75.6f, -315.0f, -225.0f, -135.0f, -45.0f, 0.0f, 45.0f, 135.0f, 225.0f, 315.0f };
        this.moonwalkPos = new float[] { 79.6f, -340.0f, -290.0f, -250.0f, -200.0f, -160.0f, -110.0f, -70.0f, -20.0f, 0.0f, 20.0f, 70.0f, 110.0f, 160.0f, 200.0f, 250.0f, 290.0f, 340.0f };
        this.breezilyPos = new float[] { 79.9f, -360.0f, -270.0f, -180.0f, -90.0f, 0.0f, 90.0f, 180.0f, 270.0f, 360.0f };
        this.normalPos = new float[] { 78.0f, -315.0f, -225.0f, -135.0f, -45.0f, 0.0f, 45.0f, 135.0f, 225.0f, 315.0f };
        this.registerSetting(this.goodAdvice = new DescriptionSetting("Best with fastplace, not autoplace"));
        this.registerSetting(this.waitFor = new SliderSetting("Wait time (ms)", 500.0, 0.0, 5000.0, 25.0));
        this.registerSetting(this.setLook = new TickSetting("Set look pos", true));
        this.registerSetting(this.onSneak = new TickSetting("Work only when sneaking", true));
        this.registerSetting(this.workWithSafeWalk = new TickSetting("Work with safewalk", false));
        this.registerSetting(this.assistRange = new SliderSetting("Assist range", 10.0, 1.0, 40.0, 1.0));
        this.registerSetting(this.glideTime = new SliderSetting("Glide speed", 500.0, 1.0, 201.0, 5.0));
        this.registerSetting(this.assistMode = new SliderSetting("Value", 1.0, 1.0, 4.0, 1.0));
        this.registerSetting(this.assistModeDesc = new DescriptionSetting("Mode: GodBridge"));
    }
    
    @Override
    public void guiUpdate() {
        this.assistModeDesc.setDesc("Mode: " + Utils.Modes.BridgeMode.values()[(int)(this.assistMode.getInput() - 1.0)].name());
    }
    
    @Override
    public void onEnable() {
        this.waitingForAim = false;
        this.gliding = false;
        super.onEnable();
    }
    
    @Subscribe
    public void onRender2D(final Render2DEvent e) {
        if (!Utils.Player.isPlayerInGame()) {
            return;
        }
        final Module safeWalk = Raven.moduleManager.getModuleByClazz(SafeWalk.class);
        if (safeWalk != null && safeWalk.isEnabled() && !this.workWithSafeWalk.isToggled()) {
            return;
        }
        if (!Utils.Player.playerOverAir() || !BridgeAssist.mc.field_71439_g.field_70122_E) {
            return;
        }
        if (this.onSneak.isToggled() && !BridgeAssist.mc.field_71439_g.func_70093_af()) {
            return;
        }
        if (this.gliding) {
            final float fuckedYaw = BridgeAssist.mc.field_71439_g.field_70177_z;
            final float fuckedPitch = BridgeAssist.mc.field_71439_g.field_70125_A;
            final float yaw = fuckedYaw - (int)fuckedYaw / 360.0f * 360.0f;
            final float pitch = fuckedPitch - (int)fuckedPitch / 360.0f * 360.0f;
            double ilovebloat1 = yaw - this.speedYaw;
            double ilovebloat2 = yaw + this.speedYaw;
            double ilovebloat3 = pitch - this.speedPitch;
            double ilovebloat4 = pitch + this.speedPitch;
            if (ilovebloat1 < 0.0) {
                ilovebloat1 *= -1.0;
            }
            if (ilovebloat2 < 0.0) {
                ilovebloat2 *= -1.0;
            }
            if (ilovebloat3 < 0.0) {
                ilovebloat3 *= -1.0;
            }
            if (ilovebloat4 < 0.0) {
                ilovebloat4 *= -1.0;
            }
            if (this.speedYaw > ilovebloat1 || this.speedYaw > ilovebloat2) {
                BridgeAssist.mc.field_71439_g.field_70177_z = this.waitingForYaw;
            }
            if (this.speedPitch > ilovebloat3 || this.speedPitch > ilovebloat4) {
                BridgeAssist.mc.field_71439_g.field_70125_A = this.waitingForPitch;
            }
            if (BridgeAssist.mc.field_71439_g.field_70177_z < this.waitingForYaw) {
                final EntityPlayerSP field_71439_g = BridgeAssist.mc.field_71439_g;
                field_71439_g.field_70177_z += (float)this.speedYaw;
            }
            if (BridgeAssist.mc.field_71439_g.field_70177_z > this.waitingForYaw) {
                final EntityPlayerSP field_71439_g2 = BridgeAssist.mc.field_71439_g;
                field_71439_g2.field_70177_z -= (float)this.speedYaw;
            }
            if (BridgeAssist.mc.field_71439_g.field_70125_A > this.waitingForPitch) {
                final EntityPlayerSP field_71439_g3 = BridgeAssist.mc.field_71439_g;
                field_71439_g3.field_70125_A -= (float)this.speedPitch;
            }
            if (BridgeAssist.mc.field_71439_g.field_70177_z == this.waitingForYaw && BridgeAssist.mc.field_71439_g.field_70125_A == this.waitingForPitch) {
                this.gliding = false;
                this.waitingForAim = false;
            }
            return;
        }
        if (!this.waitingForAim) {
            this.waitingForAim = true;
            this.startWaitTime = System.currentTimeMillis();
            return;
        }
        if (System.currentTimeMillis() - this.startWaitTime < this.waitFor.getInput()) {
            return;
        }
        final float fuckedYaw = BridgeAssist.mc.field_71439_g.field_70177_z;
        final float fuckedPitch = BridgeAssist.mc.field_71439_g.field_70125_A;
        final float yaw = fuckedYaw - (int)fuckedYaw / 360.0f * 360.0f;
        final float pitch = fuckedPitch - (int)fuckedPitch / 360.0f * 360.0f;
        final float range = (float)this.assistRange.getInput();
        switch (Utils.Modes.BridgeMode.values()[(int)(this.assistMode.getInput() - 1.0)]) {
            case GODBRIDGE: {
                if (this.godbridgePos[0] >= pitch - range && this.godbridgePos[0] <= pitch + range) {
                    for (int k = 1; k < this.godbridgePos.length; ++k) {
                        if (this.godbridgePos[k] >= yaw - range && this.godbridgePos[k] <= yaw + range) {
                            this.aimAt(this.godbridgePos[0], this.godbridgePos[k], fuckedPitch);
                            this.waitingForAim = false;
                            return;
                        }
                    }
                }
            }
            case MOONWALK: {
                if (this.moonwalkPos[0] >= pitch - range && this.moonwalkPos[0] <= pitch + range) {
                    for (int k = 1; k < this.moonwalkPos.length; ++k) {
                        if (this.moonwalkPos[k] >= yaw - range && this.moonwalkPos[k] <= yaw + range) {
                            this.aimAt(this.moonwalkPos[0], this.moonwalkPos[k], fuckedPitch);
                            this.waitingForAim = false;
                            return;
                        }
                    }
                }
            }
            case BREEZILY: {
                if (this.breezilyPos[0] >= pitch - range && this.breezilyPos[0] <= pitch + range) {
                    for (int k = 1; k < this.breezilyPos.length; ++k) {
                        if (this.breezilyPos[k] >= yaw - range && this.breezilyPos[k] <= yaw + range) {
                            this.aimAt(this.breezilyPos[0], this.breezilyPos[k], fuckedPitch);
                            this.waitingForAim = false;
                            return;
                        }
                    }
                }
            }
            case NORMAL: {
                if (this.normalPos[0] >= pitch - range && this.normalPos[0] <= pitch + range) {
                    for (int k = 1; k < this.normalPos.length; ++k) {
                        if (this.normalPos[k] >= yaw - range && this.normalPos[k] <= yaw + range) {
                            this.aimAt(this.normalPos[0], this.normalPos[k], fuckedPitch);
                            this.waitingForAim = false;
                            return;
                        }
                    }
                    break;
                }
                break;
            }
        }
        this.waitingForAim = false;
    }
    
    public void aimAt(final float pitch, final float yaw, final float fuckedPitch) {
        if (this.setLook.isToggled()) {
            BridgeAssist.mc.field_71439_g.field_70125_A = pitch + (int)fuckedPitch / 360.0f * 360.0f;
            BridgeAssist.mc.field_71439_g.field_70177_z = yaw;
        }
    }
}
