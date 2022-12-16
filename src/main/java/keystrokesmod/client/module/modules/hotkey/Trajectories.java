package keystrokesmod.client.module.modules.hotkey;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.utils.*;
import net.minecraft.item.*;

public class Trajectories extends Module
{
    private final TickSetting preferSlot;
    private final SliderSetting hotbarSlotPreference;
    
    public Trajectories() {
        super("Trajectories", ModuleCategory.hotkey);
        this.registerSetting(this.preferSlot = new TickSetting("Prefer a slot", false));
        this.registerSetting(this.hotbarSlotPreference = new SliderSetting("Prefer wich slot", 5.0, 1.0, 9.0, 1.0));
    }
    
    @Override
    public void onEnable() {
        if (!Utils.Player.isPlayerInGame()) {
            return;
        }
        if (this.preferSlot.isToggled()) {
            final int preferedSlot = (int)this.hotbarSlotPreference.getInput() - 1;
            if (checkSlot(preferedSlot)) {
                Trajectories.mc.field_71439_g.field_71071_by.field_70461_c = preferedSlot;
                this.disable();
                return;
            }
        }
        int slot = 0;
        while (slot <= 8) {
            if (checkSlot(slot)) {
                if (Trajectories.mc.field_71439_g.field_71071_by.field_70461_c != slot) {
                    Trajectories.mc.field_71439_g.field_71071_by.field_70461_c = slot;
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
        final ItemStack itemInSlot = Trajectories.mc.field_71439_g.field_71071_by.func_70301_a(slot);
        return itemInSlot != null && (itemInSlot.func_77973_b() instanceof ItemSnowball || itemInSlot.func_77973_b() instanceof ItemEgg || itemInSlot.func_77973_b() instanceof ItemFishingRod);
    }
}
