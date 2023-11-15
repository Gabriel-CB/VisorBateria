package com.example.permutabateria
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_BATTERY_CHANGED
import android.content.Intent.ACTION_BOOT_COMPLETED
import android.content.IntentFilter
import android.graphics.Color
import android.os.BatteryManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.example.permutabateria.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding


    private val handler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {

            val tv: TextView = binding.nivelBateria;

            if(msg.obj.toString().toFloat() > 70.0){
                tv.setTextColor(Color.GREEN)
            }else if(msg.obj.toString().toFloat() < 15.0){
                tv.setTextColor(Color.RED)
            }else{
                tv.setTextColor(Color.BLACK)
            }

            tv.setText("O nível da bateria é: " + msg.obj)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val intentFilter: IntentFilter = IntentFilter(ACTION_BATTERY_CHANGED);
        intentFilter.addAction(ACTION_BOOT_COMPLETED);

        val receiver: BroadcastReceiver = object : BroadcastReceiver() {

            override fun onReceive(context: Context, intent: Intent) {
                val level: Int = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                val scale: Int = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                var nivel = level * 100 / scale.toFloat();
                val message = Message()

                message.obj = nivel;

                handler.sendMessage(message);
            }
        }

        registerReceiver(receiver, intentFilter)
    }
}