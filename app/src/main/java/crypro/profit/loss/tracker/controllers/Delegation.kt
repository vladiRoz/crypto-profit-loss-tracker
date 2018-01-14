package crypro.profit.loss.tracker.controllers

/**
 * Created by vladi on 16/12/17.
 */
interface Delegation {
        fun <T>delegate(actionName: String, extraObject: T)
}