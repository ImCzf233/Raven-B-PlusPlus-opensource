package keystrokesmod.client.module.modules.config;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.main.*;

public class ConfigSettings extends Module
{
    public TickSetting updateConfig;
    
    public ConfigSettings() {
        super("Config", ModuleCategory.config);
        this.registerSetting(this.updateConfig = new TickSetting("Update ConfigList", false));
    }
    
    @Override
    public boolean canBeEnabled() {
        return false;
    }
    
    @Override
    public void guiButtonToggled(final TickSetting b) {
        b.setEnabled(false);
        Raven.configManager.discoverConfigs();
    }
}
