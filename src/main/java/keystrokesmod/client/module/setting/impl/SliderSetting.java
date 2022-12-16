package keystrokesmod.client.module.setting.impl;

import keystrokesmod.client.module.setting.*;
import com.google.gson.*;
import keystrokesmod.client.clickgui.raven.components.*;
import keystrokesmod.client.clickgui.raven.*;
import java.math.*;

public class SliderSetting extends Setting
{
    private final String name;
    private double value;
    private final double max;
    private final double min;
    private final double interval;
    private final double defaultVal;
    
    public SliderSetting(final String settingName, final double defaultValue, final double min, final double max, final double intervals) {
        super(settingName);
        this.name = settingName;
        this.value = defaultValue;
        this.min = min;
        this.max = max;
        this.interval = intervals;
        this.defaultVal = defaultValue;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public void resetToDefaults() {
        this.value = this.defaultVal;
    }
    
    @Override
    public JsonObject getConfigAsJson() {
        final JsonObject data = new JsonObject();
        data.addProperty("type", this.getSettingType());
        data.addProperty("value", (Number)this.getInput());
        return data;
    }
    
    @Override
    public String getSettingType() {
        return "slider";
    }
    
    @Override
    public void applyConfigFromJson(final JsonObject data) {
        if (!data.get("type").getAsString().equals(this.getSettingType())) {
            return;
        }
        this.setValue(data.get("value").getAsDouble());
    }
    
    @Override
    public Component createComponent(final ModuleComponent moduleComponent) {
        return null;
    }
    
    public double getInput() {
        return r(this.value, 2);
    }
    
    public double getMin() {
        return this.min;
    }
    
    public double getMax() {
        return this.max;
    }
    
    public void setValue(double n) {
        n = check(n, this.min, this.max);
        n = Math.round(n * (1.0 / this.interval)) / (1.0 / this.interval);
        this.value = n;
    }
    
    public static double check(double v, final double i, final double a) {
        v = Math.max(i, v);
        v = Math.min(a, v);
        return v;
    }
    
    public static double r(final double v, final int p) {
        if (p < 0) {
            return 0.0;
        }
        BigDecimal bd = new BigDecimal(v);
        bd = bd.setScale(p, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
