package e.joaopaulo.tcc;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import static android.content.Context.NOTIFICATION_SERVICE;

public class Alarme extends BroadcastReceiver{


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("lalala2","ta dando");

        String msg = "Revisao";

        intent.putExtra("mensagem", msg);

        //passar pra intent e pegar o id do banco de dados
        int id = (int) (Math.random() * 1000);



        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        PendingIntent p = PendingIntent.getActivity(context,id, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setTicker("Revisalot");
        builder.setContentTitle("Hora de revisar");
        builder.setContentText(msg);
        builder.setSmallIcon(R.drawable.logo);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.logo));
        builder.setContentIntent(p);

        Notification notification = builder.build();

        notification.vibrate = new long[]{150, 300,150,600};
        notification.flags = Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(id, notification);

        try {
            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(context,som);
            toque.play();

        }catch (Exception e){}

    }
}
