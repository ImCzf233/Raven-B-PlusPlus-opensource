package keystrokesmod.client.hud.impl;

import keystrokesmod.client.hud.*;
import keystrokesmod.client.utils.enums.*;
import keystrokesmod.client.module.modules.*;
import keystrokesmod.client.utils.font.*;
import net.minecraft.util.*;
import net.minecraft.client.gui.*;

public class WatermarkComponent extends HudComponent
{
    @Override
    public EnumSide getSide() {
        return EnumSide.LEFT;
    }
    
    @Override
    public void draw(final boolean editing) {
        final HUD hud = HUD.getInstance();
        if (editing) {
            int width = 0;
            int height = 0;
            switch (HUD.watermarkMode.getMode()) {
                case Normal: {
                    if (HUD.generalMcFont.isToggled()) {
                        final FontRenderer field_71466_p = this.mc.field_71466_p;
                        hud.getClass();
                        width = field_71466_p.func_78256_a("Raven");
                        height = this.mc.field_71466_p.field_78288_b;
                        break;
                    }
                    final keystrokesmod.client.utils.font.FontRenderer normal = FontUtil.normal;
                    hud.getClass();
                    width = (int)normal.getStringWidth("Raven");
                    height = FontUtil.normal.getHeight();
                    break;
                }
            }
            Gui.func_73734_a(this.x, this.y, this.x + width, this.y + height, 1342177280);
        }
        final int color = HUD.watermarkColorMode.getMode().getColor().value(0);
        hud.getClass();
        String clientName = "Raven";
        if (HUD.watermarkOnlyFirstChar.isToggled()) {
            clientName = clientName.substring(0, 1) + EnumChatFormatting.WHITE + clientName.substring(1);
        }
        switch (HUD.watermarkMode.getMode()) {
            case Normal: {
                if (HUD.generalMcFont.isToggled()) {
                    this.mc.field_71466_p.func_175063_a(clientName, (float)this.x, (float)this.y, color);
                    break;
                }
                FontUtil.normal.drawSmoothString(clientName, this.x, (float)this.y, color);
                break;
            }
        }
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
