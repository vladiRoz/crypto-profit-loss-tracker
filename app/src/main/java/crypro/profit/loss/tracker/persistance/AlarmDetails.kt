package crypro.profit.loss.tracker.persistance

import android.arch.persistence.room.*
import android.os.Parcel
import android.os.Parcelable


/**
 * Created by vladi on 19/1/18.
 */
@Entity(
        tableName = "alarmdetails",
        foreignKeys = arrayOf(ForeignKey(entity = Coin::class,
                                        parentColumns = arrayOf("uid"),
                                        childColumns = arrayOf("coinUid"),
                                        onDelete = ForeignKey.CASCADE))
)
data class AlarmDetails (
        @PrimaryKey (autoGenerate = true)
        var uid : Long,
        var coinUid : Long,
        var ticker1 : String,
        var ticker2 : String,
        var triggerValue : Double,
        @TypeConverters(ConditionConverter::class)
        var condition : Condition

)  : Parcelable {


    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readLong(),
            parcel.readString(),
            parcel.readString(),
            parcel.readDouble(),
            AlarmDetails.Condition.valueOf(parcel.readString()))

    constructor(ticker1: String, ticker2: String, triggerValue: Double, condition: Condition) : this(0L, 0L, ticker1, ticker2, triggerValue, condition)

    enum class Condition (var value: Int) {
        GreaterThanOrEqualTo(0),
        LessThanOrEqualTo(1)
    }

    class ConditionConverter {

        @TypeConverter
        fun toCondition(status: Int): Condition {
            return if (status == Condition.GreaterThanOrEqualTo.value) {
                Condition.GreaterThanOrEqualTo
            } else if (status == Condition.LessThanOrEqualTo.value) {
                Condition.LessThanOrEqualTo
            } else {
                throw IllegalArgumentException("Could not recognize status")
            }
        }

        @TypeConverter
        fun toInteger(status: Condition): Int? {
            return status.value
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(uid)
        parcel.writeLong(coinUid)
        parcel.writeString(ticker1)
        parcel.writeString(ticker2)
        parcel.writeDouble(triggerValue)
        parcel.writeString(condition.name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AlarmDetails> {
        override fun createFromParcel(parcel: Parcel): AlarmDetails {
            return AlarmDetails(parcel)
        }

        override fun newArray(size: Int): Array<AlarmDetails?> {
            return arrayOfNulls(size)
        }
    }


}