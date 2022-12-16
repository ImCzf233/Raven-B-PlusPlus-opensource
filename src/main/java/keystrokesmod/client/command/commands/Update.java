package keystrokesmod.client.command.commands;

import keystrokesmod.client.command.*;
import keystrokesmod.client.main.*;
import keystrokesmod.client.clickgui.raven.*;
import keystrokesmod.client.utils.*;
import java.net.*;
import keystrokesmod.client.utils.version.*;

public class Update extends Command
{
    public Update() {
        super("update", "Assists you in updating the client", 0, 0, new String[0], new String[] { "upgrade" });
    }
    
    @Override
    public void onCall(final String[] args) {
        final Version clientVersion = Raven.versionManager.getClientVersion();
        final Version latestVersion = Raven.versionManager.getLatestVersion();
        if (latestVersion.isNewerThan(clientVersion)) {
            Terminal.print("Opening page...");
            URL url = null;
            try {
                url = new URL("https://github.com/K-ov/Raven-bPLUS");
                Utils.Client.openWebpage(url);
                Utils.Client.openWebpage(new URL("https://github.com/K-ov/Raven-bPLUS/raw/stable/build/libs/%5B1.8.9%5D%20BetterKeystrokes%20V-1.2.jar"));
                Terminal.print("Opened page successfully!");
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
                Terminal.print("Failed to open page! Please report this bug in Raven b++'s discord!");
            }
        }
        else {
            Terminal.print("No need to upgrade, You are on the latest build");
        }
    }
}
