package keystrokesmod.client.module.modules.movement;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.event.impl.*;
import net.minecraft.client.gui.*;
import keystrokesmod.client.clickgui.raven.*;
import net.minecraft.client.gui.inventory.*;
import org.lwjgl.input.*;
import net.minecraft.client.settings.*;
import com.google.common.eventbus.*;

public class InvMove extends Module
{
    private final DescriptionSetting ds;
    private final ComboSetting<Mode> mode;
    
    public InvMove() {
        super("InvMove", ModuleCategory.movement);
        this.registerSetting(this.mode = new ComboSetting<Mode>("Mode", Mode.Vanilla));
        this.registerSetting(this.ds = new DescriptionSetting("Vanilla does NOT work on Hypixel!"));
    }
    
    @Subscribe
    public void onTick(final TickEvent e) {
        if (InvMove.mc.field_71462_r != null) {
            if (InvMove.mc.field_71462_r instanceof GuiChat) {
                return;
            }
            boolean doReturn = false;
            switch (this.mode.getMode()) {
                case Undetectable: {
                    if (!(InvMove.mc.field_71462_r instanceof ClickGui)) {
                        doReturn = true;
                        break;
                    }
                    break;
                }
                case Test: {
                    if (InvMove.mc.field_71462_r instanceof GuiContainer && !(InvMove.mc.field_71462_r instanceof GuiInventory)) {
                        doReturn = true;
                        break;
                    }
                    break;
                }
            }
            if (doReturn) {
                return;
            }
            KeyBinding.func_74510_a(InvMove.mc.field_71474_y.field_74351_w.func_151463_i(), Keyboard.isKeyDown(InvMove.mc.field_71474_y.field_74351_w.func_151463_i()));
            KeyBinding.func_74510_a(InvMove.mc.field_71474_y.field_74368_y.func_151463_i(), Keyboard.isKeyDown(InvMove.mc.field_71474_y.field_74368_y.func_151463_i()));
            KeyBinding.func_74510_a(InvMove.mc.field_71474_y.field_74366_z.func_151463_i(), Keyboard.isKeyDown(InvMove.mc.field_71474_y.field_74366_z.func_151463_i()));
            KeyBinding.func_74510_a(InvMove.mc.field_71474_y.field_74370_x.func_151463_i(), Keyboard.isKeyDown(InvMove.mc.field_71474_y.field_74370_x.func_151463_i()));
            KeyBinding.func_74510_a(InvMove.mc.field_71474_y.field_74314_A.func_151463_i(), Keyboard.isKeyDown(InvMove.mc.field_71474_y.field_74314_A.func_151463_i()));
        }
    }
    
    public enum Mode
    {
        Vanilla, 
        Undetectable, 
        Test;
    }
}
