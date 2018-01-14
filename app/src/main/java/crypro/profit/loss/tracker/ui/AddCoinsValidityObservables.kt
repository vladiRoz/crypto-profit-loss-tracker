package crypro.profit.loss.tracker.ui

import android.support.annotation.NonNull
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import crypro.profit.loss.tracker.api.Completion
import java.lang.Double.parseDouble
import java.util.concurrent.TimeUnit


/**
 * Created by vladi on 06/12/2017.
 */
class AddCoinsRXValidator {

    companion object {

        fun validateFields(ticker1: EditText?, ticker2: EditText?, avgPos: EditText?, listener : Completion<Boolean>) {
            if (ticker1 == null || ticker2 == null || avgPos == null) return

            var ticker1Obs = createDebounceValidator(createEditTextObservable(ticker1), { validateTicker(ticker1.text.toString()) })
            var ticker2Obs = createDebounceValidator(createEditTextObservable(ticker2), { validateTicker(ticker2.text.toString()) })
            var avgPosObs = createDebounceValidator(createEditTextObservable(avgPos),   { validateAvgPosition(avgPos.text.toString()) })

            Observable.combineLatest(ticker1Obs, ticker2Obs, avgPosObs,
                    Function3<Boolean, Boolean, Boolean, Boolean> {
                        t1valid, t2valid, t3valid ->
                        t1valid && t2valid && t3valid })
                    .subscribe(
                            { enabled -> listener.onResponse(enabled) })
        }

        private fun createDebounceValidator(observable: Observable<String>, validateFunc: (String) -> Boolean): Observable<Boolean> {
            return observable.
                    debounce(1, TimeUnit.SECONDS).
                    subscribeOn(Schedulers.io()).
                    observeOn(AndroidSchedulers.mainThread()).
                    map({ str -> validateFunc(str) })
        }

        private fun validateTicker(ticker: String): Boolean {
            return !ticker.isEmpty() && ticker.length <= 5
        }

        private fun validateAvgPosition(position: String): Boolean {
            try {
                parseDouble(position)
                return true
            } catch (e: NumberFormatException) {
                return false
            }
        }

        private fun createEditTextObservable(@NonNull editText: EditText): Observable<String> {

            val subject = PublishSubject.create<String>()

            editText.addTextChangedListener(object : TextWatcher {

                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable) {
                    subject.onNext(s.toString())
                }
            })

            return subject
        }
    }
}











