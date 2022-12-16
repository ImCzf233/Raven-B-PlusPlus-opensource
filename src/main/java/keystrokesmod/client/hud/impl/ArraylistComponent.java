package keystrokesmod.client.hud.impl;

import keystrokesmod.client.hud.*;
import keystrokesmod.client.utils.enums.*;
import keystrokesmod.client.module.modules.*;
import keystrokesmod.client.main.*;
import keystrokesmod.client.utils.*;
import java.util.*;
import keystrokesmod.client.utils.font.*;
import net.minecraft.client.gui.*;
import keystrokesmod.client.module.*;

public class ArraylistComponent extends HudComponent
{
    @Override
    public EnumSide getSide() {
        return EnumSide.getSide(this.x, new ScaledResolution(this.mc));
    }
    
    @Override
    public void draw(final boolean editing) {
        final HUD hud = HUD.getInstance();
        ColorSupplier color = null;
        switch (HUD.watermarkColorMode.getMode()) {
            case Static: {
                color = HUD.ColorMode.Static.getColor();
                break;
            }
            case Rainbow: {
                color = HUD.ColorMode.Rainbow.getColor();
                break;
            }
            case Fade: {
                color = HUD.ColorMode.Fade.getColor();
                break;
            }
            case Astolfo: {
                color = HUD.ColorMode.Astolfo.getColor();
                break;
            }
        }
        if (color == null) {
            return;
        }
        final List<String> modules = new ArrayList<String>();
        Raven.moduleManager.getEnabledModules().forEach(module -> modules.add(module.getName()));
        final EnumSide side = this.getSide();
    }
    
    @Override
    public boolean isIn(final int mouseX, final int mouseY) {
        final HUD hud = HUD.getInstance();
        switch (HUD.watermarkMode.getMode()) {
            case Normal: {
                if (HUD.generalMcFont.isToggled()) {
                    if (mouseX > this.x) {
                        final int x = this.x;
                        final FontRenderer field_71466_p = this.mc.field_71466_p;
                        hud.getClass();
                        if (mouseX < x + field_71466_p.func_78256_a("Raven") && mouseY > this.y && mouseY < this.y + this.mc.field_71466_p.field_78288_b) {
                            return true;
                        }
                    }
                    return false;
                }
                if (mouseX > this.x) {
                    final double n = mouseX;
                    final double n2 = this.x;
                    final keystrokesmod.client.utils.font.FontRenderer normal = FontUtil.normal;
                    hud.getClass();
                    if (n < n2 + normal.getStringWidth("Raven") && mouseY > this.y && mouseY < this.y + FontUtil.normal.getHeight()) {
                        return true;
                    }
                }
                return false;
            }
            default: {
                return false;
            }
        }
    }
}
