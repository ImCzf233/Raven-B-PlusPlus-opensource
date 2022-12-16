package keystrokesmod.client.module.modules.minigames.Sumo;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import java.util.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.module.modules.player.*;
import net.minecraft.client.gui.*;
import net.minecraftforge.fml.relauncher.*;
import keystrokesmod.client.utils.*;
import net.minecraft.client.*;
import com.google.common.eventbus.*;
import keystrokesmod.client.event.impl.*;
import net.minecraft.client.gui.inventory.*;
import io.netty.util.internal.*;
import net.minecraft.client.settings.*;
import net.minecraft.client.entity.*;
import java.lang.reflect.*;
import net.minecraft.init.*;
import net.minecraft.util.*;
import net.minecraft.block.*;
import org.lwjgl.input.*;

public class SumoClicker extends Module
{
    public static DescriptionSetting bestWithDelayRemover;
    public static SliderSetting jitterLeft;
    public static TickSetting weaponOnly;
    public static TickSetting breakBlocks;
    public static DoubleSliderSetting leftCPS;
    public static TickSetting inventoryFill;
    public static ComboSetting clickStyle;
    public static ComboSetting clickTimings;
    private long lastClick;
    private long leftHold;
    public boolean allowedClick;
    public static boolean autoClickerEnabled;
    private boolean leftDown;
    private long leftDownTime;
    private long leftUpTime;
    private long leftk;
    private long leftl;
    private double leftm;
    private boolean leftn;
    private boolean breakHeld;
    private Random rand;
    private Method playerMouseInput;
    
    public SumoClicker() {
        super("Sumo Clicker", ModuleCategory.sumo);
        this.registerSetting(SumoClicker.bestWithDelayRemover = new DescriptionSetting("Best with delay remover."));
        this.registerSetting(SumoClicker.leftCPS = new DoubleSliderSetting("Left CPS", 9.0, 13.0, 1.0, 60.0, 0.5));
        this.registerSetting(SumoClicker.jitterLeft = new SliderSetting("Jitter left", 0.0, 0.0, 3.0, 0.1));
        this.registerSetting(SumoClicker.inventoryFill = new TickSetting("Inventory fill", false));
        this.registerSetting(SumoClicker.weaponOnly = new TickSetting("Weapon only", false));
        this.registerSetting(SumoClicker.breakBlocks = new TickSetting("Break blocks", false));
        this.registerSetting(SumoClicker.clickTimings = new ComboSetting("Click event", (T)RightClicker.ClickEvent.Render));
        this.registerSetting(SumoClicker.clickStyle = new ComboSetting("Click Style", (T)RightClicker.ClickStyle.Raven));
        try {
            this.playerMouseInput = ReflectionHelper.findMethod((Class)GuiScreen.class, (Object)null, new String[] { "func_73864_a", "mouseClicked" }, new Class[] { Integer.TYPE, Integer.TYPE, Integer.TYPE });
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        if (this.playerMouseInput != null) {
            this.playerMouseInput.setAccessible(true);
        }
        SumoClicker.autoClickerEnabled = false;
    }
    
    @Override
    public void onEnable() {
        if (this.playerMouseInput == null) {
            this.disable();
        }
        this.allowedClick = false;
        this.rand = new Random();
        SumoClicker.autoClickerEnabled = true;
    }
    
    @Override
    public void onDisable() {
        this.leftDownTime = 0L;
        this.leftUpTime = 0L;
        SumoClicker.autoClickerEnabled = false;
    }
    
    @Subscribe
    public void onRender2D(final Render2DEvent e) {
        if (!Utils.Client.currentScreenMinecraft() && !(Minecraft.func_71410_x().field_71462_r instanceof GuiInventory) && !(Minecraft.func_71410_x().field_71462_r instanceof GuiContainer)) {
            return;
        }
        if (SumoClicker.clickTimings.getMode() != RightClicker.ClickEvent.Render) {
            return;
        }
        if (SumoClicker.clickStyle.getMode() == RightClicker.ClickStyle.Raven) {
            this.ravenClick();
        }
        else if (SumoClicker.clickStyle.getMode() == RightClicker.ClickStyle.SKid) {
            this.skidClick();
        }
    }
    
    @Subscribe
    public void onTick(final TickEvent tick) {
        if (!Utils.Client.currentScreenMinecraft() && !(Minecraft.func_71410_x().field_71462_r instanceof GuiInventory) && !(Minecraft.func_71410_x().field_71462_r instanceof GuiChest)) {
            return;
        }
        if (SumoClicker.clickTimings.getMode() != RightClicker.ClickEvent.Tick) {
            return;
        }
        if (SumoClicker.clickStyle.getMode() == RightClicker.ClickStyle.Raven) {
            this.ravenClick();
        }
        else if (SumoClicker.clickStyle.getMode() == RightClicker.ClickStyle.SKid) {
            this.skidClick();
        }
    }
    
    private void skidClick() {
        if (!Utils.Player.isPlayerInGame()) {
            return;
        }
        final double speedLeft1 = 1.0 / ThreadLocalRandom.current().nextDouble(SumoClicker.leftCPS.getInputMin() - 0.2, SumoClicker.leftCPS.getInputMax());
        final double leftHoldLength = speedLeft1 / ThreadLocalRandom.current().nextDouble(SumoClicker.leftCPS.getInputMin() - 0.02, SumoClicker.leftCPS.getInputMax());
        Mouse.poll();
        if (SumoClicker.mc.field_71462_r != null || !SumoClicker.mc.field_71415_G) {
            this.doInventoryClick();
            return;
        }
        if (Mouse.isButtonDown(0)) {
            if (this.breakBlock()) {
                return;
            }
            if (SumoClicker.weaponOnly.isToggled() && !Utils.Player.isPlayerHoldingWeapon()) {
                return;
            }
            if (SumoClicker.jitterLeft.getInput() > 0.0) {
                final double a = SumoClicker.jitterLeft.getInput() * 0.45;
                if (this.rand.nextBoolean()) {
                    final EntityPlayerSP entityPlayer = SumoClicker.mc.field_71439_g;
                    entityPlayer.field_70177_z += (float)(this.rand.nextFloat() * a);
                }
                else {
                    final EntityPlayerSP entityPlayer = SumoClicker.mc.field_71439_g;
                    entityPlayer.field_70177_z -= (float)(this.rand.nextFloat() * a);
                }
                if (this.rand.nextBoolean()) {
                    final EntityPlayerSP entityPlayer = SumoClicker.mc.field_71439_g;
                    entityPlayer.field_70125_A += (float)(this.rand.nextFloat() * a * 0.45);
                }
                else {
                    final EntityPlayerSP entityPlayer = SumoClicker.mc.field_71439_g;
                    entityPlayer.field_70125_A -= (float)(this.rand.nextFloat() * a * 0.45);
                }
            }
            final double speedLeft2 = 1.0 / java.util.concurrent.ThreadLocalRandom.current().nextDouble(SumoClicker.leftCPS.getInputMin() - 0.2, SumoClicker.leftCPS.getInputMax());
            if (System.currentTimeMillis() - this.lastClick > speedLeft2 * 1000.0) {
                this.lastClick = System.currentTimeMillis();
                if (this.leftHold < this.lastClick) {
                    this.leftHold = this.lastClick;
                }
                final int key = SumoClicker.mc.field_71474_y.field_74312_F.func_151463_i();
                KeyBinding.func_74510_a(key, true);
                KeyBinding.func_74507_a(key);
                Utils.Client.setMouseButtonState(0, true);
            }
            else if (System.currentTimeMillis() - this.leftHold > leftHoldLength * 1000.0) {
                KeyBinding.func_74510_a(SumoClicker.mc.field_71474_y.field_74312_F.func_151463_i(), false);
                Utils.Client.setMouseButtonState(0, false);
            }
        }
    }
    
    private void ravenClick() {
        if (SumoClicker.mc.field_71462_r != null) {
            this.doInventoryClick();
            return;
        }
        Mouse.poll();
        if (SumoClicker.weaponOnly.isToggled() && !Utils.Player.isPlayerHoldingWeapon()) {
            return;
        }
        this.leftClickExecute(SumoClicker.mc.field_71474_y.field_74312_F.func_151463_i());
    }
    
    public void leftClickExecute(final int key) {
        if (this.breakBlock()) {
            return;
        }
        if (SumoClicker.jitterLeft.getInput() > 0.0) {
            final double a = SumoClicker.jitterLeft.getInput() * 0.45;
            if (this.rand.nextBoolean()) {
                final EntityPlayerSP entityPlayer = SumoClicker.mc.field_71439_g;
                entityPlayer.field_70177_z += (float)(this.rand.nextFloat() * a);
            }
            else {
                final EntityPlayerSP entityPlayer = SumoClicker.mc.field_71439_g;
                entityPlayer.field_70177_z -= (float)(this.rand.nextFloat() * a);
            }
            if (this.rand.nextBoolean()) {
                final EntityPlayerSP entityPlayer = SumoClicker.mc.field_71439_g;
                entityPlayer.field_70125_A += (float)(this.rand.nextFloat() * a * 0.45);
            }
            else {
                final EntityPlayerSP entityPlayer = SumoClicker.mc.field_71439_g;
                entityPlayer.field_70125_A -= (float)(this.rand.nextFloat() * a * 0.45);
            }
        }
        if (this.leftUpTime > 0L && this.leftDownTime > 0L) {
            if (System.currentTimeMillis() > this.leftUpTime && this.leftDown) {
                KeyBinding.func_74510_a(key, true);
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
        final double clickSpeed = Utils.Client.ranModuleVal(SumoClicker.leftCPS, this.rand) + 0.4 * this.rand.nextDouble();
        long delay = (int)Math.round(1000.0 / clickSpeed);
        if (System.currentTimeMillis() > this.leftk) {
            if (!this.leftn && this.rand.nextInt(100) >= 85) {
                this.leftn = true;
                this.leftm = 1.1 + this.rand.nextDouble() * 0.15;
            }
            else {
                this.leftn = false;
            }
            this.leftk = System.currentTimeMillis() + 500L + this.rand.nextInt(1500);
        }
        if (this.leftn) {
            delay *= (long)this.leftm;
        }
        if (System.currentTimeMillis() > this.leftl) {
            if (this.rand.nextInt(100) >= 80) {
                delay += 50L + this.rand.nextInt(100);
            }
            this.leftl = System.currentTimeMillis() + 500L + this.rand.nextInt(1500);
        }
        this.leftUpTime = System.currentTimeMillis() + delay;
        this.leftDownTime = System.currentTimeMillis() + delay / 2L - this.rand.nextInt(10);
    }
    
    private void inInvClick(final GuiScreen guiScreen) {
        final int mouseInGUIPosX = Mouse.getX() * guiScreen.field_146294_l / SumoClicker.mc.field_71443_c;
        final int mouseInGUIPosY = guiScreen.field_146295_m - Mouse.getY() * guiScreen.field_146295_m / SumoClicker.mc.field_71440_d - 1;
        try {
            this.playerMouseInput.invoke(guiScreen, mouseInGUIPosX, mouseInGUIPosY, 0);
        }
        catch (IllegalAccessException ex) {}
        catch (InvocationTargetException ex2) {}
    }
    
    public boolean breakBlock() {
        if (SumoClicker.breakBlocks.isToggled() && SumoClicker.mc.field_71476_x != null) {
            final BlockPos p = SumoClicker.mc.field_71476_x.func_178782_a();
            if (p != null) {
                final Block bl = SumoClicker.mc.field_71441_e.func_180495_p(p).func_177230_c();
                if (bl != Blocks.field_150350_a && !(bl instanceof BlockLiquid)) {
                    if (!this.breakHeld) {
                        final int e = SumoClicker.mc.field_71474_y.field_74312_F.func_151463_i();
                        KeyBinding.func_74510_a(e, true);
                        KeyBinding.func_74507_a(e);
                        this.breakHeld = true;
                    }
                    return true;
                }
                if (this.breakHeld) {
                    this.breakHeld = false;
                }
            }
        }
        return false;
    }
    
    public void doInventoryClick() {
        if (SumoClicker.inventoryFill.isToggled() && (SumoClicker.mc.field_71462_r instanceof GuiInventory || SumoClicker.mc.field_71462_r instanceof GuiChest)) {
            if (!Mouse.isButtonDown(0) || (!Keyboard.isKeyDown(54) && !Keyboard.isKeyDown(42))) {
                this.leftDownTime = 0L;
                this.leftUpTime = 0L;
            }
            else if (this.leftDownTime != 0L && this.leftUpTime != 0L) {
                if (System.currentTimeMillis() > this.leftUpTime) {
                    this.genLeftTimings();
                    this.inInvClick(SumoClicker.mc.field_71462_r);
                }
            }
            else {
                this.genLeftTimings();
            }
        }
    }
    
    public enum ClickStyle
    {
        Raven, 
        SKid;
    }
    
    public enum ClickEvent
    {
        Tick, 
        Render;
    }
}
