package keystrokesmod.client.module.modules.other;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import java.awt.*;
import keystrokesmod.client.event.impl.*;
import keystrokesmod.client.utils.*;
import org.lwjgl.input.*;
import net.minecraft.item.*;
import com.google.common.eventbus.*;
import keystrokesmod.client.module.modules.combat.*;
import net.minecraft.entity.*;

public class MiddleClick extends Module
{
    public static ComboSetting actionSetting;
    public static TickSetting showHelp;
    int prevSlot;
    public static boolean a;
    private Robot bot;
    private boolean hasClicked;
    private int pearlEvent;
    
    public MiddleClick() {
        super("Middleclick", ModuleCategory.other);
        this.registerSetting(MiddleClick.showHelp = new TickSetting("Show friend help in chat", true));
        this.registerSetting(MiddleClick.actionSetting = new ComboSetting("On click", (T)Action.ThrowPearl));
    }
    
    @Override
    public void onEnable() {
        try {
            this.bot = new Robot();
        }
        catch (AWTException var2) {
            this.disable();
        }
        this.hasClicked = false;
        this.pearlEvent = 4;
    }
    
    @Subscribe
    public void onTick(final TickEvent e) {
        if (!Utils.Player.isPlayerInGame()) {
            return;
        }
        if (this.pearlEvent < 4) {
            if (this.pearlEvent == 3) {
                MiddleClick.mc.field_71439_g.field_71071_by.field_70461_c = this.prevSlot;
            }
            ++this.pearlEvent;
        }
        if (Mouse.isButtonDown(2) && !this.hasClicked) {
            if (Action.ThrowPearl.equals(MiddleClick.actionSetting.getMode())) {
                for (int slot = 0; slot <= 8; ++slot) {
                    final ItemStack itemInSlot = MiddleClick.mc.field_71439_g.field_71071_by.func_70301_a(slot);
                    if (itemInSlot != null && itemInSlot.func_77973_b() instanceof ItemEnderPearl) {
                        this.prevSlot = MiddleClick.mc.field_71439_g.field_71071_by.field_70461_c;
                        MiddleClick.mc.field_71439_g.field_71071_by.field_70461_c = slot;
                        this.bot.mousePress(4);
                        this.bot.mouseRelease(4);
                        this.pearlEvent = 0;
                        this.hasClicked = true;
                        return;
                    }
                }
            }
            else if (Action.AddFriend.equals(MiddleClick.actionSetting.getMode())) {
                this.addFriend();
                if (MiddleClick.showHelp.isToggled()) {
                    this.showHelpMessage();
                }
            }
            else if (Action.RemoveFriend.equals(MiddleClick.actionSetting.getMode())) {
                this.removeFriend();
                if (MiddleClick.showHelp.isToggled()) {
                    this.showHelpMessage();
                }
            }
            this.hasClicked = true;
        }
        else if (!Mouse.isButtonDown(2) && this.hasClicked) {
            this.hasClicked = false;
        }
    }
    
    private void showHelpMessage() {
        if (MiddleClick.showHelp.isToggled()) {
            Utils.Player.sendMessageToSelf("Run 'help friends' in CommandLine to find out how to add, remove and view friends.");
        }
    }
    
    private void removeFriend() {
        final Entity player = MiddleClick.mc.field_71476_x.field_72308_g;
        if (player == null) {
            Utils.Player.sendMessageToSelf("Please aim at a player/entity when removing them.");
        }
        else if (AimAssist.removeFriend(player)) {
            Utils.Player.sendMessageToSelf("Successfully removed " + player.func_70005_c_() + " from friends list!");
        }
        else {
            Utils.Player.sendMessageToSelf(player.func_70005_c_() + " was not found in the friends list!");
        }
    }
    
    private void addFriend() {
        final Entity player = MiddleClick.mc.field_71476_x.field_72308_g;
        if (player == null) {
            Utils.Player.sendMessageToSelf("Please aim at a player/entity when adding them.");
        }
        else {
            AimAssist.addFriend(player);
            Utils.Player.sendMessageToSelf("Successfully added " + player.func_70005_c_() + " to friends list.");
        }
    }
    
    public enum Action
    {
        ThrowPearl, 
        AddFriend, 
        RemoveFriend;
    }
}
