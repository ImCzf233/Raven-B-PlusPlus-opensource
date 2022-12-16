package keystrokesmod.client.module.setting.impl;

import keystrokesmod.client.module.setting.*;
import com.google.gson.*;
import keystrokesmod.client.clickgui.raven.components.*;
import keystrokesmod.client.clickgui.raven.*;

public class TickSetting extends Setting
{
    private final String name;
    private boolean isEnabled;
    private final boolean defaultValue;
    
    public TickSetting(final String name, final boolean isEnabled) {
        super(name);
        this.name = name;
        this.isEnabled = isEnabled;
        this.defaultValue = isEnabled;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public void resetToDefaults() {
        this.isEnabled = this.defaultValue;
    }
    
    @Override
    public JsonObject getConfigAsJson() {
        final JsonObject data = new JsonObject();
        data.addProperty("type", this.getSettingType());
        data.addProperty("value", Boolean.valueOf(this.isToggled()));
        return data;
    }
    
    @Override
    public String getSettingType() {
        return "tick";
    }
    
    @Override
    public void applyConfigFromJson(final JsonObject data) {
        if (!data.get("type").getAsString().equals(this.getSettingType())) {
            return;
        }
        this.setEnabled(data.get("value").getAsBoolean());
    }
    
    @Override
    public Component createComponent(final ModuleComponent moduleComponent) {
        return null;
    }
    
    public boolean isToggled() {
        return this.isEnabled;
    }
    
    public void toggle() {
        this.isEnabled = !this.isEnabled;
    }
    
    public void enable() {
        this.isEnabled = true;
    }
    
    public void disable() {
        this.isEnabled = false;
    }
    
    public void setEnabled(final boolean b) {
        this.isEnabled = b;
    }
}
