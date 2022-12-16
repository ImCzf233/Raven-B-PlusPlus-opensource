package keystrokesmod.client.notifications;

import net.minecraft.client.*;
import keystrokesmod.client.event.impl.*;
import keystrokesmod.client.module.modules.client.*;
import com.google.common.eventbus.*;
import keystrokesmod.client.module.*;
import keystrokesmod.client.config.*;
import keystrokesmod.client.main.*;
import net.minecraft.client.gui.*;

public class NotificationRenderer
{
    public static final NotificationRenderer notificationRenderer;
    private static Minecraft mc;
    
    @Subscribe
    public void onRender(final Render2DEvent e) {
        if (GuiModule.notifications()) {
            NotificationManager.render();
        }
    }
    
    public static void moduleStateChanged(final Module m) {
        if (!GuiModule.notifications() || NotificationRenderer.mc.field_71462_r != null || ConfigManager.applyingConfig || ClientConfig.applyingConfig) {
            return;
        }
        if (!m.getClass().equals(Gui.class)) {
            final String s = m.isEnabled() ? "enabled" : "disabled";
            NotificationManager.show(new Notification(NotificationType.INFO, "Module " + s, m.getName() + " has been " + s, 1));
        }
    }
    
    static {
        notificationRenderer = new NotificationRenderer();
        NotificationRenderer.mc = Minecraft.func_71410_x();
    }
}
