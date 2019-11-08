package com.example.notificationt

import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.w(TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                val msg = "${getString(R.string.msg_token_fmt)}: ${token.toString()}"
                Log.d(TAG, msg)
            })
        // Get token for FirebaseToken
        Log.d(TAG, "Firebase Token: ${FirebaseInstanceId.getInstance().getToken()}")

        val bool = "true"
        Toast.makeText(baseContext, "${TAG}: Start  success=${bool}", Toast.LENGTH_LONG).show()
    }

    override fun onNewIntent(intent: Intent?) {
        Log.d(TAG, "onNewIntent.fun")
        super.onNewIntent(intent)
        processIntent(intent)
    }

    private fun processIntent(intent: Intent?) {
        Log.d(TAG, "processIntent.func")
        if (intent != null) {
            var body = intent.getStringExtra(getString(R.string.message_extra))
            if (body.isNullOrEmpty()) {
                // message is null no action
                Log.d(TAG, "Body is Null or Empty()")
            } else {
                // have body to action
                Log.d(TAG, "Body is ${body}")
            }
        }
    }

    fun runtimeEnableAutoInit() {
        // [START fcm_runtime_enable_auto_init]
        FirebaseMessaging.getInstance().isAutoInitEnabled = true
        // [END fcm_runtime_enable_auto_init]
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
