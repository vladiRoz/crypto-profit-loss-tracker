package crypro.profit.loss.tracker.api

/**
 * Created by vladi on 07/12/2017.
 */
interface Completion<T> {
    fun onResponse(value : T)
}