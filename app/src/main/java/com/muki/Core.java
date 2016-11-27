package com.muki;


import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;

public class Core {
    private static Core instance = null;
    public Context mainContext;
    public Activity mainActivity;
    public SharedPreferences prefslog = null;
    public String nowAction;
    public Player player;


    public static Core getInstance() {  //метод для получения экземпляра класса core
        if (instance == null)
            instance = new Core();
        return instance;
    }



    public void start() {
        if (mainContext == null)
            return;
        Preferences.setContext(mainActivity);
        if (prefslog == null)
            prefslog = PreferenceManager.getDefaultSharedPreferences(mainContext);
        if (player == null)
            player = new Player();
    }

    public void setContext(Activity _context) {
        mainActivity = _context;
        mainContext = _context;
    }




    public void notifications(String name, String mess, long time) {  //уведомления. Приходят пользователю, если приложение свернуто или он находится в других фрагментах (не в ChatAction - cамом чате)
        while (mess.length() <= 15) {
            mess += " ";
        }
        Intent notificationIntent = new Intent(mainContext, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(mainContext,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Resources res = mainContext.getResources();
        Notification.Builder builder = new Notification.Builder(mainContext);

        builder.setContentIntent(contentIntent)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                .setTicker(mess.substring(0, 15))
                .setWhen(time)
                .setAutoCancel(true)
                .setContentTitle(name)
                .setContentText(mess);

        Notification notification = builder.getNotification();

        NotificationManager notificationManager = (NotificationManager) mainContext
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);

    }





}