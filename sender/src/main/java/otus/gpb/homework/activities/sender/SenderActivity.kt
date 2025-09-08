package otus.gpb.homework.activities.sender

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import otus.gpb.homework.activities.receiver.R

class SenderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sender)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.to_google_maps_button).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:?q=Рестораны&z=18"))
                .setPackage("com.google.android.apps.maps")
            runCatching {
                startActivity(intent)
            }.getOrElse {
                Log.e("debug_activity", it.message, it)
            }

        }
        findViewById<Button>(R.id.send_email_button).setOnClickListener {

        }
        findViewById<Button>(R.id.open_receiver_button).setOnClickListener {

        }

    }
}
