package keystrokesmod.client.module.modules.hotkey;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.utils.*;
import net.minecraft.potion.*;
import java.util.*;
import net.minecraft.item.*;

public class Healing extends Module
{
    private final TickSetting preferSlot;
    private final SliderSetting hotbarSlotPreference;
    private final ComboSetting itemMode;
    private final HealingItems mode;
    
    public Healing() {
        super("Healing", ModuleCategory.hotkey);
        this.mode = HealingItems.HEAL_POT;
        this.registerSetting(this.preferSlot = new TickSetting("Prefer a slot", false));
        this.registerSetting(this.hotbarSlotPreference = new SliderSetting("Prefer wich slot", 8.0, 1.0, 9.0, 1.0));
        this.registerSetting(this.itemMode = new ComboSetting("Mode:", (T)this.mode));
    }
    
    @Override
    public void onEnable() {
        if (!Utils.Player.isPlayerInGame()) {
            return;
        }
        if (this.preferSlot.isToggled()) {
            final int preferedSlot = (int)this.hotbarSlotPreference.getInput() - 1;
            if (this.itemMode.getMode() == HealingItems.SOUP && this.isSoup(preferedSlot)) {
                Healing.mc.field_71439_g.field_71071_by.field_70461_c = preferedSlot;
                this.disable();
                return;
            }
            if (this.itemMode.getMode() == HealingItems.GAPPLE && this.isGapple(preferedSlot)) {
                Healing.mc.field_71439_g.field_71071_by.field_70461_c = preferedSlot;
                this.disable();
                return;
            }
            if (this.itemMode.getMode() == HealingItems.FOOD && this.isFood(preferedSlot)) {
                Healing.mc.field_71439_g.field_71071_by.field_70461_c = preferedSlot;
                this.disable();
                return;
            }
            if (this.itemMode.getMode() == HealingItems.ALL && (this.isGapple(preferedSlot) || this.isFood(preferedSlot) || this.isSoup(preferedSlot))) {
                Healing.mc.field_71439_g.field_71071_by.field_70461_c = preferedSlot;
                this.disable();
                return;
            }
            if (this.itemMode.getMode() == HealingItems.HEAL_POT && this.isHPot(preferedSlot)) {
                Healing.mc.field_71439_g.field_71071_by.field_70461_c = preferedSlot;
                this.disable();
                return;
            }
        }
        for (int slot = 0; slot <= 8; ++slot) {
            if (this.itemMode.getMode() == HealingItems.SOUP && this.isSoup(slot)) {
                Healing.mc.field_71439_g.field_71071_by.field_70461_c = slot;
                this.disable();
                return;
            }
            if (this.itemMode.getMode() == HealingItems.GAPPLE && this.isGapple(slot)) {
                Healing.mc.field_71439_g.field_71071_by.field_70461_c = slot;
                this.disable();
                return;
            }
            if (this.itemMode.getMode() == HealingItems.FOOD && this.isFood(slot)) {
                Healing.mc.field_71439_g.field_71071_by.field_70461_c = slot;
                this.disable();
                return;
            }
            if (this.itemMode.getMode() == HealingItems.ALL && (this.isGapple(slot) || this.isFood(slot) || this.isSoup(slot))) {
                Healing.mc.field_71439_g.field_71071_by.field_70461_c = slot;
                this.disable();
                return;
            }
            if (this.itemMode.getMode() == HealingItems.HEAL_POT && this.isHPot(slot)) {
                Healing.mc.field_71439_g.field_71071_by.field_70461_c = slot;
                this.disable();
                System.out.println("a");
                return;
            }
        }
        this.disable();
    }
    
    public static boolean checkSlot(final int slot) {
        final ItemStack itemInSlot = Healing.mc.field_71439_g.field_71071_by.func_70301_a(slot);
        return itemInSlot != null && itemInSlot.func_82833_r().equalsIgnoreCase("ladder");
    }
    
    public boolean isSoup(final int slot) {
        final ItemStack itemInSlot = Healing.mc.field_71439_g.field_71071_by.func_70301_a(slot);
        return itemInSlot != null && itemInSlot.func_77973_b() instanceof ItemSoup;
    }
    
    public boolean isGapple(final int slot) {
        final ItemStack itemInSlot = Healing.mc.field_71439_g.field_71071_by.func_70301_a(slot);
        return itemInSlot != null && itemInSlot.func_77973_b() instanceof ItemAppleGold;
    }
    
    public boolean isHPot(final int slot) {
        final ItemStack itemInSlot = Healing.mc.field_71439_g.field_71071_by.func_70301_a(slot);
        if (itemInSlot == null) {
            return false;
        }
        if (itemInSlot.func_77973_b() instanceof ItemPotion) {
            final ItemPotion ip = (ItemPotion)itemInSlot.func_77973_b();
            Utils.Player.sendMessageToSelf("" + slot);
            for (final PotionEffect pe : ip.func_77832_l(itemInSlot)) {
                if (pe.func_76456_a() == Potion.field_76432_h.field_76415_H) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean isHead(final int slot) {
        final ItemStack itemInSlot = Healing.mc.field_71439_g.field_71071_by.func_70301_a(slot);
        return itemInSlot != null && itemInSlot.func_77973_b() instanceof Item;
    }
    
    public boolean isFood(final int slot) {
        final ItemStack itemInSlot = Healing.mc.field_71439_g.field_71071_by.func_70301_a(slot);
        return itemInSlot != null && itemInSlot.func_77973_b() instanceof ItemFood;
    }
    
    public enum HealingItems
    {
        SOUP, 
        GAPPLE, 
        FOOD, 
        HEAL_POT, 
        ALL;
    }
}
