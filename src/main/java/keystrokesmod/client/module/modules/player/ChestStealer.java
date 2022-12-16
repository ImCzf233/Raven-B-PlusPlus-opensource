package keystrokesmod.client.module.modules.player;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import net.minecraft.inventory.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.event.impl.*;
import keystrokesmod.client.utils.*;
import java.util.concurrent.*;
import net.minecraft.entity.player.*;
import com.google.common.eventbus.*;
import java.util.function.*;
import java.util.*;

public class ChestStealer extends Module
{
    private final DoubleSliderSetting firstDelay;
    private final DoubleSliderSetting delay;
    private final DoubleSliderSetting closeDelay;
    private final TickSetting autoClose;
    private boolean inChest;
    private final CoolDown delayTimer;
    private final CoolDown closeTimer;
    private ArrayList<Slot> sortedSlots;
    private ContainerChest chest;
    
    public ChestStealer() {
        super("ChestStealer", ModuleCategory.player);
        this.delayTimer = new CoolDown(0L);
        this.closeTimer = new CoolDown(0L);
        this.registerSetting(this.firstDelay = new DoubleSliderSetting("Open delay", 250.0, 450.0, 0.0, 1000.0, 1.0));
        this.registerSetting(this.delay = new DoubleSliderSetting("Delay", 150.0, 250.0, 0.0, 1000.0, 1.0));
        this.registerSetting(this.autoClose = new TickSetting("Auto Close", false));
        this.registerSetting(this.closeDelay = new DoubleSliderSetting("Close delay", 150.0, 250.0, 0.0, 1000.0, 1.0));
    }
    
    @Subscribe
    public void onRender2D(final Render2DEvent e) {
        if (Utils.Player.isPlayerInChest()) {
            if (!this.inChest) {
                this.chest = (ContainerChest)ChestStealer.mc.field_71439_g.field_71070_bA;
                this.delayTimer.setCooldown((long)ThreadLocalRandom.current().nextDouble(this.firstDelay.getInputMin(), this.firstDelay.getInputMax() + 0.01));
                this.delayTimer.start();
                this.generatePath(this.chest);
                this.inChest = true;
            }
            if (this.inChest && !this.sortedSlots.isEmpty() && this.delayTimer.hasFinished()) {
                ChestStealer.mc.field_71442_b.func_78753_a(ChestStealer.mc.field_71439_g.field_71070_bA.field_75152_c, this.sortedSlots.get(0).s, 0, 1, (EntityPlayer)ChestStealer.mc.field_71439_g);
                this.delayTimer.setCooldown((long)ThreadLocalRandom.current().nextDouble(this.delay.getInputMin(), this.delay.getInputMax() + 0.01));
                this.delayTimer.start();
                this.sortedSlots.remove(0);
            }
            if (this.sortedSlots.isEmpty() && this.autoClose.isToggled()) {
                if (this.closeTimer.firstFinish()) {
                    ChestStealer.mc.field_71439_g.func_71053_j();
                    this.inChest = false;
                }
                else {
                    this.closeTimer.setCooldown((long)ThreadLocalRandom.current().nextDouble(this.closeDelay.getInputMin(), this.closeDelay.getInputMax() + 0.01));
                    this.closeTimer.start();
                }
            }
        }
        else {
            this.inChest = false;
        }
    }
    
    public void generatePath(final ContainerChest chest) {
        final ArrayList<Slot> slots = new ArrayList<Slot>();
        for (int i = 0; i < chest.func_85151_d().func_70302_i_(); ++i) {
            if (chest.func_75138_a().get(i) != null) {
                slots.add(new Slot(i));
            }
        }
        final Slot[] ss = sort(slots.toArray(new Slot[slots.size()]));
        final ArrayList<Slot> newSlots = new ArrayList<Slot>();
        Collections.addAll(newSlots, ss);
        this.sortedSlots = newSlots;
    }
    
    public static Slot[] sort(final Slot[] in) {
        if (in == null || in.length == 0) {
            return in;
        }
        final Slot[] out = new Slot[in.length];
        Slot current = in[ThreadLocalRandom.current().nextInt(0, in.length)];
        for (int i = 0; i < in.length; ++i) {
            if (i == in.length - 1) {
                out[in.length - 1] = Arrays.stream(in).filter(p -> !p.visited).findAny().orElseGet(null);
                break;
            }
            final Slot finalCurrent = current;
            (out[i] = finalCurrent).visit();
            final Slot next = current = Arrays.stream(in).filter(p -> !p.visited).min(Comparator.comparingDouble(p -> p.getDistance(finalCurrent))).get();
        }
        return out;
    }
    
    public class Slot
    {
        final int x;
        final int y;
        final int s;
        boolean visited;
        
        public Slot(final int s) {
            this.x = (s + 1) % 10;
            this.y = s / 9;
            this.s = s;
        }
        
        public double getDistance(final Slot s) {
            return Math.abs(this.x - s.x) + Math.abs(this.y - s.y);
        }
        
        public void visit() {
            this.visited = true;
        }
    }
}
