package xyz.LYGDLOG.noforget;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
public class ReminderBroadcast extends BroadcastReceiver {

    public ReminderBroadcast() {}

    private static final String CHANNEL_ID = "notifyLembit";
    SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd-HH:mm", Locale.getDefault());
    Date currentDate = new Date();// 获取当前日期时间
    String formattedDateTime = sdf.format(currentDate);// 格式化日期时间为字符串
    NotificationCompat.Builder customNotification;

    @Override
    public void onReceive(Context context, Intent intent) {

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        if (vibrator != null) vibrator.vibrate(new long[]{0, 4000}, -1);

        Toast.makeText(context, "起床了！！起床了！！", Toast.LENGTH_LONG).show();

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        if (alarmUri == null)
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);

        if (ringtone != null) ringtone.play();
    }
}