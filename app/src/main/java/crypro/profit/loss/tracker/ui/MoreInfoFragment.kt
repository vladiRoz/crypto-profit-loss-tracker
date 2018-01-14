package crypro.profit.loss.tracker.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import crypro.profit.loss.tracker.R
import crypro.profit.loss.tracker.utils.inflate

/**
 * Created by vladi on 18/12/17.
 */
class MoreInfoFragment : Fragment() {

    companion object {
        fun newInstance() : MoreInfoFragment {
            return MoreInfoFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.more_info_fragment)
    }
}