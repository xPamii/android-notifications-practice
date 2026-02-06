package com.xpamii.notification.activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;

import androidx.core.app.RemoteInput;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.xpamii.notification.R;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "my_channel_01";

    private static final String KEY_TEXT_REPLY = "key_text_reply";

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> { // Request permission launcher.

                if (isGranted) {
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }

            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        //Check if notification permission is granted or not.
        //If not, request permission.
        //If yes, create notification channel.
        createNotificationChannel();
        checkAndRequestNotificationPermission();

        Button notificationButton = findViewById(R.id.notification_button);
        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNotification();
            }

        });

    }

    // Notification with action.
    private void sendNotification() {

        Intent intent = new Intent(this, NotificationActivity.class); //
        int requestCode = 0;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, // Create pending intent.
                requestCode,
                intent,
                PendingIntent.FLAG_MUTABLE);

        RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)// Create remote input. (androidx.core.app.RemoteInput)
                .setLabel("Type your message here") // Set label for remote input.
                .build();

        NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(R.drawable.ic_launcher_foreground, // Set icon for action.
                "Reply", // Set title for action.
                pendingIntent).addRemoteInput(remoteInput).build(); // Create action for notification.

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentTitle("Congratulations! Message from BMW ")
                .setContentText("You have won new BMW M3 Competition. Please click here to claim your prize. ")
                .addAction(replyAction)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            int notificationId = 1;
            managerCompat.notify(notificationId, builder.build());
        } else {
            checkAndRequestNotificationPermission();
        }
    }


    // Normal test notification.  Do not use this in production.
//    private void sendNotification() {
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setSmallIcon(R.drawable.ic_launcher_foreground)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setContentTitle("Notification Title")
//                .setContentText("Notification Body")
//                .setAutoCancel(true);
//
//        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);//
//
//        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
//            int notificationId = 1;
//            managerCompat.notify(notificationId, builder.build());
//        } else {
//            checkAndRequestNotificationPermission();
//        }
//    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // Our android OS version >= Android Version 8 (Oreo).
            CharSequence name = " General Channel"; // The user-visible name of the channel.
            String description = "Channel for general notifications"; // The user-visible description of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance); // Create notification channel.
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);// Get notification manager.
            notificationManager.createNotificationChannel(channel); // Create notification channel.

        }

    }

    private void checkAndRequestNotificationPermission() { // Check if permission is granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {// Our android OS version >= Android Version 13 (TIRAMISU).
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {// If permission is not granted.
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);// Request permission.
            }
        }
    }


//    private static final int NOTIFICATION_NUMBER = 1;
//
//    private void popupNotification() {
//        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        String channelId = "channel_id";
//        NotificationChannel testNotification = new NotificationChannel( channelId,
//                "Test Notification",
//                NotificationManager.IMPORTANCE_DEFAULT);
//        manager.createNotificationChannel(testNotification);
//
//        Notification notification = new NotificationCompat.Builder(this, channelId)
//                .setSmallIcon(R.drawable.ic_launcher_foreground)
//                .setContentTitle("Notification Practical")
//                .setContentText("This is my notification manager")
//                .setAutoCancel(true)
//                .build();
//
//        manager.notify(NOTIFICATION_NUMBER, notification);
//
//    }

}


