package keystrokesmod.client.module.setting.impl;

import keystrokesmod.client.module.setting.*;
import com.google.gson.*;
import keystrokesmod.client.clickgui.raven.components.*;
import keystrokesmod.client.clickgui.raven.*;

public class DescriptionSetting extends Setting
{
    private String desc;
    private final String defaultDesc;
    
    public DescriptionSetting(final String t) {
        super(t);
        this.desc = t;
        this.defaultDesc = t;
    }
    
    public String getDesc() {
        return this.desc;
    }
    
    public void setDesc(final String t) {
        this.desc = t;
    }
    
    @Override
    public void resetToDefaults() {
        this.desc = this.defaultDesc;
    }
    
    @Override
    public JsonObject getConfigAsJson() {
        final JsonObject data = new JsonObject();
        data.addProperty("type", this.getSettingType());
        data.addProperty("value", this.getDesc());
        return data;
    }
    
    @Override
    public String getSettingType() {
        return "desc";
    }
    
    @Override
    public void applyConfigFromJson(final JsonObject data) {
        if (!data.get("type").getAsString().equals(this.getSettingType())) {
            return;
        }
        this.setDesc(data.get("value").getAsString());
    }
    
    @Override
    public Component createComponent(final ModuleComponent moduleComponent) {
        return null;
    }
}
