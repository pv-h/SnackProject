 package com.my.snackproject

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.my.snackproject.SnakeCore.isPlay
import com.my.snackproject.SnakeCore.startTheGame
import com.my.snackproject.SnakeCore.nextMove
import kotlinx.android.synthetic.main.activity_main.*


const val HEAD_SIZE = 100
const val CELLS_OF_FIELD = 10


class MainActivity : AppCompatActivity() {
//    private val allTale = mutableListOf<PartOfTale>()
    private val human by lazy {
        ImageView(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val head = View(this)
        head.layoutParams = FrameLayout.LayoutParams(HEAD_SIZE, HEAD_SIZE)
        head.background = ContextCompat.getDrawable(this, R.drawable.circle)
//        container.layoutParams = FrameLayout.LayoutParams(HEAD_SIZE* CELLS_OF_FIELD, HEAD_SIZE* CELLS_OF_FIELD)

        startTheGame()
        generateNewHuman()
        nextMove = { move(Directions.BOTTOM, head) }

        ivArrowUp.setOnClickListener { nextMove = { move(Directions.UP, head) } }
        ivArrowBottom.setOnClickListener { nextMove = { move(Directions.BOTTOM, head) } }
        ivArrowLeft.setOnClickListener { nextMove = { move(Directions.LEFT, head) } }
        ivArrowRight.setOnClickListener { nextMove = { move(Directions.RIGHT, head) } }
        ivPause.setOnClickListener {
            if (isPlay) ivPause.setImageResource(R.drawable.ic_play)
            else ivPause.setImageResource(R.drawable.ic_pause)
            isPlay = !isPlay
        }
    }

    private fun generateNewHuman() {
         human.layoutParams = FrameLayout.LayoutParams(HEAD_SIZE, HEAD_SIZE)
         human.setImageResource(R.drawable.ic_person)
         (human.layoutParams as FrameLayout.LayoutParams).topMargin = (0 until CELLS_OF_FIELD).random() * HEAD_SIZE
         (human.layoutParams as FrameLayout.LayoutParams).leftMargin = (0 until CELLS_OF_FIELD).random() * HEAD_SIZE
         container.removeView(human)
         container.addView(human)
    }
//    private fun addPartOfTale (top:Int, left:Int){
//        val talePart = drawPartOfTale(top,left)
//        allTale.add(PartOfTale(top,left,talePart))
//    }

//    private fun drawPartOfTale(top: Int, left: Int): ImageView {
//        val taleImage = ImageView(this)
//        taleImage.setImageResource(R.drawable.ic_person)
//        taleImage.setBackgroundColor(ContextCompat.getColor(this,R.color.colorAccent))
//        taleImage.layoutParams = FrameLayout.LayoutParams(HEAD_SIZE, HEAD_SIZE)
//        (taleImage.layoutParams as FrameLayout.LayoutParams).topMargin = top
//        (taleImage.layoutParams as FrameLayout.LayoutParams).leftMargin = left
//
//        container.addView(taleImage)
//        return taleImage
//    }

    private fun checkIfSnakeEatsPerson(head: View) {
        if (head.left==human.left && head.top==human.top) {
            generateNewHuman()
//            addPartOfTale(head.top,head.left)
        }
    }

    fun move(directions: Directions, head: View) {
        when (directions) {
            Directions.UP -> (head.layoutParams as FrameLayout.LayoutParams).topMargin -= HEAD_SIZE
            Directions.BOTTOM -> (head.layoutParams as FrameLayout.LayoutParams).topMargin += HEAD_SIZE
            Directions.LEFT -> (head.layoutParams as FrameLayout.LayoutParams).leftMargin -= HEAD_SIZE
            Directions.RIGHT -> (head.layoutParams as FrameLayout.LayoutParams).leftMargin += HEAD_SIZE
        }
        runOnUiThread {
//            if (checkIfSnakeSmash(head)){
//                isPlay = false
//                showScore()
//                return@runOnUiThread
//            }
//            makeTaleMove(head.top,head.left)
//            checkIfSnakeEatsPerson(head)
            container.removeView(head)
            container.addView(head)
        }
    }

//    private fun showScore() {
//        AlertDialog.Builder(this)
//            .setTitle("Your score: ${allTale.size} items")
//            .setPositiveButton("ok") { _, _ ->
//                this.recreate()
//                }
//            .setCancelable(false)
//            .create()
//            .show()
//    }
//
//    private fun checkIfSnakeSmash(head: View): Boolean{
//        for (talePart in allTale){
//            if (talePart.left==head.left && talePart.top==head.top) return true
//        }
//        if (head.top< 0 || head.left< 0 || head.top >= HEAD_SIZE* CELLS_OF_FIELD || head.left >= HEAD_SIZE* CELLS_OF_FIELD) return true
//        return false
//    }

//    private fun makeTaleMove(headTop: Int, headLeft: Int) {
//        var tempTalePart:PartOfTale? = null
//        for (index in 0 until allTale.size){
//            val talePart = allTale[index]
//            container.removeView(talePart.imageView)
//            if (index==0){
//                tempTalePart = talePart
//                allTale[index] = PartOfTale(headTop, headLeft, drawPartOfTale(headTop, headLeft))
//            }else{
//                val anotherTempPartOfTale = allTale[index]
//                tempTalePart?.let {
//                    allTale[index] = PartOfTale(it.top,it.left,drawPartOfTale(it.top,it.left))
//                }
//                tempTalePart = anotherTempPartOfTale
//            }
//        }
//    }
}

enum class Directions {
    UP,
    RIGHT,
    BOTTOM,
    LEFT
}