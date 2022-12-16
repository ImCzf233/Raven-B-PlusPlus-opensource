package keystrokesmod.keystroke;

import net.minecraft.command.*;

public class KeyStrokeCommand extends CommandBase
{
    public String func_71517_b() {
        return "keystrokesmod";
    }
    
    public void func_71515_b(final ICommandSender sender, final String[] args) {
        KeyStrokeMod.toggleKeyStrokeConfigGui();
    }
    
    public String func_71518_a(final ICommandSender sender) {
        return "/keystrokesmod";
    }
    
    public int func_82362_a() {
        return 0;
    }
    
    public boolean func_71519_b(final ICommandSender sender) {
        return true;
    }
}
