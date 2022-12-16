package keystrokesmod.client.module.modules.hotkey;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.utils.*;
import net.minecraft.item.*;

public class Ladders extends Module
{
    private final TickSetting preferSlot;
    private final SliderSetting hotbarSlotPreference;
    
    public Ladders() {
        super("Ladders", ModuleCategory.hotkey);
        this.registerSetting(this.preferSlot = new TickSetting("Prefer a slot", false));
        this.registerSetting(this.hotbarSlotPreference = new SliderSetting("Prefer wich slot", 8.0, 1.0, 9.0, 1.0));
    }
    
    @Override
    public void onEnable() {
        if (!Utils.Player.isPlayerInGame()) {
            return;
        }
        if (this.preferSlot.isToggled()) {
            final int preferedSlot = (int)this.hotbarSlotPreference.getInput() - 1;
            if (checkSlot(preferedSlot)) {
                Ladders.mc.field_71439_g.field_71071_by.field_70461_c = preferedSlot;
                this.disable();
                return;
            }
        }
        int slot = 0;
        while (slot <= 8) {
            if (checkSlot(slot)) {
                if (Ladders.mc.field_71439_g.field_71071_by.field_70461_c != slot) {
                    Ladders.mc.field_71439_g.field_71071_by.field_70461_c = slot;
                    this.disable();
                }
                return;
            }
            else {
                ++slot;
            }
        }
        this.disable();
    }
    
    public static boolean checkSlot(final int slot) {
        final ItemStack itemInSlot = Ladders.mc.field_71439_g.field_71071_by.func_70301_a(slot);
        return itemInSlot != null && itemInSlot != null && itemInSlot.func_82833_r().equalsIgnoreCase("ladder");
    }
}
