package otus.gpb.homework.activities.sender

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
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
            val intent = createMapsIntent()
            runCatching { startActivity(intent) }
                .getOrElse {
                    handleOpenClientError(it, getString(R.string.no_maps_clients_installed))
                }

        }
        findViewById<Button>(R.id.send_email_button).setOnClickListener {
            val intent = createEmailIntent()
            runCatching { startActivity(intent) }
                .getOrElse {
                    handleOpenClientError(it, getString(R.string.no_email_clients_installed))
                }
        }
        findViewById<Button>(R.id.open_receiver_button).setOnClickListener {

        }

    }

    private fun createMapsIntent(): Intent {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("geo:?q=Рестораны&z=18")
            setPackage("com.google.android.apps.maps")
        }
        return intent
    }

    private fun handleOpenClientError(it: Throwable, viewClientError: String) {
        Log.e("debug_activity", it.message, it)
        if (it is ActivityNotFoundException)
            Toast.makeText(this, viewClientError, Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(this, "Unsupported error.", Toast.LENGTH_SHORT).show()
    }

    private fun createEmailIntent(): Intent {
        return Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf("android@otus.ru"))
            putExtra(Intent.EXTRA_SUBJECT, "Тестовое письмо")
            putExtra(
                Intent.EXTRA_TEXT, """
                        Добрый день!
    
                        Это тестовое письмо.
                    """.trimIndent()
            )
        }
    }
}
