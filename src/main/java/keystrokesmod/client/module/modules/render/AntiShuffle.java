package keystrokesmod.client.module.modules.render;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.utils.*;
import keystrokesmod.client.module.setting.*;

public class AntiShuffle extends Module
{
    public static DescriptionSetting a;
    private static final String c = "¡ìk";
    
    public AntiShuffle() {
        super("AntiShuffle", ModuleCategory.render);
        this.registerSetting(AntiShuffle.a = new DescriptionSetting(Utils.Java.capitalizeWord("remove") + " &k"));
    }
    
    public static String getUnformattedTextForChat(final String s) {
        return s.replace("¡ìk", "");
    }
}
