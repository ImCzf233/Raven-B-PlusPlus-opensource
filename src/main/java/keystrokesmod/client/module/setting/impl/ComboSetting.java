package keystrokesmod.client.module.setting.impl;

import keystrokesmod.client.module.setting.*;
import com.google.gson.*;
import keystrokesmod.client.clickgui.raven.components.*;
import keystrokesmod.client.clickgui.raven.*;

public class ComboSetting<T extends Enum<?>> extends Setting
{
    private T[] options;
    private T currentOption;
    private final T defaultOption;
    
    public ComboSetting(final String settingName, final T defaultOption) {
        super(settingName);
        this.currentOption = defaultOption;
        this.defaultOption = defaultOption;
        try {
            this.options = (T[])defaultOption.getClass().getMethod("values", (Class<?>[])new Class[0]).invoke(null, new Object[0]);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void resetToDefaults() {
        this.currentOption = this.defaultOption;
    }
    
    @Override
    public JsonObject getConfigAsJson() {
        final JsonObject data = new JsonObject();
        data.addProperty("type", this.getSettingType());
        data.addProperty("value", this.getMode().toString());
        return data;
    }
    
    @Override
    public String getSettingType() {
        return "mode";
    }
    
    @Override
    public void applyConfigFromJson(final JsonObject data) {
        if (!data.get("type").getAsString().equals(this.getSettingType())) {
            return;
        }
        final String bruh = data.get("value").getAsString();
        for (final T opt : this.options) {
            if (opt.toString().equals(bruh)) {
                this.setMode(opt);
            }
        }
    }
    
    @Override
    public Component createComponent(final ModuleComponent moduleComponent) {
        return null;
    }
    
    public T getMode() {
        return this.currentOption;
    }
    
    public void setMode(final T value) {
        this.currentOption = value;
    }
    
    public void nextMode() {
        for (int i = 0; i < this.options.length; ++i) {
            if (this.options[i] == this.currentOption) {
                this.currentOption = this.options[(i + 1) % this.options.length];
                return;
            }
        }
    }
}
