package keystrokesmod.client.module;

import java.util.*;

public class GuiModuleManager
{
    private final List<GuiModule> guiModules;
    
    public GuiModuleManager() {
        this.guiModules = new ArrayList<GuiModule>();
        Module.ModuleCategory[] values;
        for (int categoryAmount = (values = Module.ModuleCategory.values()).length, category = 1; category < categoryAmount; ++category) {
            this.addModule(new GuiModule(values[category], values[category].getParentCategory()));
        }
    }
    
    public GuiModule getModuleByModuleCategory(final Module.ModuleCategory name) {
        for (final GuiModule module : this.guiModules) {
            if (module.getGuiCategory() == name) {
                return module;
            }
        }
        return null;
    }
    
    public void addModule(final GuiModule m) {
        this.guiModules.add(m);
    }
    
    public List<GuiModule> getModules() {
        return this.guiModules;
    }
}
