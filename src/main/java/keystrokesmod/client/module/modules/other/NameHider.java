package keystrokesmod.client.module.modules.other;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.utils.*;
import keystrokesmod.client.module.setting.*;

public class NameHider extends Module
{
    public static DescriptionSetting a;
    public static String n;
    public static String playerNick;
    
    public NameHider() {
        super("Name Hider", ModuleCategory.other);
        this.registerSetting(NameHider.a = new DescriptionSetting(Utils.Java.capitalizeWord("command") + ": cname [name]"));
    }
    
    public static String format(String s) {
        if (NameHider.mc.field_71439_g != null) {
            s = (NameHider.playerNick.isEmpty() ? s.replace(NameHider.mc.field_71439_g.func_70005_c_(), NameHider.n) : s.replace(NameHider.playerNick, NameHider.n));
        }
        return s;
    }
    
    static {
        NameHider.n = "ravenb++";
        NameHider.playerNick = "";
    }
}
