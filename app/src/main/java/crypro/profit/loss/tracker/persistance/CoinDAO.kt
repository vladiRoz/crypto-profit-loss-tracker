package crypro.profit.loss.tracker.persistance

import android.arch.persistence.room.*
import io.reactivex.Single


/**
 * Created by vladi on 04/12/2017.
 */
@Dao
interface CoinDAO {

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insertCoin(coin : Coin) : Long

    @Query("SELECT * FROM coins where ticker1 LIKE  :arg0 AND ticker2 LIKE :arg1")
    fun findCoin(ticker1 : String, ticker2 : String) : Single<Coin>

    @Update (onConflict = OnConflictStrategy.REPLACE)
    fun updateCoin(coin : Coin)

    @Delete
    fun deleteCoin(coin : List<Coin>)

    @Query("SELECT * FROM coins")
    fun getAllCoins() : Single<List<Coin>>

}
