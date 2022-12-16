package keystrokesmod.client.module.modules.player;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.utils.*;
import org.lwjgl.input.*;
import net.minecraft.item.*;
import com.google.common.eventbus.*;
import keystrokesmod.client.event.impl.*;
import net.minecraft.client.gui.*;
import java.awt.*;
import keystrokesmod.client.main.*;
import net.minecraft.client.settings.*;

public class SafeWalk extends Module
{
    public static TickSetting doShift;
    public static TickSetting blocksOnly;
    public static TickSetting shiftOnJump;
    public static TickSetting onHold;
    public static TickSetting showBlockAmount;
    public static TickSetting lookDown;
    public static TickSetting shawtyMoment;
    public static DoubleSliderSetting pitchRange;
    public static SliderSetting blockShowMode;
    public static DescriptionSetting blockShowModeDesc;
    public static DoubleSliderSetting shiftTime;
    private static boolean shouldBridge;
    private static boolean isShifting;
    private boolean allowedShift;
    private final CoolDown shiftTimer;
    
    public SafeWalk() {
        super("SafeWalk", ModuleCategory.player);
        this.shiftTimer = new CoolDown(0L);
        this.registerSetting(SafeWalk.doShift = new TickSetting("Shift", false));
        this.registerSetting(SafeWalk.shiftOnJump = new TickSetting("Shift during jumps", false));
        this.registerSetting(SafeWalk.shiftTime = new DoubleSliderSetting("Shift time: (s)", 140.0, 200.0, 0.0, 280.0, 5.0));
        this.registerSetting(SafeWalk.onHold = new TickSetting("On shift hold", false));
        this.registerSetting(SafeWalk.blocksOnly = new TickSetting("Blocks only", true));
        this.registerSetting(SafeWalk.showBlockAmount = new TickSetting("Show amount of blocks", true));
        this.registerSetting(SafeWalk.blockShowMode = new SliderSetting("Block display info:", 2.0, 1.0, 2.0, 1.0));
        this.registerSetting(SafeWalk.blockShowModeDesc = new DescriptionSetting("Mode: "));
        this.registerSetting(SafeWalk.lookDown = new TickSetting("Only when looking down", true));
        this.registerSetting(SafeWalk.pitchRange = new DoubleSliderSetting("Pitch min range:", 70.0, 85.0, 0.0, 90.0, 1.0));
        this.registerSetting(SafeWalk.shawtyMoment = new TickSetting("Shawty Moment", true));
    }
    
    @Override
    public void onDisable() {
        if (SafeWalk.doShift.isToggled() && Utils.Player.playerOverAir()) {
            this.setShift(false);
        }
        SafeWalk.shouldBridge = false;
        SafeWalk.isShifting = false;
    }
    
    @Override
    public void guiUpdate() {
        SafeWalk.blockShowModeDesc.setDesc("Mode: " + BlockAmountInfo.values()[(int)SafeWalk.blockShowMode.getInput() - 1]);
    }
    
    @Subscribe
    public void onTick(final TickEvent e) {
        if (!Utils.Client.currentScreenMinecraft()) {
            return;
        }
        if (!Utils.Player.isPlayerInGame()) {
            return;
        }
        final boolean shiftTimeSettingActive = SafeWalk.shiftTime.getInputMax() > 0.0;
        if (SafeWalk.doShift.isToggled()) {
            if (SafeWalk.lookDown.isToggled() && (SafeWalk.mc.field_71439_g.field_70125_A < SafeWalk.pitchRange.getInputMin() || SafeWalk.mc.field_71439_g.field_70125_A > SafeWalk.pitchRange.getInputMax())) {
                SafeWalk.shouldBridge = false;
                if (Keyboard.isKeyDown(SafeWalk.mc.field_71474_y.field_74311_E.func_151463_i())) {
                    this.setShift(true);
                }
                return;
            }
            if (SafeWalk.onHold.isToggled() && !Keyboard.isKeyDown(SafeWalk.mc.field_71474_y.field_74311_E.func_151463_i())) {
                SafeWalk.shouldBridge = false;
                return;
            }
            if (SafeWalk.blocksOnly.isToggled()) {
                final ItemStack i = SafeWalk.mc.field_71439_g.func_70694_bm();
                if (i == null || !(i.func_77973_b() instanceof ItemBlock)) {
                    if (SafeWalk.isShifting) {
                        this.setShift(SafeWalk.isShifting = false);
                    }
                    return;
                }
            }
            if (SafeWalk.shawtyMoment.isToggled() && SafeWalk.mc.field_71439_g.field_71158_b.field_78900_b >= 0.0f) {
                SafeWalk.shouldBridge = false;
                return;
            }
            if (SafeWalk.mc.field_71439_g.field_70122_E) {
                if (Utils.Player.playerOverAir()) {
                    if (shiftTimeSettingActive) {
                        this.shiftTimer.setCooldown(Utils.Java.randomInt(SafeWalk.shiftTime.getInputMin(), SafeWalk.shiftTime.getInputMax() + 0.1));
                        this.shiftTimer.start();
                    }
                    this.setShift(SafeWalk.isShifting = true);
                    SafeWalk.shouldBridge = true;
                }
                else if (SafeWalk.mc.field_71439_g.func_70093_af() && !Keyboard.isKeyDown(SafeWalk.mc.field_71474_y.field_74311_E.func_151463_i()) && SafeWalk.onHold.isToggled()) {
                    SafeWalk.isShifting = false;
                    this.setShift(SafeWalk.shouldBridge = false);
                }
                else if (SafeWalk.onHold.isToggled() && !Keyboard.isKeyDown(SafeWalk.mc.field_71474_y.field_74311_E.func_151463_i())) {
                    SafeWalk.isShifting = false;
                    this.setShift(SafeWalk.shouldBridge = false);
                }
                else if (SafeWalk.mc.field_71439_g.func_70093_af() && Keyboard.isKeyDown(SafeWalk.mc.field_71474_y.field_74311_E.func_151463_i()) && SafeWalk.onHold.isToggled() && (!shiftTimeSettingActive || this.shiftTimer.hasFinished())) {
                    this.setShift(SafeWalk.isShifting = false);
                    SafeWalk.shouldBridge = true;
                }
                else if (SafeWalk.mc.field_71439_g.func_70093_af() && !SafeWalk.onHold.isToggled() && (!shiftTimeSettingActive || this.shiftTimer.hasFinished())) {
                    this.setShift(SafeWalk.isShifting = false);
                    SafeWalk.shouldBridge = true;
                }
            }
            else if (SafeWalk.shouldBridge && SafeWalk.mc.field_71439_g.field_71075_bZ.field_75100_b) {
                this.setShift(false);
                SafeWalk.shouldBridge = false;
            }
            else if (SafeWalk.shouldBridge && Utils.Player.playerOverAir() && SafeWalk.shiftOnJump.isToggled()) {
                this.setShift(SafeWalk.isShifting = true);
            }
            else {
                this.setShift(SafeWalk.isShifting = false);
            }
        }
    }
    
    @Subscribe
    public void onRender2D(final Render2DEvent e) {
        if (!SafeWalk.showBlockAmount.isToggled() || !Utils.Player.isPlayerInGame()) {
            return;
        }
        if (SafeWalk.mc.field_71462_r == null && SafeWalk.shouldBridge) {
            final ScaledResolution res = new ScaledResolution(SafeWalk.mc);
            int totalBlocks = 0;
            if (BlockAmountInfo.values()[(int)SafeWalk.blockShowMode.getInput() - 1] == BlockAmountInfo.BLOCKS_IN_CURRENT_STACK) {
                totalBlocks = Utils.Player.getBlockAmountInCurrentStack(SafeWalk.mc.field_71439_g.field_71071_by.field_70461_c);
            }
            else {
                for (int slot = 0; slot < 36; ++slot) {
                    totalBlocks += Utils.Player.getBlockAmountInCurrentStack(slot);
                }
            }
            if (totalBlocks <= 0) {
                return;
            }
            int rgb;
            if (totalBlocks < 16.0) {
                rgb = Color.red.getRGB();
            }
            else if (totalBlocks < 32.0) {
                rgb = Color.orange.getRGB();
            }
            else if (totalBlocks < 128.0) {
                rgb = Color.yellow.getRGB();
            }
            else if (totalBlocks > 128.0) {
                rgb = Color.green.getRGB();
            }
            else {
                rgb = Color.black.getRGB();
            }
            final String t = totalBlocks + " blocks";
            final int x = res.func_78326_a() / 2 - SafeWalk.mc.field_71466_p.func_78256_a(t) / 2;
            int y;
            if (Raven.debugger) {
                y = res.func_78328_b() / 2 + 17 + SafeWalk.mc.field_71466_p.field_78288_b;
            }
            else {
                y = res.func_78328_b() / 2 + 15;
            }
            SafeWalk.mc.field_71466_p.func_175065_a(t, (float)x, (float)y, rgb, false);
        }
    }
    
    private void setShift(final boolean sh) {
        KeyBinding.func_74510_a(SafeWalk.mc.field_71474_y.field_74311_E.func_151463_i(), sh);
    }
    
    public enum BlockAmountInfo
    {
        BLOCKS_IN_TOTAL, 
        BLOCKS_IN_CURRENT_STACK;
    }
}
