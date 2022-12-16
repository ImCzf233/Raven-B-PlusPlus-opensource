package keystrokesmod.client.module.modules.player;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.event.impl.*;
import keystrokesmod.client.utils.*;
import org.lwjgl.input.*;
import keystrokesmod.client.module.modules.combat.*;
import keystrokesmod.client.main.*;
import net.minecraft.init.*;
import net.minecraft.block.*;
import java.util.concurrent.*;
import net.minecraft.util.*;
import com.google.common.eventbus.*;
import net.minecraft.item.*;

public class AutoTool extends Module
{
    private final TickSetting hotkeyBack;
    private Block previousBlock;
    private boolean isWaiting;
    public static DoubleSliderSetting mineDelay;
    public static int previousSlot;
    public static boolean justFinishedMining;
    public static boolean mining;
    public static CoolDown delay;
    
    public AutoTool() {
        super("Auto Tool", ModuleCategory.player);
        this.registerSetting(this.hotkeyBack = new TickSetting("Hotkey back", true));
        this.registerSetting(AutoTool.mineDelay = new DoubleSliderSetting("Max delay", 10.0, 50.0, 0.0, 2000.0, 1.0));
        AutoTool.delay = new CoolDown(0L);
    }
    
    @Subscribe
    public void onRender2D(final Render2DEvent e) {
        if (!Utils.Player.isPlayerInGame() || AutoTool.mc.field_71462_r != null) {
            return;
        }
        if (!Mouse.isButtonDown(0)) {
            if (AutoTool.mining) {
                this.finishMining();
            }
            if (this.isWaiting) {
                this.isWaiting = false;
            }
            return;
        }
        final LeftClicker autoClicker = (LeftClicker)Raven.moduleManager.getModuleByClazz(LeftClicker.class);
        if (autoClicker.isEnabled() && !LeftClicker.breakBlocks.isToggled()) {
            return;
        }
        final BlockPos lookingAtBlock = AutoTool.mc.field_71476_x.func_178782_a();
        if (lookingAtBlock != null) {
            final Block stateBlock = AutoTool.mc.field_71441_e.func_180495_p(lookingAtBlock).func_177230_c();
            if (stateBlock != Blocks.field_150350_a && !(stateBlock instanceof BlockLiquid) && stateBlock != null) {
                if (AutoTool.mineDelay.getInputMax() > 0.0) {
                    if (this.previousBlock != null) {
                        if (this.previousBlock != stateBlock) {
                            this.previousBlock = stateBlock;
                            this.isWaiting = true;
                            AutoTool.delay.setCooldown((long)ThreadLocalRandom.current().nextDouble(AutoTool.mineDelay.getInputMin(), AutoTool.mineDelay.getInputMax() + 0.01));
                            AutoTool.delay.start();
                        }
                        else if (this.isWaiting && AutoTool.delay.hasFinished()) {
                            this.isWaiting = false;
                            AutoTool.previousSlot = Utils.Player.getCurrentPlayerSlot();
                            AutoTool.mining = true;
                            this.hotkeyToFastest();
                        }
                    }
                    else {
                        this.previousBlock = stateBlock;
                        this.isWaiting = false;
                    }
                    return;
                }
                if (!AutoTool.mining) {
                    AutoTool.previousSlot = Utils.Player.getCurrentPlayerSlot();
                    AutoTool.mining = true;
                }
                this.hotkeyToFastest();
            }
        }
    }
    
    public void finishMining() {
        if (this.hotkeyBack.isToggled()) {
            Utils.Player.hotkeyToSlot(AutoTool.previousSlot);
        }
        AutoTool.justFinishedMining = false;
        AutoTool.mining = false;
    }
    
    private void hotkeyToFastest() {
        int index = -1;
        double speed = 1.0;
        for (int slot = 0; slot <= 8; ++slot) {
            final ItemStack itemInSlot = AutoTool.mc.field_71439_g.field_71071_by.func_70301_a(slot);
            if (itemInSlot != null && (itemInSlot.func_77973_b() instanceof ItemTool || itemInSlot.func_77973_b() instanceof ItemShears)) {
                final BlockPos p = AutoTool.mc.field_71476_x.func_178782_a();
                final Block bl = AutoTool.mc.field_71441_e.func_180495_p(p).func_177230_c();
                if (itemInSlot.func_77973_b().getDigSpeed(itemInSlot, bl.func_176223_P()) > speed) {
                    speed = itemInSlot.func_77973_b().getDigSpeed(itemInSlot, bl.func_176223_P());
                    index = slot;
                }
            }
        }
        if (index != -1) {
            if (speed > 1.1) {
                Utils.Player.hotkeyToSlot(index);
            }
        }
    }
}
