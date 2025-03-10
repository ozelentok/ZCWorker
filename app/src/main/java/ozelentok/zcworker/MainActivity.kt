package ozelentok.zcworker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.PowerManager
import android.os.PowerManager.WakeLock
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {
    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private lateinit var workerThread: Thread
    private lateinit var wakeLock: WakeLock

    private val wakeLockTimeoutMs: Long = 1000 * 60 * 60 * 24
    private val manageStorageRequestCode = 2296

    init {
        System.loadLibrary("zcjni")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startButton = findViewById(R.id.start_worker_button)
        stopButton = findViewById(R.id.stop_worker_button)
        requestExternalStoragePermission()
        setupWakeLock()
    }

    private fun requestExternalStoragePermission() {
        if (Environment.isExternalStorageManager()) {
            return
        }
        try {
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            intent.addCategory("android.intent.category.DEFAULT")
            intent.data = Uri.parse(String.format("package:%s", applicationContext.packageName))
            startActivityForResult(intent, manageStorageRequestCode)
        } catch (e: java.lang.Exception) {
            Log.e("ZCWorker", "Failed to open file management permissions", e)
            val intent = Intent()
            intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
            startActivityForResult(intent, manageStorageRequestCode)
        }
    }

    private fun setupWakeLock() {
        val powerManager = getSystemService(POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "SFTPServer::WakeLock")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)
        if (resultCode != RESULT_OK) {
            return
        }
        if (requestCode == manageStorageRequestCode) {
            if (Environment.isExternalStorageManager()) {
                Utils.showToast(this, "Successfully gained file management permissions")
            } else {
                Utils.showErrorToast(this, "Unable to gain file management permissions")
            }
        }
    }

    private fun enableWakeLock() {
        wakeLock.acquire(wakeLockTimeoutMs)
    }

    private fun disableWakeLock() {
        wakeLock.release()
    }

    fun onStartWorker(view: View) {
        startButton.isEnabled = false
        workerThread = thread {
            try {
                connectWorker(
                    AppPreferences.getHost(this@MainActivity),
                    AppPreferences.getPort(this@MainActivity)
                )
            } catch (e: java.lang.Exception) {
                runOnUiThread {
                    Utils.showErrorToast(this@MainActivity, e.message)
                    startButton.isEnabled = true
                }
            }
            runOnUiThread {
                enableWakeLock()
                startService(Intent(this@MainActivity, KeepAliveService::class.java))
                stopButton.isEnabled = true
            }
            waitOnWorker()
            runOnUiThread {
                disableWakeLock()
                stopService(Intent(this@MainActivity, KeepAliveService::class.java))
                startButton.isEnabled = true
                stopButton.isEnabled = false
            }
        }
    }

    fun onStopWorker(view: View) {
        stopButton.isEnabled = false
        thread {
            try {
                stopWorker()
                workerThread.join()
            } catch (e: java.lang.Exception) {
                runOnUiThread {
                    Utils.showErrorToast(this@MainActivity, e.message)
                }
                stopButton.isEnabled = true
            }
        }
    }

    fun onOpenSettings(view: View) {
        val settingsIntent = Intent(this, SettingsActivity::class.java)
        startActivity(settingsIntent)
    }

    fun onShowAbout(view: View?) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        try {
            val pInfo = packageManager.getPackageInfo(packageName, 0)
            builder.setMessage("Made By Oz Elentok\nVersion: ${pInfo.versionName}")
        } catch (e: Exception) {
            return
        }
        builder.show()
    }

    external fun connectWorker(host: String?, port: Int)
    external fun waitOnWorker()
    external fun stopWorker()
}