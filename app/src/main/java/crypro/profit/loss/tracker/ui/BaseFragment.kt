package crypro.profit.loss.tracker.ui

import android.content.Context
import android.support.v4.app.Fragment

/**
 * Created by vladi on 14/1/18.
 */
open class BaseFragment<T : FragmentUICallback> : Fragment() {

    protected var activityCallback : T? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        activityCallback = (context) as T

    }
}