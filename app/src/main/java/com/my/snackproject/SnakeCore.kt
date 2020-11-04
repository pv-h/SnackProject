package com.my.snackproject

const val START_GAME_SPEED = 600L
const val MIN_GAME_SPEED = 200L

object SnakeCore {

    var nextMove: () -> Unit = {}
    var isPlay = true
    private var thread: Thread
    var gameSpeed = START_GAME_SPEED
init {
    thread = Thread {
        while (true) {
            Thread.sleep(gameSpeed)
            if (isPlay) {
                nextMove()
            }
        }
    }
    thread.start()
}


    fun startTheGame() {
        gameSpeed = START_GAME_SPEED
        isPlay = true
    }
}
