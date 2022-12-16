package keystrokesmod.client.module.modules.other;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.utils.*;
import keystrokesmod.client.module.setting.*;
import net.minecraft.util.*;

public class FakeChat extends Module
{
    public static DescriptionSetting a;
    public static String msg;
    public static final String command = "fakechat";
    public static final String c4 = "&cInvalid message.";
    
    public FakeChat() {
        super("Fake Chat", ModuleCategory.other);
        this.registerSetting(FakeChat.a = new DescriptionSetting(Utils.Java.capitalizeWord("command") + ": " + "fakechat" + " [msg]"));
    }
    
    @Override
    public void onEnable() {
        if (FakeChat.msg.contains("\\n")) {
            final String[] split2;
            final String[] split = split2 = FakeChat.msg.split("\\\\n");
            for (final String s : split2) {
                this.sm(s);
            }
        }
        else {
            this.sm(FakeChat.msg);
        }
        this.disable();
    }
    
    private void sm(final String txt) {
        FakeChat.mc.field_71439_g.func_145747_a((IChatComponent)new ChatComponentText(Utils.Client.reformat(txt)));
    }
    
    static {
        FakeChat.msg = "&eThis is a fake chat message.";
    }
}
