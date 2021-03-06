   package com.my.snackproject

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.my.snackproject.SnakeCore.gameSpeed
//import androidx.core.content.ContextCompat
import com.my.snackproject.SnakeCore.isPlay
import com.my.snackproject.SnakeCore.startTheGame
import com.my.snackproject.SnakeCore.nextMove
import kotlinx.android.synthetic.main.activity_main.*


const val HEAD_SIZE = 100
const val CELLS_OF_FIELD = 10


class MainActivity : AppCompatActivity() {
    private val allTale = mutableListOf<PartOfTale>()
    private val human by lazy {
        ImageView(this)
            .apply {
                this.layoutParams = FrameLayout.LayoutParams(HEAD_SIZE, HEAD_SIZE)
                this.setImageResource(R.drawable.ic_person)
            }
    }
    private val head by lazy {
        ImageView(this)
            .apply {
                this.layoutParams = FrameLayout.LayoutParams(HEAD_SIZE, HEAD_SIZE)
                this.setImageResource(R.drawable.snake_head)
            }
    }
    private var currentDirections = Directions.BOTTOM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startTheGame()
        generateNewHuman()
        nextMove = { move(Directions.BOTTOM) }

        ivArrowUp.setOnClickListener { nextMove = { checkIfCurrentDirectionIsNotOpposite(Directions.UP, Directions.BOTTOM) } }
        ivArrowBottom.setOnClickListener { nextMove = { checkIfCurrentDirectionIsNotOpposite(Directions.BOTTOM, Directions.UP) } }
        ivArrowLeft.setOnClickListener { nextMove = { checkIfCurrentDirectionIsNotOpposite(Directions.LEFT, Directions.RIGHT) } }
        ivArrowRight.setOnClickListener { nextMove = { checkIfCurrentDirectionIsNotOpposite(Directions.RIGHT, Directions.LEFT) } }
        ivPause.setOnClickListener {
            if (isPlay) ivPause.setImageResource(R.drawable.ic_play)
            else ivPause.setImageResource(R.drawable.ic_pause)
            isPlay = !isPlay
        }
    }
    private fun checkIfCurrentDirectionIsNotOpposite(properDirections: Directions, oppositeDirections: Directions){
        if (currentDirections == oppositeDirections) move(currentDirections)
        else move(properDirections)
    }

    private fun generateNewHuman() {
        val viewCoordinate = generateHumanCoordinates()
         (human.layoutParams as FrameLayout.LayoutParams).topMargin = viewCoordinate.top
         (human.layoutParams as FrameLayout.LayoutParams).leftMargin = viewCoordinate.left
         container.removeView(human)
         container.addView(human)
    }

    private fun generateHumanCoordinates(): ViewCoordinate {
        val viewCoordinate = ViewCoordinate(
            (0 until CELLS_OF_FIELD).random() * HEAD_SIZE,
            (0 until CELLS_OF_FIELD).random() * HEAD_SIZE
        )
        for (partTale in allTale){
            if (partTale.viewCoordinate == viewCoordinate) return generateHumanCoordinates()
            }
        if (head.top == viewCoordinate.top && head.left == viewCoordinate.left) return generateHumanCoordinates()
        return viewCoordinate
    }

    private fun addPartOfTale (top:Int, left:Int){
        val talePart = drawPartOfTale(top,left)
        allTale.add(PartOfTale(ViewCoordinate(top, left), talePart))
    }

    private fun drawPartOfTale(top: Int, left: Int): ImageView {
        val taleImage = ImageView(this)
        taleImage.setImageResource(R.drawable.snake_scales)
        taleImage.layoutParams = FrameLayout.LayoutParams(HEAD_SIZE, HEAD_SIZE)
        (taleImage.layoutParams as FrameLayout.LayoutParams).topMargin = top
        (taleImage.layoutParams as FrameLayout.LayoutParams).leftMargin = left

        container.addView(taleImage)
        return taleImage
    }

    private fun checkIfSnakeEatsPerson() {
        if (head.left==human.left && head.top==human.top) {
            generateNewHuman()
            addPartOfTale(head.top,head.left)
            increaseDifficult()
        }
    }

    private fun increaseDifficult(){
        if (gameSpeed <= MIN_GAME_SPEED) return
        if (allTale.size%5 == 0) gameSpeed -= 100
    }

    private fun move(directions: Directions) {
        when (directions) {
            Directions.UP -> moveHeadAndRotate(Directions.UP, 90f, -HEAD_SIZE)
            Directions.BOTTOM -> moveHeadAndRotate(Directions.BOTTOM, 270f, HEAD_SIZE)
            Directions.LEFT -> moveHeadAndRotate(Directions.LEFT, 0f, -HEAD_SIZE)
            Directions.RIGHT -> moveHeadAndRotate(Directions.RIGHT, 180f, HEAD_SIZE)
        }
        runOnUiThread {
            if (checkIfSnakeSmash()){
                isPlay = false
                showScore()
                return@runOnUiThread
            }
            makeTaleMove()
            checkIfSnakeEatsPerson()
            container.removeView(head)
            container.addView(head)
        }
    }

    private fun moveHeadAndRotate(directions: Directions, angle: Float, coordinates: Int){
        head.rotation = angle
        when(directions){
            Directions.UP, Directions.BOTTOM -> {
                (head.layoutParams as FrameLayout.LayoutParams).topMargin += coordinates
            }
            Directions.LEFT, Directions.RIGHT -> {
                (head.layoutParams as FrameLayout.LayoutParams).leftMargin += coordinates
            }
        }
        currentDirections = directions
    }

    private fun showScore() {
        AlertDialog.Builder(this)
            .setTitle("Your score: ${allTale.size} items")
            .setPositiveButton("ok") { _, _ ->
                this.recreate()
                }
            .setCancelable(false)
            .create()
            .show()
    }
//
    private fun checkIfSnakeSmash(): Boolean{
        for (talePart in allTale){
            if (talePart.viewCoordinate.left==head.left && talePart.viewCoordinate.top==head.top) return true
        }
        if (head.top< 0 || head.left< 0 || head.top >= HEAD_SIZE* CELLS_OF_FIELD || head.left >= HEAD_SIZE* CELLS_OF_FIELD) return true
        return false
    }

    private fun makeTaleMove() {
        var tempTalePart:PartOfTale? = null
        for (index in 0 until allTale.size){
            val talePart = allTale[index]
            container.removeView(talePart.imageView)
            if (index==0){
                tempTalePart = talePart
                allTale[index] = PartOfTale(ViewCoordinate(head.top, head.left), drawPartOfTale(head.top, head.left))
            }else{
                val anotherTempPartOfTale = allTale[index]
                tempTalePart?.let {
                    allTale[index] = PartOfTale(it.viewCoordinate, drawPartOfTale(it.viewCoordinate.top, it.viewCoordinate.left))
                }
                tempTalePart = anotherTempPartOfTale
            }
        }
    }
}

enum class Directions {
    UP,
    RIGHT,
    BOTTOM,
    LEFT
}