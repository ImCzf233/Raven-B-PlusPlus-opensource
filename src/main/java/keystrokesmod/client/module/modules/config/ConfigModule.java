package keystrokesmod.client.module.modules.config;

import keystrokesmod.client.module.*;
import keystrokesmod.client.main.*;
import org.lwjgl.input.*;

public class ConfigModule extends Module
{
    public static ConfigModule currentConfig;
    public boolean checked;
    
    public ConfigModule(final String cfgName) {
        super(cfgName, ModuleCategory.config);
        this.clientConfig = true;
        this.showInHud = false;
    }
    
    @Override
    public void toggle() {
        Raven.configManager.save();
        Raven.configManager.loadConfigByName(this.getName());
        ConfigModule.currentConfig = this;
    }
    
    @Override
    public boolean isEnabled() {
        return ConfigModule.currentConfig == this;
    }
    
    @Override
    public void keybind() {
        if (!this.isEnabled() && this.keycode != 0 && Keyboard.isKeyDown(this.keycode)) {
            this.toggle();
        }
    }
}
