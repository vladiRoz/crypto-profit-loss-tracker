@file:JvmName("ExtensionFunctions")

package crypro.profit.loss.tracker.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by vladi on 22/11/2017.
 */


fun ViewGroup.inflate(resLayout: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(resLayout, this, attachToRoot)
}
