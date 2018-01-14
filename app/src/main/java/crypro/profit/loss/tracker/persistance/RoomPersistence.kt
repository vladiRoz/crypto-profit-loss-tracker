package crypro.profit.loss.tracker.persistance

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import android.util.Log
import crypro.profit.loss.tracker.CoinsStatusApplication
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import crypro.profit.loss.tracker.api.Completion

/**
 * Created by vladi on 04/12/2017.
 */
class RoomPersistence(context: Context = CoinsStatusApplication.getApplicationContext()) : CoinsPersistence {

    val DB_NAME = "coins"

    @Database(entities = arrayOf(Coin::class), version = 1, exportSchema = false)
    abstract class DB : RoomDatabase() {
        abstract fun coinDao(): CoinDAO
    }

    companion object {
        var database: DB? = null
    }

    init {
        database = Room.databaseBuilder(context, DB::class.java, DB_NAME).build()
    }

    override fun insertCoin(coin: Coin, listener: Completion<Long>){
//        runSingleTask({database?.coinDao()?.insertCoin(coin)})

        var id = 0L
        Single.fromCallable {
            id = database?.coinDao()?.insertCoin(coin)!!
            Log.i(javaClass.simpleName, "insert id: " + id + " coin: " + coin.ticker1)
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).
                subscribe({ _ ->
                    // TODO remove after getMarkets api call
                    coin.uid = id
                    listener.onResponse(id)
                })
    }

    override fun updateCoin(coin: Coin) {
//        runSingleTask({database?.coinDao()?.updateCoin(coin)})

        Single.fromCallable { database?.coinDao()?.updateCoin(coin) }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).
                subscribe({ _, error -> Log.i(javaClass.simpleName, "update , error: " + error?.message) })
    }

    override fun delete(coins: List<Coin>) {
//        runSingleTask({database?.coinDao()?.deleteCoin(coin)})

        Single.fromCallable {
            Log.i(javaClass.simpleName, "deleteCoin coin id: " + coins.get(0).uid + " coin: " + coins.get(0).ticker1)
            database?.coinDao()?.deleteCoin(coins)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).
                subscribe({ x, error -> Log.i(javaClass.simpleName, "deleteCoin , error: " + error?.message + " x: " + x) })
    }

    override fun getAllCoins(listener: Completion<List<Coin>>) {
        database?.coinDao()?.getAllCoins()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { coins ->
                    listener.onResponse(coins)
                }
    }

    // why not to utilize rx power
//    override fun getAllCoinsNotifier(listener: Completion<List<Coin>>) {
//        database?.coinDao()?.getAllCoinsNotifier()
//                ?.subscribeOn(Schedulers.io())
//                ?.observeOn(AndroidSchedulers.mainThread())
//                ?.subscribe({ coins ->
//                    listener.onResponse(coins)
//                }, { throwable -> Log.i("vlsi", "valdi") })
//    }

    override fun findCoin(ticker1: String, ticker2: String, listener: Completion<Coin?>) {
        database?.coinDao()?.findCoin(ticker1, ticker2)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({ coin, error ->
                    listener.onResponse(coin)
                })
    }

    // figure out
    fun runSingleTask(task: (Unit) -> Unit) {
        Single.fromCallable { task }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).
                subscribe({ _, error ->
                    Log.i(javaClass.simpleName, task.javaClass.name + " , error: " + error?.message)
                })
    }


}