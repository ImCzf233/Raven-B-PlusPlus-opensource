package keystrokesmod.client.module.modules.combat;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import java.util.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.utils.*;
import com.google.common.eventbus.*;
import keystrokesmod.client.event.impl.*;
import net.minecraftforge.event.entity.player.*;
import java.util.concurrent.*;
import net.minecraft.entity.player.*;
import net.minecraft.client.settings.*;
import org.lwjgl.input.*;

public class BlockHit extends Module
{
    public static SliderSetting range;
    public static SliderSetting chance;
    public static TickSetting onlyPlayers;
    public static TickSetting onlyForward;
    public static DoubleSliderSetting waitMs;
    public static DoubleSliderSetting actionMs;
    public static DoubleSliderSetting hitPer;
    public static boolean executingAction;
    public static int hits;
    public static int rHit;
    public static boolean call;
    public static boolean tryStartCombo;
    private final CoolDown actionTimer;
    private final CoolDown waitTimer;
    public Random r;
    
    public BlockHit() {
        super("BlockHit", ModuleCategory.combat);
        this.actionTimer = new CoolDown(0L);
        this.waitTimer = new CoolDown(0L);
        this.r = new Random();
        this.registerSetting(BlockHit.onlyPlayers = new TickSetting("Only combo players", true));
        this.registerSetting(BlockHit.onlyForward = new TickSetting("Only blockhit when walking forward", false));
        this.registerSetting(BlockHit.waitMs = new DoubleSliderSetting("Action Time (MS)", 30.0, 40.0, 1.0, 300.0, 1.0));
        this.registerSetting(BlockHit.actionMs = new DoubleSliderSetting("Block after ... ms", 20.0, 30.0, 1.0, 300.0, 1.0));
        this.registerSetting(BlockHit.hitPer = new DoubleSliderSetting("Once every ... hits", 1.0, 1.0, 1.0, 10.0, 1.0));
        this.registerSetting(BlockHit.chance = new SliderSetting("Chance %", 100.0, 0.0, 100.0, 1.0));
        this.registerSetting(BlockHit.range = new SliderSetting("Range: ", 3.0, 1.0, 6.0, 0.05));
    }
    
    @Subscribe
    public void onRender(final Render2DEvent e) {
        if (!Utils.Player.isPlayerInGame()) {
            return;
        }
        if (BlockHit.tryStartCombo && this.waitTimer.hasFinished()) {
            BlockHit.tryStartCombo = false;
            this.startCombo();
        }
        if (this.actionTimer.hasFinished() && BlockHit.executingAction) {
            this.finishCombo();
        }
    }
    
    @Subscribe
    public void onHit(final ForgeEvent fe) {
        if (fe.getEvent() instanceof AttackEntityEvent) {
            final AttackEntityEvent e = (AttackEntityEvent)fe.getEvent();
            if (isSecondCall() || BlockHit.executingAction) {
                return;
            }
            ++BlockHit.hits;
            if (BlockHit.hits > BlockHit.rHit) {
                BlockHit.hits = 1;
                final int eaSports = (int)(BlockHit.hitPer.getInputMax() - BlockHit.hitPer.getInputMin() + 1.0);
                BlockHit.rHit = ThreadLocalRandom.current().nextInt(eaSports);
                BlockHit.rHit += (int)BlockHit.hitPer.getInputMin();
            }
            if ((!(e.target instanceof EntityPlayer) && BlockHit.onlyPlayers.isToggled()) || Math.random() > BlockHit.chance.getInput() / 100.0 || !Utils.Player.isPlayerHoldingSword() || BlockHit.mc.field_71439_g.func_70032_d(e.target) > BlockHit.range.getInput() || BlockHit.rHit != BlockHit.hits) {
                return;
            }
            this.tryStartCombo();
        }
    }
    
    private void finishCombo() {
        BlockHit.executingAction = false;
        final int key = BlockHit.mc.field_71474_y.field_74313_G.func_151463_i();
        KeyBinding.func_74510_a(key, false);
        Utils.Client.setMouseButtonState(1, false);
    }
    
    private void startCombo() {
        if (!Keyboard.isKeyDown(BlockHit.mc.field_71474_y.field_74351_w.func_151463_i()) && BlockHit.onlyForward.isToggled()) {
            return;
        }
        BlockHit.executingAction = true;
        final int key = BlockHit.mc.field_71474_y.field_74313_G.func_151463_i();
        KeyBinding.func_74510_a(key, true);
        KeyBinding.func_74507_a(key);
        Utils.Client.setMouseButtonState(1, true);
        this.actionTimer.setCooldown((long)ThreadLocalRandom.current().nextDouble(BlockHit.waitMs.getInputMin(), BlockHit.waitMs.getInputMax() + 0.01));
        this.actionTimer.start();
    }
    
    public void tryStartCombo() {
        BlockHit.tryStartCombo = true;
        this.waitTimer.setCooldown((long)ThreadLocalRandom.current().nextDouble(BlockHit.actionMs.getInputMin(), BlockHit.actionMs.getInputMax() + 0.01));
        this.waitTimer.start();
    }
    
    private static boolean isSecondCall() {
        if (BlockHit.call) {
            BlockHit.call = false;
            return true;
        }
        BlockHit.call = true;
        return false;
    }
}
