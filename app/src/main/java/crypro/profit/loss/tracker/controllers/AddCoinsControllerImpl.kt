package crypro.profit.loss.tracker.controllers

import crypro.profit.loss.tracker.persistance.CoinsPersistence
import status.portfolio.crypto.vladi.cryptoportfoliostatus.controllers.AddCoinsController

/**
 * Created by vladi on 04/12/2017.
 */
abstract class AddCoinsControllerImpl(protected var persistence: CoinsPersistence?) : AddCoinsController {

    override fun setPersistenceHandler(persistence: CoinsPersistence) {
        this.persistence = persistence
    }

}