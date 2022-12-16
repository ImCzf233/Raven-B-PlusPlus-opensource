package keystrokesmod.client.notifications;

import java.util.concurrent.*;

public class NotificationManager
{
    private static LinkedBlockingQueue<Notification> pendingNotifications;
    private static Notification currentNotification;
    
    public static void show(final Notification notification) {
        NotificationManager.pendingNotifications.add(notification);
    }
    
    public static void update() {
        if (NotificationManager.currentNotification != null && !NotificationManager.currentNotification.isShown()) {
            NotificationManager.currentNotification = null;
        }
        if (NotificationManager.currentNotification == null && !NotificationManager.pendingNotifications.isEmpty()) {
            (NotificationManager.currentNotification = NotificationManager.pendingNotifications.poll()).show();
        }
    }
    
    public static void render() {
        update();
        if (NotificationManager.currentNotification != null) {
            NotificationManager.currentNotification.render();
        }
    }
    
    static {
        NotificationManager.pendingNotifications = new LinkedBlockingQueue<Notification>();
        NotificationManager.currentNotification = null;
    }
}
