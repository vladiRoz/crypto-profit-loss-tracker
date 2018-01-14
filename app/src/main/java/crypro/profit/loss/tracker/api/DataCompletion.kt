package crypro.profit.loss.tracker.api

/**
 * Created by vladi on 28/11/2017.
 */
interface DataCompletion<T> : Completion<T> {
    fun onError(message : String)
}