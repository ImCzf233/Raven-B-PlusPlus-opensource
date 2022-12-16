package keystrokesmod.client.module.modules.hotkey;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.utils.*;
import net.minecraft.item.*;

public class Blocks extends Module
{
    private final TickSetting preferSlot;
    private final SliderSetting hotbarSlotPreference;
    
    public Blocks() {
        super("Blocks", ModuleCategory.hotkey);
        this.registerSetting(this.preferSlot = new TickSetting("Prefer a slot", false));
        this.registerSetting(this.hotbarSlotPreference = new SliderSetting("Prefer wich slot", 9.0, 1.0, 9.0, 1.0));
    }
    
    @Override
    public void onEnable() {
        if (!Utils.Player.isPlayerInGame()) {
            return;
        }
        if (this.preferSlot.isToggled()) {
            final int preferedSlot = (int)this.hotbarSlotPreference.getInput() - 1;
            final ItemStack itemInSlot = Blocks.mc.field_71439_g.field_71071_by.func_70301_a(preferedSlot);
            if (itemInSlot != null && itemInSlot.func_77973_b() instanceof ItemBlock) {
                Blocks.mc.field_71439_g.field_71071_by.field_70461_c = preferedSlot;
                this.disable();
                return;
            }
        }
        int slot = 0;
        while (slot <= 8) {
            final ItemStack itemInSlot = Blocks.mc.field_71439_g.field_71071_by.func_70301_a(slot);
            if (itemInSlot != null && itemInSlot.func_77973_b() instanceof ItemBlock && (((ItemBlock)itemInSlot.func_77973_b()).func_179223_d().func_149730_j() || ((ItemBlock)itemInSlot.func_77973_b()).func_179223_d().func_149686_d())) {
                if (Blocks.mc.field_71439_g.field_71071_by.field_70461_c != slot) {
                    Blocks.mc.field_71439_g.field_71071_by.field_70461_c = slot;
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
}
