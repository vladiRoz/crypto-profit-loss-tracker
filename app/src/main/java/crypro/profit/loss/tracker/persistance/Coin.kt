package crypro.profit.loss.tracker.persistance

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable

/**
 * Created by vladi on 03/12/2017.
 */
@Entity(tableName = "coins")
data class Coin (
        @PrimaryKey (autoGenerate = true)
        var uid : Long,
        @ColumnInfo(name = "ticker1")
        var ticker1 : String = "",
        @ColumnInfo(name = "ticker2")
        var ticker2: String = "",
        @ColumnInfo(name = "avgPosition")
        var avgPosition : Double,
        @ColumnInfo(name = "lastPrice")
        var lastPrice : Double = -1.0

) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString(),
            parcel.readString(),
            parcel.readDouble(),
            parcel.readDouble()) {
    }

    constructor() : this(0, "", "", 0.0)

        override fun equals(other: Any?): Boolean {
                val otherCoin : Coin = other as Coin
                return otherCoin.ticker1.equals(this.ticker1) && otherCoin.ticker2.equals(this.ticker2)
        }

        constructor(coin: Coin) : this(coin.uid, coin.ticker1, coin.ticker2, coin.avgPosition, coin.lastPrice)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(uid)
        parcel.writeString(ticker1)
        parcel.writeString(ticker2)
        parcel.writeDouble(avgPosition)
        parcel.writeDouble(lastPrice)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Coin> {

        val NO_ID : Long = 0

        override fun createFromParcel(parcel: Parcel): Coin {
            return Coin(parcel)
        }

        override fun newArray(size: Int): Array<Coin?> {
            return arrayOfNulls(size)
        }
    }

}