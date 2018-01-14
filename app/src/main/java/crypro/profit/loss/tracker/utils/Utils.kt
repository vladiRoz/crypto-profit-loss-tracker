package crypro.profit.loss.tracker.utils

import android.app.Activity
import android.graphics.Point
import crypro.profit.loss.tracker.persistance.Coin
import java.text.DecimalFormat

/**
 * Created by vladi on 30/11/2017.
 */
class Utils {

    companion object {

        fun getScreenHeight(activity: Activity): Int {
            return getScreenDimentions(activity).y
        }

        fun getScreenWidth(activity: Activity): Int {
            return getScreenDimentions(activity).x
        }

        fun getScreenDimentions(context: Activity): Point {
            val display = context.getWindowManager().getDefaultDisplay()
            val size = Point()
            display.getSize(size)
            return size;
        }

        fun calcPL(lastPrice : Double, avgPrice : Double) : Double {
            return ((lastPrice - avgPrice) / avgPrice) * 100
        }

        fun getReadableFormat(num : Double, beforeDotsFormat : String, afterDotsFormat : String) : String {
            return DecimalFormat(beforeDotsFormat + '.' + afterDotsFormat).format(num)
        }

        fun getMarketName(ticker1: String, ticker2: String): String {
            return "$ticker2-$ticker1"
        }

        fun getMarketName(coin : Coin): String {
            return getMarketName(coin.ticker1, coin.ticker2)
        }
    }
}