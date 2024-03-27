package xyz.LYGDLOG.noforget;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

public class NoforgetPlugin implements FlutterPlugin, MethodCallHandler {
    //  MethodChannel 将 Flutter 和原生 Android 进行通信
    //  这个本地引用用于向 Flutter Engine 注册插件并注销它
    //  当 Flutter 引擎与 Activity 分离时
    private MethodChannel channel;
    private Context context;
    SimpleDateFormat sdf = new SimpleDateFormat(
            "yy/MM/dd-HH:mm", Locale.getDefault());
    Date currentDate = new Date();// 获取当前日期时间
    String formattedDateTime = sdf.format(currentDate);

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        channel = new MethodChannel(
                flutterPluginBinding.getBinaryMessenger(), "noforget");

        channel.setMethodCallHandler(this);

        // 获取上下文
        context = flutterPluginBinding.getApplicationContext();
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        if (call.method.equals("getPlatformVersion")) {

            triggerAlarm();
            triggerNotification();

            result.success("你使用的Android版本：" + android.os.Build.VERSION.RELEASE);
        } else {
            result.notImplemented();
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }

    private void triggerAlarm() {
        AlarmManager alarmManager = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);

        // 设置提醒，这里是每天早上8点触发
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 4);
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent
                .getBroadcast(context, 0,
                        new Intent(context, ReminderBroadcast.class),
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pendingIntent
        );
    }

    private void triggerNotification() {

        RemoteViews notificationLayout = new RemoteViews(
                context.getPackageName(),
                R.layout.notification_small);

        notificationLayout.setTextViewText(
                R.id.notification_title,
                "小模板的 文本内容: " + formattedDateTime);//修改小模板的 文本内容

        RemoteViews notificationLayoutExpanded = new RemoteViews(
                context.getPackageName(),
                R.drawable.tv_song2);

        notificationLayoutExpanded.setTextViewText(
                R.id.tv_song_id1, "现在将这个文字修改了1");

        notificationLayoutExpanded.setTextViewText(
                R.id.tv_song_id2, "现在将这个文字修改了2");

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat
                .Builder(context, "channel_id")
                .setSmallIcon(R.drawable.circle)
                .setCustomContentView(notificationLayout)//小样式 模板
                .setCustomBigContentView(notificationLayoutExpanded)//大样式 模板
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // 通知管理器发送通知
        notificationManager.notify(1, builder.build());
    }
}
