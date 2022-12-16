package keystrokesmod.client.clickgui.kv.components;

import keystrokesmod.client.clickgui.kv.*;
import keystrokesmod.client.module.*;
import keystrokesmod.client.main.*;
import java.util.*;
import keystrokesmod.client.utils.font.*;

public class KvCategoryComponent extends KvComponent
{
    private final Module.ModuleCategory category;
    private final List<Module.ModuleCategory> childCategories;
    private final List<KvModuleComponent> modules;
    
    public KvCategoryComponent(final Module.ModuleCategory category) {
        this.category = category;
        this.childCategories = category.getChildCategories();
        this.modules = new ArrayList<KvModuleComponent>();
        for (final Module module : Raven.moduleManager.getModulesInCategory(category)) {
            this.modules.add(new KvModuleComponent(module));
        }
    }
    
    @Override
    public void draw(final int mouseX, final int mouseY) {
        FontUtil.normal.drawStringWithShadow(this.category.getName(), this.x, (float)this.y, (Raven.kvCompactGui.currentCategory == this) ? -256 : (this.isMouseOver(mouseX, mouseY) ? -1610547456 : -16711936));
    }
    
    @Override
    public boolean mouseDown(final int x, final int y, final int button) {
        if (button == 0 && this.isMouseOver(x, y)) {
            Raven.kvCompactGui.setCurrentCategory(this);
            return true;
        }
        return false;
    }
    
    @Override
    public void mouseReleased(final int x, final int y, final int button) {
    }
    
    @Override
    public void setCoords(final int x, final int y) {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public void setDimensions(final int width, final int height) {
        this.width = width;
        this.height = height;
    }
    
    @Override
    public boolean isMouseOver(final int x, final int y) {
        return x > this.x && x < this.x + this.width && y > this.y && y < this.y + this.height;
    }
    
    @Override
    public int getHeight() {
        return this.height;
    }
    
    @Override
    public int getWidth() {
        return this.width;
    }
    
    @Override
    public int getY() {
        return this.y;
    }
    
    @Override
    public int getX() {
        return this.x;
    }
    
    public Module.ModuleCategory getCategory() {
        return this.category;
    }
    
    public List<Module.ModuleCategory> getChildCategory() {
        return this.childCategories;
    }
    
    public List<KvModuleComponent> getModules() {
        return this.modules;
    }
}
