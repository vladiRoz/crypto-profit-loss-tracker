package crypro.profit.loss.tracker.persistance

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Single

/**
 * Created by vladi on 19/1/18.
 */
@Dao
interface AlarmDetailsDAO {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insertAlarm(details : AlarmDetails) : Long

    @Query("SELECT * FROM coins where ticker1 LIKE  :arg0 AND ticker2 LIKE :arg1")
    fun getAlarm(ticker1 : String, ticker2 : String) : Single<Coin>
}