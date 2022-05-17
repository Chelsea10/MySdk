package com.example.webviewdialog;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.annotation.RequiresApi;

public class NLService extends NotificationListenerService {
    public final static String COMMAND = "com.canking.COMMAND_NOTIFICATION_LISTENER_SERVICE";
    public final static String COMMAND_EXTRA = "command";
    public final static String CANCEL_ALL = "clearall";
    public final static String GET_LIST = "list";
    public final static String WEIXIN = "com.tencent.mm";

    private String TAG = "NLService";
    private NLServiceReceiver nlservicereciver;

    @Override
    public void onCreate() {
        super.onCreate();
        nlservicereciver = new NLServiceReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(COMMAND);
        registerReceiver(nlservicereciver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(nlservicereciver);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.i(TAG, "channelId:" + sbn.getNotification().getChannelId() + " ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText + "\t" + sbn.getPackageName());

        Notification notification = sbn.getNotification();
        notification.getChannelId();

        Intent i = new Intent(NotificationActivity.UPDATE);
        i.putExtra(NotificationActivity.EVENT, "接受 :" + sbn.getPackageName() + "\n");
        i.putExtras(notification.extras);
        i.putExtra(NotificationActivity.VIEW_S, notification.contentView);
        i.putExtra(NotificationActivity.View_L, notification.bigContentView);
        sendBroadcast(i);

        onBounReveive(sbn);
    }

    private void onBounReveive(StatusBarNotification sbn) {
        Notification notification = sbn.getNotification();
        String pkg = sbn.getPackageName();
        if (!pkg.equals(WEIXIN)) return;

        String content = notification.extras.getString(Notification.EXTRA_TEXT);
        if (content.contains("[微信红包]")) {
            final PendingIntent pendingIntent = notification.contentIntent;
            try {
                pendingIntent.send();
            } catch (PendingIntent.CanceledException e) {
            }
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i(TAG, "ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText + "\t" + sbn.getPackageName());
        Intent i = new Intent(NotificationActivity.UPDATE);
        i.putExtra(NotificationActivity.EVENT, "移除 :" + sbn.getPackageName() + "\n");
        Notification notification = sbn.getNotification();
        i.putExtras(notification.extras);
        i.putExtra(NotificationActivity.VIEW_S, notification.contentView);
        i.putExtra(NotificationActivity.View_L, notification.bigContentView);

        sendBroadcast(i);
    }

    class NLServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String command = intent.getStringExtra(COMMAND_EXTRA);
            Log.e(TAG, "Command receive:" + command);

            if (command.equals(CANCEL_ALL)) {
                NLService.this.cancelAllNotifications();
            } else if (command.equals(GET_LIST)) {
                int i = 1;
                for (StatusBarNotification sbn : NLService.this.getActiveNotifications()) {
                    Intent i2 = new Intent(NotificationActivity.UPDATE);
                    i2.putExtra(NotificationActivity.EVENT, i + " " + sbn.getPackageName() + "\n");
                    Notification notification = sbn.getNotification();
                    i2.putExtras(notification.extras);
                    i2.putExtra(NotificationActivity.VIEW_S, notification.contentView);
                    i2.putExtra(NotificationActivity.View_L, notification.bigContentView);
                    Log.i(TAG, sbn.getPackageName());
//                    sendBroadcast(i2);
                    i++;
                }
            }

        }
    }


}
