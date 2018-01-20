package crypro.profit.loss.tracker.utils

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by vladi on 20/1/18.
 */
class ParcelableUtil {

    companion object {
        fun marshall(parcelable: Parcelable): ByteArray {
            val parcel = Parcel.obtain()
            parcelable.writeToParcel(parcel, 0)
            val bytes = parcel.marshall()
            parcel.recycle()
            return bytes
        }

        fun unmarshal(bytes: ByteArray): Parcel {
            val parcel = Parcel.obtain()
            parcel.unmarshall(bytes, 0, bytes.size)
            parcel.setDataPosition(0)
            return parcel
        }

        fun <T> unmarshal(bytes: ByteArray, creator: Parcelable.Creator<T>): T {
            val parcel = unmarshal(bytes)
            val result = creator.createFromParcel(parcel)
            parcel.recycle()
            return result
        }
    }
}