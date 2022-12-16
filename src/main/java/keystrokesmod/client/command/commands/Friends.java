package keystrokesmod.client.command.commands;

import keystrokesmod.client.command.*;
import keystrokesmod.client.module.modules.combat.*;
import keystrokesmod.client.clickgui.raven.*;
import net.minecraft.entity.*;
import java.util.*;

public class Friends extends Command
{
    public Friends() {
        super("friends", "Allows you to manage and view your friends list", 1, 2, new String[] { "add / remove / list", "Player's name" }, new String[] { "f", "amigos", "lonely4ever" });
    }
    
    @Override
    public void onCall(final String[] args) {
        if (args.length == 0) {
            this.listFriends();
        }
        else if (args[0].equalsIgnoreCase("list")) {
            this.listFriends();
        }
        else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("add")) {
                final boolean added = AimAssist.addFriend(args[1]);
                if (added) {
                    Terminal.print("Successfully added " + args[1] + " to your friends list!");
                }
                else {
                    Terminal.print("An error occurred!");
                }
            }
            else if (args[0].equalsIgnoreCase("remove")) {
                final boolean removed = AimAssist.removeFriend(args[1]);
                if (removed) {
                    Terminal.print("Successfully removed " + args[1] + " from your friends list!");
                }
                else {
                    Terminal.print("An error occurred!");
                }
            }
        }
        else {
            this.incorrectArgs();
        }
    }
    
    public void listFriends() {
        if (AimAssist.getFriends().isEmpty()) {
            Terminal.print("You have no friends. :(");
        }
        else {
            Terminal.print("Your friends are:");
            for (final Entity entity : AimAssist.getFriends()) {
                Terminal.print(entity.func_70005_c_());
            }
        }
    }
}
