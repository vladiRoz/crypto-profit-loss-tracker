package crypro.profit.loss.tracker.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.add_new_coins_dialog_layout.*
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentActivity
import android.view.WindowManager
import crypro.profit.loss.tracker.R
import kotlinx.android.synthetic.main.add_coins_item_layout.*
import crypro.profit.loss.tracker.api.Completion
import status.portfolio.crypto.vladi.cryptoportfoliostatus.controllers.AddCoinsController
import crypro.profit.loss.tracker.persistance.Coin
import crypro.profit.loss.tracker.utils.Utils
import java.lang.Double.parseDouble


/**
 * Created by vladi on 30/11/2017.
 */
class AddNewCoinsDialog : DialogFragment(), View.OnClickListener {

    private var controller: AddCoinsController? = null
    private var isValidFields: Boolean = false

    companion object {
        fun newInstance(): AddNewCoinsDialog {
            return AddNewCoinsDialog()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.add_new_coins_dialog_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cancel_text_view.setOnClickListener(this)
        done_text_view.setOnClickListener(this)

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

        initRXValidation()
    }

    override fun onResume() {
        val params = dialog.window!!.attributes
        params.width = Utils.getScreenWidth(activity as FragmentActivity) - resources.getDimension(R.dimen.add_coins_dialog_width_space).toInt()

        params.height = resources.getDimension(R.dimen.add_coins_dialog_height).toInt()
        dialog.window!!.attributes = params as android.view.WindowManager.LayoutParams

        super.onResume()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            done_text_view.id -> onDoneClicked()
            cancel_text_view.id -> onCancelClicked()
        }
    }

    private fun onCancelClicked() {
        dismiss()
    }

    private fun onDoneClicked() {

        if (isValidFields) {

            val ticker1 = first_ticker_edit_text?.text.toString().toLowerCase()
            val ticker2 = second_ticker_edit_text?.text.toString().toLowerCase()
            val avgPosition = parseDouble(position_avg_edit_text.text.toString())

            // todo remove after Bittrex api getMarkets implementation
            controller?.findCoin(ticker1, ticker2, object : Completion<Coin?> {

                override fun onResponse(coin: Coin?) {
                    if (coin == null) {
                        val bornCoin = Coin(Coin.NO_ID, ticker1, ticker2, avgPosition)
                        controller?.insertCoin(bornCoin, object : Completion<Long> {

                            override fun onResponse(value: Long) {
                                controller?.notifyNewCoinAdded(bornCoin)
                            }
                        })

                    } else {
                        coin.avgPosition = avgPosition
                        controller?.updateCoin(coin)
                        controller?.notifyCoinUpdated(coin)
                    }
                }
            })

            dismiss()
        } else {
            // show red fields not valid
        }
    }

    fun setController(controller: AddCoinsController) {
        this.controller = controller
    }

    fun initRXValidation() {
        AddCoinsRXValidator.validateFields(first_ticker_edit_text,
                second_ticker_edit_text,
                position_avg_edit_text,
                object : Completion<Boolean> {
                    override fun onResponse(isValid: Boolean) {
                        isValidFields = isValid
                    }
                })
    }


}