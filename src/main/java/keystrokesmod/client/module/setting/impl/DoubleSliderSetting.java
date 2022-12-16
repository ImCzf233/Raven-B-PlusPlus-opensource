package keystrokesmod.client.module.setting.impl;

import keystrokesmod.client.module.setting.*;
import com.google.gson.*;
import keystrokesmod.client.clickgui.raven.components.*;
import keystrokesmod.client.clickgui.raven.*;
import java.math.*;

public class DoubleSliderSetting extends Setting
{
    private final String name;
    private double valMax;
    private double valMin;
    private final double max;
    private final double min;
    private final double interval;
    private final double defaultValMin;
    private final double defaultValMax;
    
    public DoubleSliderSetting(final String settingName, final double defaultValueMin, final double defaultValueMax, final double min, final double max, final double intervals) {
        super(settingName);
        this.name = settingName;
        this.valMin = defaultValueMin;
        this.valMax = defaultValueMax;
        this.min = min;
        this.max = max;
        this.interval = intervals;
        this.defaultValMin = this.valMin;
        this.defaultValMax = this.valMax;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public void resetToDefaults() {
        this.setValueMin(this.defaultValMin);
        this.setValueMax(this.defaultValMax);
    }
    
    @Override
    public JsonObject getConfigAsJson() {
        final JsonObject data = new JsonObject();
        data.addProperty("type", this.getSettingType());
        data.addProperty("valueMin", (Number)this.getInputMin());
        data.addProperty("valueMax", (Number)this.getInputMax());
        return data;
    }
    
    @Override
    public String getSettingType() {
        return "doubleslider";
    }
    
    @Override
    public void applyConfigFromJson(final JsonObject data) {
        if (!data.get("type").getAsString().equals(this.getSettingType())) {
            return;
        }
        this.setValueMax(data.get("valueMax").getAsDouble());
        this.setValueMin(data.get("valueMin").getAsDouble());
    }
    
    @Override
    public Component createComponent(final ModuleComponent moduleComponent) {
        return null;
    }
    
    public double getInputMin() {
        return round(this.valMin, 2);
    }
    
    public double getInputMax() {
        return round(this.valMax, 2);
    }
    
    public double getMin() {
        return this.min;
    }
    
    public double getMax() {
        return this.max;
    }
    
    public void setValueMin(double n) {
        n = correct(n, this.min, this.valMax);
        n = Math.round(n * (1.0 / this.interval)) / (1.0 / this.interval);
        this.valMin = n;
    }
    
    public void setValueMax(double n) {
        n = correct(n, this.valMin, this.max);
        n = Math.round(n * (1.0 / this.interval)) / (1.0 / this.interval);
        this.valMax = n;
    }
    
    public static double correct(double val, final double min, final double max) {
        val = Math.max(min, val);
        val = Math.min(max, val);
        return val;
    }
    
    public static double round(final double val, final int p) {
        if (p < 0) {
            return 0.0;
        }
        BigDecimal bd = new BigDecimal(val);
        bd = bd.setScale(p, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
