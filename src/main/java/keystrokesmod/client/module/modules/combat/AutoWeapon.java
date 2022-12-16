package keystrokesmod.client.module.modules.combat;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.event.impl.*;
import keystrokesmod.client.utils.*;
import org.lwjgl.input.*;
import com.google.common.eventbus.*;

public class AutoWeapon extends Module
{
    public static TickSetting onlyWhenHoldingDown;
    public static TickSetting goBackToPrevSlot;
    private boolean onWeapon;
    private int prevSlot;
    
    public AutoWeapon() {
        super("AutoWeapon", ModuleCategory.combat);
        this.registerSetting(AutoWeapon.onlyWhenHoldingDown = new TickSetting("Only when holding lmb", true));
        this.registerSetting(AutoWeapon.goBackToPrevSlot = new TickSetting("Revert to old slot", true));
    }
    
    @Subscribe
    public void onRender2D(final Render2DEvent ev) {
        if (!Utils.Player.isPlayerInGame() || AutoWeapon.mc.field_71462_r != null) {
            return;
        }
        if (AutoWeapon.mc.field_71476_x == null || AutoWeapon.mc.field_71476_x.field_72308_g == null || (AutoWeapon.onlyWhenHoldingDown.isToggled() && !Mouse.isButtonDown(0))) {
            if (this.onWeapon) {
                this.onWeapon = false;
                if (AutoWeapon.goBackToPrevSlot.isToggled()) {
                    AutoWeapon.mc.field_71439_g.field_71071_by.field_70461_c = this.prevSlot;
                }
            }
        }
        else {
            if (AutoWeapon.onlyWhenHoldingDown.isToggled() && !Mouse.isButtonDown(0)) {
                return;
            }
            if (!this.onWeapon) {
                this.prevSlot = AutoWeapon.mc.field_71439_g.field_71071_by.field_70461_c;
                this.onWeapon = true;
                final int maxDamageSlot = Utils.Player.getMaxDamageSlot();
                if (maxDamageSlot > 0 && Utils.Player.getSlotDamage(maxDamageSlot) > Utils.Player.getSlotDamage(AutoWeapon.mc.field_71439_g.field_71071_by.field_70461_c)) {
                    AutoWeapon.mc.field_71439_g.field_71071_by.field_70461_c = maxDamageSlot;
                }
            }
        }
    }
}
