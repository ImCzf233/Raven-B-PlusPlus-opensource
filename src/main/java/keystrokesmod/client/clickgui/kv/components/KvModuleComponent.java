package keystrokesmod.client.clickgui.kv.components;

import keystrokesmod.client.module.*;
import keystrokesmod.client.clickgui.kv.*;
import net.minecraft.client.gui.*;
import keystrokesmod.client.utils.font.*;

public class KvModuleComponent extends KvComponent
{
    private Module module;
    private int toggleX;
    private int toggleY;
    private int toggleWidth;
    private int toggleHeight;
    
    public KvModuleComponent(final Module module) {
        this.module = module;
    }
    
    @Override
    public void draw(final int mouseX, final int mouseY) {
        KvCompactGui.drawRoundedRect(this.x, this.y, this.x + this.width, this.y + this.height, 12, -16711681);
        Gui.func_73734_a(this.x, this.y + (int)(3 * this.height / 3.8), this.x + this.width, this.y + (int)(3 * this.height / 3.8) + 1, -1);
        Gui.func_73734_a(this.x + this.width - (int)(this.width / 3.8), this.y + (int)(3 * this.height / 3.8), this.x + this.width - (int)(this.width / 3.8) + 1, this.y + this.height, -1);
        FontUtil.normal.drawCenteredStringWithShadow(this.module.getName(), (float)(this.x + this.width / 2), (float)(this.y + this.height - (int)(3 * this.height / 3.8 / 2.0)), -16777216);
        FontUtil.two.drawNoBSCenteredString(this.module.isEnabled() ? "Disabled" : "Enabled", (float)(this.toggleX + this.toggleWidth / 2), (float)this.toggleY, -16777216);
    }
    
    @Override
    public boolean mouseDown(final int x, final int y, final int button) {
        return false;
    }
    
    @Override
    public void mouseReleased(final int x, final int y, final int button) {
    }
    
    @Override
    public void setCoords(final int x, final int y) {
        this.x = x;
        this.y = y;
        this.toggleX = x;
        this.toggleY = y + (this.height - (int)(this.height / 3.8 / 2.0));
        this.toggleWidth = this.width - (int)(this.width / 3.8);
        this.toggleHeight = this.height - this.toggleY;
    }
    
    @Override
    public void setDimensions(final int width, final int height) {
        this.width = width;
        this.height = height;
    }
}
