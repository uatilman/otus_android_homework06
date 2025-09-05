package otus.gpb.homework.activities

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ActivityA : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_a)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        findViewById<Button>(R.id.button_open_activity_b_from_a).setOnClickListener {
            val intentActivityB = Intent(this, ActivityB::class.java)
            intentActivityB.addFlags(FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intentActivityB)
        }
        Log.i("debug_activity", "${logInfo()}: onCreate")
    }

    override fun onResume() {
        super.onResume()
        Log.i("debug_activity", "${logInfo()}: onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i("debug_activity", "${logInfo()}: onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i("debug_activity", "${logInfo()}: onStop")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i("debug_activity", "${logInfo()}: onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("debug_activity", "${logInfo()}: onDestroy")
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.i("debug_activity", "${logInfo()}: onNewIntent")
    }

}
