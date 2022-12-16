package keystrokesmod.client.module.modules.hotkey;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.utils.*;
import net.minecraft.item.*;

public class Armour extends Module
{
    public static TickSetting ignoreIfAlreadyEquipped;
    
    public Armour() {
        super("Armour", ModuleCategory.hotkey);
        this.registerSetting(Armour.ignoreIfAlreadyEquipped = new TickSetting("Ignore if already equipped", true));
    }
    
    @Override
    public void onEnable() {
        if (!Utils.Player.isPlayerInGame()) {
            return;
        }
        int index = -1;
        double strength = -1.0;
        for (int armorType = 0; armorType < 4; ++armorType) {
            index = -1;
            strength = -1.0;
            for (int slot = 0; slot <= 8; ++slot) {
                final ItemStack itemStack = Armour.mc.field_71439_g.field_71071_by.func_70301_a(slot);
                if (itemStack != null && itemStack.func_77973_b() instanceof ItemArmor) {
                    final ItemArmor armorPiece = (ItemArmor)itemStack.func_77973_b();
                    if (!Utils.Player.playerWearingArmor().contains(armorPiece.field_77881_a) && armorPiece.field_77881_a == armorType && Armour.ignoreIfAlreadyEquipped.isToggled()) {
                        if (armorPiece.func_82812_d().func_78044_b(armorType) > strength) {
                            strength = armorPiece.func_82812_d().func_78044_b(armorType);
                            index = slot;
                        }
                    }
                    else if (Utils.Player.playerWearingArmor().contains(armorPiece.field_77881_a) && armorPiece.field_77881_a == armorType && !Armour.ignoreIfAlreadyEquipped.isToggled()) {
                        ItemArmor playerArmor;
                        if (armorType == 0) {
                            playerArmor = (ItemArmor)Armour.mc.field_71439_g.func_82169_q(3).func_77973_b();
                        }
                        else if (armorType == 1) {
                            playerArmor = (ItemArmor)Armour.mc.field_71439_g.func_82169_q(2).func_77973_b();
                        }
                        else if (armorType == 2) {
                            playerArmor = (ItemArmor)Armour.mc.field_71439_g.func_82169_q(1).func_77973_b();
                        }
                        else {
                            if (armorType != 3) {
                                continue;
                            }
                            playerArmor = (ItemArmor)Armour.mc.field_71439_g.func_82169_q(0).func_77973_b();
                        }
                        if (armorPiece.func_82812_d().func_78044_b(armorType) > strength && armorPiece.func_82812_d().func_78044_b(armorType) > playerArmor.func_82812_d().func_78044_b(armorType)) {
                            strength = armorPiece.func_82812_d().func_78044_b(armorType);
                            index = slot;
                        }
                    }
                    else if (!Utils.Player.playerWearingArmor().contains(armorPiece.field_77881_a) && armorPiece.field_77881_a == armorType && !Armour.ignoreIfAlreadyEquipped.isToggled() && armorPiece.func_82812_d().func_78044_b(armorType) > strength) {
                        strength = armorPiece.func_82812_d().func_78044_b(armorType);
                        index = slot;
                    }
                }
            }
            if (index > -1 || strength > -1.0) {
                Armour.mc.field_71439_g.field_71071_by.field_70461_c = index;
                this.disable();
                this.onDisable();
                return;
            }
        }
        this.onDisable();
        this.disable();
    }
}
