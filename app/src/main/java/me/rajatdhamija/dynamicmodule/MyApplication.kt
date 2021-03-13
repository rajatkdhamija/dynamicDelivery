package me.rajatdhamija.dynamicmodule

import android.content.Context
import com.google.android.play.core.splitcompat.SplitCompat
import com.google.android.play.core.splitcompat.SplitCompatApplication

class MyApplication : SplitCompatApplication() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        SplitCompat.install(this)
    }
}