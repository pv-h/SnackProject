package com.my.snackproject

object SnakeCore {

    var nextMove: () -> Unit = {}
    var isPlay = true
    private var thread: Thread
init {
    thread = Thread {
        while (true) {
            Thread.sleep(600)
            if (isPlay) {
                nextMove()
            }
        }
    }
    thread.start()
}


    fun startTheGame() {
    isPlay = true
    }
}
