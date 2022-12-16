package keystrokesmod.client.module.modules.client;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.utils.*;
import keystrokesmod.client.module.setting.*;
import java.util.concurrent.*;
import keystrokesmod.client.main.*;
import java.net.*;
import keystrokesmod.client.utils.version.*;
import keystrokesmod.client.event.impl.*;
import com.google.common.eventbus.*;

public class UpdateCheck extends Module
{
    public static DescriptionSetting howToUse;
    public static TickSetting copyToClipboard;
    public static TickSetting openLink;
    private Future<?> f;
    private final ExecutorService executor;
    private final Runnable task;
    
    public UpdateCheck() {
        super("Update", ModuleCategory.client);
        this.registerSetting(UpdateCheck.howToUse = new DescriptionSetting(Utils.Java.capitalizeWord("command") + ": update"));
        this.registerSetting(UpdateCheck.copyToClipboard = new TickSetting("Copy to clipboard", true));
        this.registerSetting(UpdateCheck.openLink = new TickSetting("Open dl in browser", true));
        this.executor = Executors.newFixedThreadPool(1);
        final Version latest;
        final Version current;
        URL url;
        this.task = (() -> {
            latest = Raven.versionManager.getLatestVersion();
            current = Raven.versionManager.getClientVersion();
            if (latest.isNewerThan(current)) {
                Utils.Player.sendMessageToSelf("The current version or Raven B++ is outdated. Visit https://github.com/K-ov/Raven-bPLUS/tree/stable/build/libs to download the latest version.");
                Utils.Player.sendMessageToSelf("https://github.com/K-ov/Raven-bPLUS/tree/stable/build/libs");
            }
            if (current.isNewerThan(latest)) {
                Utils.Player.sendMessageToSelf("You are on a beta build of raven");
                Utils.Player.sendMessageToSelf("https://github.com/K-ov/Raven-bPLUS");
            }
            else {
                Utils.Player.sendMessageToSelf("You are on the latest public version!");
            }
            if (UpdateCheck.copyToClipboard.isToggled() && Utils.Client.copyToClipboard("https://github.com/K-ov/Raven-bPLUS/raw/stable/build/libs/%5B1.8.9%5D%20BetterKeystrokes%20V-1.2.jar")) {
                Utils.Player.sendMessageToSelf("Successfully copied download link to clipboard!");
            }
            Utils.Player.sendMessageToSelf("https://github.com/K-ov/Raven-bPLUS");
            if (UpdateCheck.openLink.isToggled()) {
                try {
                    url = new URL("https://github.com/K-ov/Raven-bPLUS");
                    Utils.Client.openWebpage(url);
                    Utils.Client.openWebpage(new URL("https://github.com/K-ov/Raven-bPLUS/raw/stable/build/libs/%5B1.8.9%5D%20BetterKeystrokes%20V-1.2.jar"));
                }
                catch (MalformedURLException bruh) {
                    bruh.printStackTrace();
                    Utils.Player.sendMessageToSelf("&cFailed to open page! Please report this bug in Raven b++'s discord");
                }
            }
            this.disable();
        });
    }
    
    @Subscribe
    public void onGameLoop(final GameLoopEvent e) {
        if (this.f == null) {
            this.f = this.executor.submit(this.task);
            Utils.Player.sendMessageToSelf("Update check started!");
        }
        else if (this.f.isDone()) {
            this.f = this.executor.submit(this.task);
            Utils.Player.sendMessageToSelf("Update check started!");
        }
    }
}
