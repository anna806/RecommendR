package hu.recommendr.service

import okhttp3.Call
import okhttp3.EventListener

class GPTEventListener : EventListener() {
    var done = false

    override fun callStart(call: Call) {
        done = false
    }

    override fun callEnd(call: Call) {
        done = true
    }

}