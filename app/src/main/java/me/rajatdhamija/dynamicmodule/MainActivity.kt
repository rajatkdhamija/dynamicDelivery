package me.rajatdhamija.dynamicmodule

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus

class MainActivity : AppCompatActivity() {

    private val manager: SplitInstallManager by lazy {
        SplitInstallManagerFactory.create(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnDownloadMyModule: AppCompatButton = findViewById(R.id.btnDownloadMyModule)
        val btnOpenMuModule: AppCompatButton = findViewById(R.id.btnOpenMyModule)
        val isEnabled = manager.installedModules.contains("myModule")
        btnOpenMuModule.isEnabled = isEnabled
        btnDownloadMyModule.setOnClickListener {
            val request = SplitInstallRequest.newBuilder()
                    .addModule("myModule")
                    .build()

            manager.startInstall(request)

            manager.registerListener {
                when (it.status()) {
                    SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
                        Log.e("USER_CONFIRMATION","Downloading on demand module: state REQUIRES USER CONFIRMATION")
                    }
                    SplitInstallSessionStatus.UNKNOWN -> {
                        Log.e("UNKNOWN","Downloading on demand module: state UNKNOWN")
                    }
                    SplitInstallSessionStatus.FAILED -> Log.e("Status Failed", it.errorCode().toString())
                    SplitInstallSessionStatus.DOWNLOADING -> btnDownloadMyModule.showSnackbar("Downloading feature")
                    SplitInstallSessionStatus.INSTALLED -> {
                        btnDownloadMyModule.showSnackbar("Feature ready to be used")
                        val isEnabled = manager.installedModules.contains("myModule")
                        btnOpenMuModule.isEnabled = isEnabled
                    }
                    else -> { /* Do nothing in this example */ }
                }
            }
        }

        btnOpenMuModule.setOnClickListener {
            val intent = Intent()
            intent.setClassName(BuildConfig.APPLICATION_ID, "me.rajatdhamija.mymodule.DynamicModuleWelcomeActivity")
            startActivity(intent)
        }
    }

    private fun View.showSnackbar(message: String) {
        Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
    }
}