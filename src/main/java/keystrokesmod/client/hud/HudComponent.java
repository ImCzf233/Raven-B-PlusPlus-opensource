package keystrokesmod.client.hud;

import net.minecraft.client.*;
import keystrokesmod.client.utils.enums.*;
import keystrokesmod.client.module.modules.*;

public abstract class HudComponent
{
    protected final Minecraft mc;
    protected int x;
    protected int y;
    
    public HudComponent() {
        this.mc = Minecraft.func_71410_x();
    }
    
    public abstract EnumSide getSide();
    
    public abstract void draw(final boolean p0);
    
    public abstract boolean isIn(final int p0, final int p1);
    
    public void onClick(final int mouseX, final int mouseY) {
        if (this.isIn(mouseX, mouseY)) {
            HUD.getInstance().setDrag(this, mouseX - this.x, mouseY - this.y);
        }
    }
    
    public int getX() {
        return this.x;
    }
    
    public HudComponent setX(final int x) {
        this.x = x;
        return this;
    }
    
    public int getY() {
        return this.y;
    }
    
    public HudComponent setY(final int y) {
        this.y = y;
        return this;
    }
}
