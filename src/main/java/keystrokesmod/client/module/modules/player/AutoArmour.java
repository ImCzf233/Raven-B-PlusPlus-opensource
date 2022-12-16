package keystrokesmod.client.module.modules.player;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import java.util.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.event.impl.*;
import keystrokesmod.client.utils.*;
import java.util.concurrent.*;
import net.minecraft.inventory.*;
import net.minecraft.entity.player.*;
import com.google.common.eventbus.*;
import net.minecraft.client.*;
import net.minecraft.enchantment.*;
import net.minecraft.item.*;

public class AutoArmour extends Module
{
    private final DoubleSliderSetting firstDelay;
    private final DoubleSliderSetting delay;
    private List<Slot> sortedSlots;
    private boolean inInv;
    private final CoolDown delayTimer;
    
    public AutoArmour() {
        super("AutoArmour", ModuleCategory.player);
        this.sortedSlots = new ArrayList<Slot>();
        this.delayTimer = new CoolDown(0L);
        this.registerSetting(this.firstDelay = new DoubleSliderSetting("Open delay", 250.0, 450.0, 0.0, 1000.0, 1.0));
        this.registerSetting(this.delay = new DoubleSliderSetting("Delay", 150.0, 250.0, 0.0, 1000.0, 1.0));
    }
    
    @Subscribe
    public void onRender2D(final Render2DEvent e) {
        if (Utils.Player.isPlayerInInventory()) {
            if (!this.inInv) {
                this.delayTimer.setCooldown((long)ThreadLocalRandom.current().nextDouble(this.firstDelay.getInputMin(), this.firstDelay.getInputMax() + 0.01));
                this.delayTimer.start();
                this.generatePath((ContainerPlayer)AutoArmour.mc.field_71439_g.field_71070_bA);
                this.inInv = true;
            }
            if (!this.sortedSlots.isEmpty() && this.delayTimer.hasFinished()) {
                AutoArmour.mc.field_71442_b.func_78753_a(AutoArmour.mc.field_71439_g.field_71070_bA.field_75152_c, this.sortedSlots.get(0).s, 0, 1, (EntityPlayer)AutoArmour.mc.field_71439_g);
                this.delayTimer.setCooldown((long)ThreadLocalRandom.current().nextDouble(this.delay.getInputMin(), this.delay.getInputMax() + 0.01));
                this.delayTimer.start();
                this.sortedSlots.remove(0);
            }
        }
        else {
            this.inInv = false;
        }
    }
    
    public void generatePath(final ContainerPlayer inv) {
        final ArrayList<Slot> slots = new ArrayList<Slot>();
        final Slot[] bestArmour = { new Slot(-1), new Slot(-1), new Slot(-1), new Slot(-1) };
        for (int i = 0; i < inv.func_75138_a().size(); ++i) {
            if (inv.func_75138_a().get(i) != null && inv.func_75138_a().get(i).func_77973_b() instanceof ItemArmor && (i <= 4 || i >= 9)) {
                final Slot ia = new Slot(i);
                if (bestArmour[ia.at].v < ia.v) {
                    bestArmour[ia.at] = ia;
                }
            }
        }
        for (int i = 0; i < 4; ++i) {
            try {
                final Slot s = new Slot(i + 5);
                if (s.v < bestArmour[i].v) {
                    slots.add(s);
                    slots.add(bestArmour[i]);
                }
            }
            catch (NullPointerException e) {
                slots.add(bestArmour[i]);
            }
            catch (ClassCastException e2) {
                slots.add(new Slot(i + 5));
                slots.add(bestArmour[i]);
            }
        }
        this.sortedSlots = slots;
    }
    
    public static class Slot
    {
        final int x;
        final int y;
        final int s;
        int at;
        float v;
        
        public Slot(final int s) {
            this.s = s;
            this.x = (s + 1) % 10;
            this.y = s / 9;
            this.setValues();
        }
        
        public void setValues() {
            if (this.s < 0) {
                return;
            }
            final ItemStack itemStack = AutoArmour.mc.field_71439_g.field_71070_bA.func_75139_a(this.s).func_75211_c();
            final Item is = itemStack.func_77973_b();
            if (!(is instanceof ItemArmor)) {
                return;
            }
            final ItemArmor as = (ItemArmor)is;
            float pl;
            try {
                pl = EnchantmentHelper.func_82781_a(itemStack).get(0);
            }
            catch (Exception e) {
                pl = 0.0f;
            }
            this.v = as.field_77879_b + (float)(pl * 0.6);
            this.at = as.field_77881_a;
        }
    }
}
