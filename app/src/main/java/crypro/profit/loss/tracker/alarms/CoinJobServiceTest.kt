package crypro.profit.loss.tracker.alarms

import crypro.profit.loss.tracker.CoinsStatusApplication


/**
 * Created by vladi on 23/1/18.
 */
class CoinJobServiceTest : CoinJobService(CoinsStatusApplication.DefaultManager.instance)