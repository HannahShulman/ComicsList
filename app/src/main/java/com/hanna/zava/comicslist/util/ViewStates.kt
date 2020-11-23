package com.hanna.zava.comicslist.util

import android.view.View
import com.airbnb.lottie.LottieAnimationView
import com.hanna.zava.comicslist.R

class ViewStates(private val view: View) {

    private var currentState = State.UNDEFINED

    fun setState(state: State) {
        if (currentState == state) return

        currentState = state
        State.values().filterNot { it == state }.forEach {
            view.findViewById<View>(it.viewId)?.visibility = View.INVISIBLE
        }

        setLoadingIndicator(state == State.LOADING)
        view.findViewById<View>(state.viewId)?.visibility = View.VISIBLE
    }

    private fun setLoadingIndicator(isEnabled: Boolean) {
        val lottieView = view.findViewById<LottieAnimationView>(lottieLoadingIndicatorId)
        if (isEnabled) {
            lottieView?.playAnimation()
            lottieView?.visibility = View.VISIBLE
        } else {
            lottieView?.cancelAnimation()
            lottieView?.visibility = View.GONE
        }
    }

    enum class State(val viewId: Int) {
        LOADING(R.id.loading_view),
        MAIN(R.id.main_view),
        ERROR(0),
        UNDEFINED(0)
    }

    companion object {
        const val lottieLoadingIndicatorId = R.id.lottie_view
    }
}