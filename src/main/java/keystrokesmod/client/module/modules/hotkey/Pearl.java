package keystrokesmod.client.module.modules.hotkey;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import java.util.*;
import net.minecraft.client.settings.*;
import keystrokesmod.client.module.setting.*;
import net.minecraft.item.*;
import keystrokesmod.client.utils.*;

public class Pearl extends Module
{
    private final TickSetting preferSlot;
    private final SliderSetting hotbarSlotPreference;
    public static ArrayList<KeyBinding> changedKeybinds;
    
    public Pearl() {
        super("Pearl", ModuleCategory.hotkey);
        this.registerSetting(this.preferSlot = new TickSetting("Prefer a slot", false));
        this.registerSetting(this.hotbarSlotPreference = new SliderSetting("Prefer wich slot", 6.0, 1.0, 9.0, 1.0));
    }
    
    public static boolean checkSlot(final int slot) {
        final ItemStack itemInSlot = Pearl.mc.field_71439_g.field_71071_by.func_70301_a(slot);
        return itemInSlot != null && itemInSlot.func_82833_r().equalsIgnoreCase("ender pearl");
    }
    
    @Override
    public void onEnable() {
        if (!Utils.Player.isPlayerInGame()) {
            return;
        }
        if (this.preferSlot.isToggled()) {
            final int preferedSlot = (int)this.hotbarSlotPreference.getInput() - 1;
            if (checkSlot(preferedSlot)) {
                Pearl.mc.field_71439_g.field_71071_by.field_70461_c = preferedSlot;
                this.disable();
                return;
            }
        }
        for (int slot = 0; slot <= 8; ++slot) {
            if (checkSlot(slot)) {
                Pearl.mc.field_71439_g.field_71071_by.field_70461_c = slot;
                this.disable();
                return;
            }
        }
        this.disable();
    }
    
    static {
        Pearl.changedKeybinds = new ArrayList<KeyBinding>();
    }
}
