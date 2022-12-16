package keystrokesmod.client.module.modules.hotkey;

import keystrokesmod.client.module.*;
import keystrokesmod.client.utils.*;
import net.minecraft.entity.ai.attributes.*;
import net.minecraft.item.*;
import java.util.*;

public class Weapon extends Module
{
    public Weapon() {
        super("Weapon", ModuleCategory.hotkey);
    }
    
    @Override
    public void onEnable() {
        if (!Utils.Player.isPlayerInGame()) {
            return;
        }
        int index = -1;
        double damage = -1.0;
        for (int slot = 0; slot <= 8; ++slot) {
            final ItemStack itemInSlot = Weapon.mc.field_71439_g.field_71071_by.func_70301_a(slot);
            if (itemInSlot != null) {
                for (final AttributeModifier mooommHelp : itemInSlot.func_111283_C().values()) {
                    if (mooommHelp.func_111164_d() > damage) {
                        damage = mooommHelp.func_111164_d();
                        index = slot;
                    }
                }
            }
        }
        if (index > -1 && damage > -1.0 && Weapon.mc.field_71439_g.field_71071_by.field_70461_c != index) {
            Utils.Player.hotkeyToSlot(index);
        }
        this.disable();
    }
}
