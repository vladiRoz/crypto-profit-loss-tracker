package crypro.profit.loss.tracker

import android.app.Application
import android.content.Context

/**
 * Created by vladi on 16/12/17.
 */
class CoinsStatusApplication : Application() {

    companion object {
        private var instance : CoinsStatusApplication? = null

        fun getApplicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    init {
        instance = this
    }

}