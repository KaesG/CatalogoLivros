package com.example.catalogolivros

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

class LembreteReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificacao = NotificationCompat.Builder(context, "lembrete_leitura")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Hora da leitura!")
            .setContentText("Lembre-se de continuar sua leitura.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        val manager = NotificationManagerCompat.from(context)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                manager.notify(1, notificacao)
            }
        } else {
            manager.notify(1, notificacao)
        }
    }
}
