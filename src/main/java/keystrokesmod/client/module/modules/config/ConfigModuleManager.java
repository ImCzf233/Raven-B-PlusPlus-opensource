package keystrokesmod.client.module.modules.config;

import keystrokesmod.client.config.*;
import keystrokesmod.client.main.*;
import keystrokesmod.client.module.*;
import java.util.*;
import java.util.function.*;

public class ConfigModuleManager
{
    private List<ConfigModule> modules;
    
    public ConfigModuleManager() {
        this.modules = new ArrayList<ConfigModule>();
    }
    
    public void updater(final ArrayList<Config> cfgs) {
        try {
            for (final Config cfg : cfgs) {
                if (!this.hasModule(cfg.getName())) {
                    final ConfigModule m = new ConfigModule(cfg.getName());
                    this.modules.add(m);
                    if (Raven.configManager.getConfig() != cfg) {
                        continue;
                    }
                    m.setToggled(true);
                }
            }
        }
        catch (NullPointerException omgwoawsocoolandedgy) {
            omgwoawsocoolandedgy.printStackTrace();
        }
        Raven.clickGui.getCategoryComponent(Module.ModuleCategory.config).updateModules();
        Raven.clickGui.getCategoryComponent(Module.ModuleCategory.config).r3nd3r();
    }
    
    public List<ConfigModule> getConfigModules() {
        return this.modules;
    }
    
    public boolean hasModule(final String name) {
        return this.modules.stream().map((Function<? super Object, ?>)Module::getName).filter(name::equals).findFirst().isPresent();
    }
    
    public boolean hasConfig(final ArrayList<Config> cfgs, final String name) {
        return this.modules.stream().map((Function<? super Object, ?>)Module::getName).filter(name::equals).findFirst().isPresent();
    }
}
