package crypro.profit.loss.tracker.managers

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import crypro.profit.loss.tracker.api.BittrexApi
import crypro.profit.loss.tracker.api.CoinResponse
import crypro.profit.loss.tracker.api.CryptoExchangeApi
import crypro.profit.loss.tracker.api.DataCompletion

/**
 * Created by vladi on 23/11/2017.
 */
class CoinRequestManager(private val exchangeApi: CryptoExchangeApi = BittrexApi()) : CoinsApiManager {

    val BE_ERROR: String = "be_error"

    var subscription = CompositeDisposable()

    override fun destroy() {
        if (!subscription.isDisposed) {
            subscription.dispose()
        }
        subscription.clear()
    }

    override fun init() {
        subscription = CompositeDisposable()
    }


    override fun getCoinStats(market: String, completion: DataCompletion<CoinResponse?>) {

        val observable = Observable.create<CoinResponse?> {

            subscriber ->

            var coinData: CoinResponse?
            val response = exchangeApi.getLastPrice(market).execute()
            if (response.isSuccessful) {
                coinData = CoinResponse(market, response.body())
            } else {
                coinData = null
            }

            // no need to update if there is some error
            coinData?.let { subscriber.onNext(it) }
            subscriber.onComplete()
        }

        val coinSubscription = observable.
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(
                        { coins ->
                            completion.onResponse(coins)
                        },
                        { _ ->
                            completion.onError(BE_ERROR)
                        })

        subscription.add(coinSubscription)
    }

    override fun checkValidResponse(message: String?) : Boolean{
        return exchangeApi.isValidResponse(message)
    }


}



