package keystrokesmod.client.module.setting;

import com.google.gson.*;
import keystrokesmod.client.clickgui.raven.components.*;
import keystrokesmod.client.clickgui.raven.*;

public abstract class Setting
{
    public String settingName;
    
    public Setting(final String name) {
        this.settingName = name;
    }
    
    public String getName() {
        return this.settingName;
    }
    
    public abstract void resetToDefaults();
    
    public abstract JsonObject getConfigAsJson();
    
    public abstract String getSettingType();
    
    public abstract void applyConfigFromJson(final JsonObject p0);
    
    public abstract Component createComponent(final ModuleComponent p0);
}
