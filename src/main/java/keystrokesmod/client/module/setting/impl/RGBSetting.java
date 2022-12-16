package keystrokesmod.client.module.setting.impl;

import keystrokesmod.client.module.setting.*;
import java.awt.*;
import com.google.gson.*;
import keystrokesmod.client.clickgui.raven.components.*;
import keystrokesmod.client.clickgui.raven.*;

public class RGBSetting extends Setting
{
    private final String name;
    private int[] colour;
    private int[] defaultColour;
    private int colorRGB;
    
    public RGBSetting(final String name, final int defaultRed, final int defaultGreen, final int defaultBlue) {
        super(name);
        this.name = name;
        this.defaultColour = new int[] { defaultRed, defaultGreen, defaultBlue };
        this.colour = new int[] { defaultRed, defaultGreen, defaultBlue };
        this.colorRGB = new Color(defaultRed, defaultGreen, defaultBlue).getRGB();
    }
    
    @Override
    public void resetToDefaults() {
        for (int i = 0; i <= this.colour.length; ++i) {
            this.colour[i] = this.defaultColour[i];
        }
    }
    
    @Override
    public JsonObject getConfigAsJson() {
        final JsonObject data = new JsonObject();
        data.addProperty("type", this.getSettingType());
        data.addProperty("red", (Number)this.getRed());
        data.addProperty("green", (Number)this.getGreen());
        data.addProperty("blue", (Number)this.getBlue());
        return data;
    }
    
    @Override
    public String getSettingType() {
        return "rgbsetting";
    }
    
    @Override
    public void applyConfigFromJson(final JsonObject data) {
        if (!data.get("type").getAsString().equals(this.getSettingType())) {
            return;
        }
        this.setRed(data.get("red").getAsInt());
        this.setGreen(data.get("green").getAsInt());
        this.setBlue(data.get("blue").getAsInt());
    }
    
    @Override
    public Component createComponent(final ModuleComponent moduleComponent) {
        return null;
    }
    
    public int getRed() {
        return this.colour[0];
    }
    
    public int getGreen() {
        return this.colour[1];
    }
    
    public int getBlue() {
        return this.colour[2];
    }
    
    public int[] getColors() {
        return this.colour;
    }
    
    public int getColor(final int colour) {
        return this.colour[colour];
    }
    
    public int getRGB() {
        return this.colorRGB;
    }
    
    public void setRed(final int red) {
        this.setColor(0, red);
    }
    
    public void setGreen(final int green) {
        this.setColor(1, green);
    }
    
    public void setBlue(final int blue) {
        this.setColor(2, blue);
    }
    
    public void setColor(final int colour, final int value) {
        this.colour[colour] = value;
        this.colorRGB = new Color(this.colour[0], this.colour[1], this.colour[2]).getRGB();
    }
    
    public Color getColor() {
        return new Color(this.colour[0], this.colour[1], this.colour[2]);
    }
    
    public void setColors(final int[] colour) {
        this.colour = colour.clone();
    }
}
