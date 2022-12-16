package keystrokesmod.client.module;

import keystrokesmod.client.main.*;

public class GuiModule extends Module
{
    private final ModuleCategory moduleCategory;
    
    public GuiModule(final ModuleCategory moduleCategory, final ModuleCategory parentCategory) {
        super(moduleCategory.getName(), parentCategory);
        this.moduleCategory = moduleCategory;
        this.hasBind = false;
        this.showInHud = false;
    }
    
    @Override
    public void onEnable() {
        Raven.clickGui.getCategoryComponent(this.moduleCategory).setVisable(true);
    }
    
    @Override
    public void onDisable() {
        Raven.clickGui.getCategoryComponent(this.moduleCategory).setVisable(false);
    }
    
    public ModuleCategory getGuiCategory() {
        return this.moduleCategory;
    }
}
