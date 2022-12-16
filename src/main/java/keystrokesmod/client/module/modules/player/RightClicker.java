package keystrokesmod.client.module.modules.player;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import java.util.*;
import keystrokesmod.client.module.setting.*;
import net.minecraft.client.gui.*;
import net.minecraftforge.fml.relauncher.*;
import keystrokesmod.client.utils.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.inventory.*;
import com.google.common.eventbus.*;
import keystrokesmod.client.event.impl.*;
import io.netty.util.internal.*;
import org.lwjgl.input.*;
import net.minecraft.client.settings.*;
import net.minecraft.client.entity.*;
import keystrokesmod.client.main.*;
import net.minecraft.item.*;
import java.lang.reflect.*;

public class RightClicker extends Module
{
    public static SliderSetting jitterRight;
    public static TickSetting onlyBlocks;
    public static TickSetting preferFastPlace;
    public static TickSetting noBlockSword;
    public static TickSetting ignoreRods;
    public static TickSetting allowEat;
    public static TickSetting allowBow;
    public static SliderSetting rightClickDelay;
    public static DoubleSliderSetting rightCPS;
    public static ComboSetting clickStyle;
    public static ComboSetting clickTimings;
    private Random rand;
    private Method playerMouseInput;
    private long righti;
    private long rightj;
    private long rightk;
    private long rightl;
    private double rightm;
    private boolean rightn;
    private long lastClick;
    private long rightHold;
    private boolean rightClickWaiting;
    private double rightClickWaitStartTime;
    private boolean allowedClick;
    private boolean rightDown;
    
    public RightClicker() {
        super("Right Clicker", ModuleCategory.player);
        this.registerSetting(RightClicker.rightCPS = new DoubleSliderSetting("RightCPS", 12.0, 16.0, 1.0, 60.0, 0.5));
        this.registerSetting(RightClicker.jitterRight = new SliderSetting("Jitter right", 0.0, 0.0, 3.0, 0.1));
        this.registerSetting(RightClicker.rightClickDelay = new SliderSetting("Rightclick delay (ms)", 85.0, 0.0, 500.0, 1.0));
        this.registerSetting(RightClicker.noBlockSword = new TickSetting("Don't rightclick sword", true));
        this.registerSetting(RightClicker.ignoreRods = new TickSetting("Ignore rods", true));
        this.registerSetting(RightClicker.onlyBlocks = new TickSetting("Only rightclick with blocks", false));
        this.registerSetting(RightClicker.preferFastPlace = new TickSetting("Prefer fast place", false));
        this.registerSetting(RightClicker.allowEat = new TickSetting("Allow eat & drink", true));
        this.registerSetting(RightClicker.allowBow = new TickSetting("Allow bow", true));
        this.registerSetting(RightClicker.clickTimings = new ComboSetting("Click event", (T)ClickEvent.Render));
        this.registerSetting(RightClicker.clickStyle = new ComboSetting("Click Style", (T)ClickStyle.Raven));
        try {
            this.playerMouseInput = ReflectionHelper.findMethod((Class)GuiScreen.class, (Object)null, new String[] { "func_73864_a", "mouseClicked" }, new Class[] { Integer.TYPE, Integer.TYPE, Integer.TYPE });
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        if (this.playerMouseInput != null) {
            this.playerMouseInput.setAccessible(true);
        }
        this.rightClickWaiting = false;
    }
    
    @Override
    public void onEnable() {
        if (this.playerMouseInput == null) {
            this.disable();
        }
        this.rightClickWaiting = false;
        this.allowedClick = false;
        this.rand = new Random();
    }
    
    @Override
    public void onDisable() {
        this.rightClickWaiting = false;
    }
    
    @Subscribe
    public void onRender2D(final Render2DEvent e) {
        if (!Utils.Client.currentScreenMinecraft() && !(Minecraft.func_71410_x().field_71462_r instanceof GuiInventory) && !(Minecraft.func_71410_x().field_71462_r instanceof GuiChest)) {
            return;
        }
        if (RightClicker.clickTimings.getMode() != ClickEvent.Render) {
            return;
        }
        if (RightClicker.clickStyle.getMode() == ClickStyle.Raven) {
            this.ravenClick();
        }
        else if (RightClicker.clickStyle.getMode() == ClickStyle.SKid) {
            this.skidClick();
        }
    }
    
    @Subscribe
    public void onTick(final TickEvent tick) {
        if (!Utils.Client.currentScreenMinecraft() && !(Minecraft.func_71410_x().field_71462_r instanceof GuiInventory) && !(Minecraft.func_71410_x().field_71462_r instanceof GuiChest)) {
            return;
        }
        if (RightClicker.clickTimings.getMode() != ClickEvent.Tick) {
            return;
        }
        if (RightClicker.clickStyle.getMode() == ClickStyle.Raven) {
            this.ravenClick();
        }
        else if (RightClicker.clickStyle.getMode() == ClickStyle.SKid) {
            this.skidClick();
        }
    }
    
    private void skidClick() {
        if (!Utils.Player.isPlayerInGame()) {
            return;
        }
        if (RightClicker.mc.field_71462_r != null || !RightClicker.mc.field_71415_G) {
            return;
        }
        final double speedRight = 1.0 / ThreadLocalRandom.current().nextDouble(RightClicker.rightCPS.getInputMin() - 0.2, RightClicker.rightCPS.getInputMax());
        final double rightHoldLength = speedRight / ThreadLocalRandom.current().nextDouble(RightClicker.rightCPS.getInputMin() - 0.02, RightClicker.rightCPS.getInputMax());
        if (!Mouse.isButtonDown(1) && !this.rightDown) {
            KeyBinding.func_74510_a(RightClicker.mc.field_71474_y.field_74313_G.func_151463_i(), false);
            Utils.Client.setMouseButtonState(1, false);
        }
        if (Mouse.isButtonDown(1) || this.rightDown) {
            if (!this.rightClickAllowed()) {
                return;
            }
            if (RightClicker.jitterRight.getInput() > 0.0) {
                final double jitterMultiplier = RightClicker.jitterRight.getInput() * 0.45;
                if (this.rand.nextBoolean()) {
                    final EntityPlayerSP entityPlayer = RightClicker.mc.field_71439_g;
                    entityPlayer.field_70177_z += (float)(this.rand.nextFloat() * jitterMultiplier);
                }
                else {
                    final EntityPlayerSP entityPlayer = RightClicker.mc.field_71439_g;
                    entityPlayer.field_70177_z -= (float)(this.rand.nextFloat() * jitterMultiplier);
                }
                if (this.rand.nextBoolean()) {
                    final EntityPlayerSP entityPlayer = RightClicker.mc.field_71439_g;
                    entityPlayer.field_70125_A += (float)(this.rand.nextFloat() * jitterMultiplier * 0.45);
                }
                else {
                    final EntityPlayerSP entityPlayer = RightClicker.mc.field_71439_g;
                    entityPlayer.field_70125_A -= (float)(this.rand.nextFloat() * jitterMultiplier * 0.45);
                }
            }
            if (System.currentTimeMillis() - this.lastClick > speedRight * 1000.0) {
                this.lastClick = System.currentTimeMillis();
                if (this.rightHold < this.lastClick) {
                    this.rightHold = this.lastClick;
                }
                final int key = RightClicker.mc.field_71474_y.field_74313_G.func_151463_i();
                KeyBinding.func_74510_a(key, true);
                Utils.Client.setMouseButtonState(1, true);
                KeyBinding.func_74507_a(key);
                this.rightDown = false;
            }
            else if (System.currentTimeMillis() - this.rightHold > rightHoldLength * 1000.0) {
                this.rightDown = true;
                KeyBinding.func_74510_a(RightClicker.mc.field_71474_y.field_74313_G.func_151463_i(), false);
                Utils.Client.setMouseButtonState(1, false);
            }
        }
        else if (!Mouse.isButtonDown(1)) {
            this.rightClickWaiting = false;
            this.allowedClick = false;
        }
    }
    
    private void ravenClick() {
        if (!Utils.Player.isPlayerInGame()) {
            return;
        }
        if (RightClicker.mc.field_71462_r != null || !RightClicker.mc.field_71415_G) {
            return;
        }
        Mouse.poll();
        if (Mouse.isButtonDown(1)) {
            this.rightClickExecute(RightClicker.mc.field_71474_y.field_74313_G.func_151463_i());
        }
        else if (!Mouse.isButtonDown(1)) {
            this.rightClickWaiting = false;
            this.allowedClick = false;
            this.righti = 0L;
            this.rightj = 0L;
        }
    }
    
    public boolean rightClickAllowed() {
        final ItemStack item = RightClicker.mc.field_71439_g.func_70694_bm();
        if (item != null) {
            if (RightClicker.allowEat.isToggled() && (item.func_77973_b() instanceof ItemFood || item.func_77973_b() instanceof ItemPotion || item.func_77973_b() instanceof ItemBucketMilk)) {
                return false;
            }
            if (RightClicker.ignoreRods.isToggled() && item.func_77973_b() instanceof ItemFishingRod) {
                return false;
            }
            if (RightClicker.allowBow.isToggled() && item.func_77973_b() instanceof ItemBow) {
                return false;
            }
            if (RightClicker.onlyBlocks.isToggled() && !(item.func_77973_b() instanceof ItemBlock)) {
                return false;
            }
            if (RightClicker.noBlockSword.isToggled() && item.func_77973_b() instanceof ItemSword) {
                return false;
            }
        }
        if (RightClicker.preferFastPlace.isToggled()) {
            final Module fastplace = Raven.moduleManager.getModuleByClazz(FastPlace.class);
            if (fastplace != null && fastplace.isEnabled()) {
                return false;
            }
        }
        if (RightClicker.rightClickDelay.getInput() != 0.0) {
            if (!this.rightClickWaiting && !this.allowedClick) {
                this.rightClickWaitStartTime = (double)System.currentTimeMillis();
                this.rightClickWaiting = true;
                return false;
            }
            if (this.rightClickWaiting && !this.allowedClick) {
                final double passedTime = System.currentTimeMillis() - this.rightClickWaitStartTime;
                if (passedTime >= RightClicker.rightClickDelay.getInput()) {
                    this.allowedClick = true;
                    this.rightClickWaiting = false;
                    return true;
                }
                return false;
            }
        }
        return true;
    }
    
    public void rightClickExecute(final int key) {
        if (!this.rightClickAllowed()) {
            return;
        }
        if (RightClicker.jitterRight.getInput() > 0.0) {
            final double jitterMultiplier = RightClicker.jitterRight.getInput() * 0.45;
            if (this.rand.nextBoolean()) {
                final EntityPlayerSP entityPlayer = RightClicker.mc.field_71439_g;
                entityPlayer.field_70177_z += (float)(this.rand.nextFloat() * jitterMultiplier);
            }
            else {
                final EntityPlayerSP entityPlayer = RightClicker.mc.field_71439_g;
                entityPlayer.field_70177_z -= (float)(this.rand.nextFloat() * jitterMultiplier);
            }
            if (this.rand.nextBoolean()) {
                final EntityPlayerSP entityPlayer = RightClicker.mc.field_71439_g;
                entityPlayer.field_70125_A += (float)(this.rand.nextFloat() * jitterMultiplier * 0.45);
            }
            else {
                final EntityPlayerSP entityPlayer = RightClicker.mc.field_71439_g;
                entityPlayer.field_70125_A -= (float)(this.rand.nextFloat() * jitterMultiplier * 0.45);
            }
        }
        if (this.rightj > 0L && this.righti > 0L) {
            if (System.currentTimeMillis() > this.rightj) {
                KeyBinding.func_74510_a(key, true);
                KeyBinding.func_74507_a(key);
                Utils.Client.setMouseButtonState(1, false);
                Utils.Client.setMouseButtonState(1, true);
                this.genRightTimings();
            }
            else if (System.currentTimeMillis() > this.righti) {
                KeyBinding.func_74510_a(key, false);
            }
        }
        else {
            this.genRightTimings();
        }
    }
    
    public void genRightTimings() {
        final double clickSpeed = Utils.Client.ranModuleVal(RightClicker.rightCPS, this.rand) + 0.4 * this.rand.nextDouble();
        long delay = (int)Math.round(1000.0 / clickSpeed);
        if (System.currentTimeMillis() > this.rightk) {
            if (!this.rightn && this.rand.nextInt(100) >= 85) {
                this.rightn = true;
                this.rightm = 1.1 + this.rand.nextDouble() * 0.15;
            }
            else {
                this.rightn = false;
            }
            this.rightk = System.currentTimeMillis() + 500L + this.rand.nextInt(1500);
        }
        if (this.rightn) {
            delay *= (long)this.rightm;
        }
        if (System.currentTimeMillis() > this.rightl) {
            if (this.rand.nextInt(100) >= 80) {
                delay += 50L + this.rand.nextInt(100);
            }
            this.rightl = System.currentTimeMillis() + 500L + this.rand.nextInt(1500);
        }
        this.rightj = System.currentTimeMillis() + delay;
        this.righti = System.currentTimeMillis() + delay / 2L - this.rand.nextInt(10);
    }
    
    private void inInvClick(final GuiScreen guiScreen) {
        final int mouseInGUIPosX = Mouse.getX() * guiScreen.field_146294_l / RightClicker.mc.field_71443_c;
        final int mouseInGUIPosY = guiScreen.field_146295_m - Mouse.getY() * guiScreen.field_146295_m / RightClicker.mc.field_71440_d - 1;
        try {
            this.playerMouseInput.invoke(guiScreen, mouseInGUIPosX, mouseInGUIPosY, 0);
        }
        catch (IllegalAccessException ex) {}
        catch (InvocationTargetException ex2) {}
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
