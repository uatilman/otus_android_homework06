package otus.gpb.homework.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ActivityC : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_c)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        findViewById<Button>(R.id.button_open_activity_a).setOnClickListener {

        }
        findViewById<Button>(R.id.button_open_activity_b_from_c).setOnClickListener {

        }
        findViewById<Button>(R.id.button_close_activity_c).setOnClickListener {

        }
        findViewById<Button>(R.id.button_close_stack).setOnClickListener {

        }

        Log.i("debug_activity", "${this::class.simpleName} ${hashCode().toString(16)}: onCreate")
    }

    override fun onResume() {
        super.onResume()
        Log.i("debug_activity", "${this::class.simpleName} ${hashCode().toString(16)}: onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i("debug_activity", "${this::class.simpleName} ${hashCode().toString(16)}: onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i("debug_activity", "${this::class.simpleName} ${hashCode().toString(16)}: onStop")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i("debug_activity", "${this::class.simpleName} ${hashCode().toString(16)}: onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("debug_activity", "${this::class.simpleName} ${hashCode().toString(16)}: onDestroy")
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.i("debug_activity", "${this::class.simpleName} ${hashCode().toString(16)}: onNewIntent")
    }
}