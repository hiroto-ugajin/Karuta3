package jp.kanoyastore.hiroto.ugajin.karuta3

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import jp.kanoyastore.hiroto.ugajin.karuta3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var mediaPlayer1: MediaPlayer
    private lateinit var mediaPlayer2: MediaPlayer

    val initialMessageArray = arrayOf(
        "京の都の　祇園祭",
        "春日なる　奈良の都の　お大仏",
        "世界遺産の、姫路城",
        "犬も歩けば、棒にあたる",
        "花より団子",
        "古池や　蛙飛び込む　水の音",
        "ひさかたの　光のどけき　春の日に　静心なく　花の散るらむ",
        "忍ぶれど　色に出にけり　わが恋は　ものや思うと　人の問うまで",
        "歌川広重　東海道",
        "葛飾北斎　富嶽三十六景",
        "写楽得意の　歌舞伎役者",
        "鬼より強い　桃太郎",
        "京に上るは　一寸法師",
        "誰でも開ける　玉手箱",
        "文明開花で　すき焼き文化",
        "海を渡って　やってきた　みんな大好き　ベースボール"
    )

    val drawableArray = arrayOf(
        R.drawable.k0,
        R.drawable.k1,
        R.drawable.k2,
        R.drawable.k3,
        R.drawable.k4,
        R.drawable.k5,
        R.drawable.k6,
        R.drawable.k7,
        R.drawable.k8,
        R.drawable.k9,
        R.drawable.k10,
        R.drawable.k11,
        R.drawable.k12,
        R.drawable.k13,
        R.drawable.k14,
        R.drawable.k15
    )

    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mediaPlayer1 = MediaPlayer.create(this, R.raw.nice)
        mediaPlayer2 = MediaPlayer.create(this, R.raw.boo)

        val textView = binding.text

        // initialMessageArrayをシャッフルしてmessageArrayを作成
        val messageArray = initialMessageArray.toMutableList().shuffled().toTypedArray()

        textView.textSize = 25f // ピクセル単位で指定
        textView.text = messageArray[currentIndex]
        currentIndex = 0
        var correctCount = 0

// 元のdrawableArrayのインデックスを保持する配列を作成する
        val originalIndices = IntArray(drawableArray.size) { it }
        val shuffledDrawableArray = drawableArray.clone().apply {
            shuffle()
        }

        // ボタンのクリックリスナーでボタンのtagを取得する
        val buttonClickListener = View.OnClickListener { view ->

            mediaPlayer1.reset()
            mediaPlayer1 = MediaPlayer.create(this, R.raw.nice)

            mediaPlayer2.reset()
            mediaPlayer2 = MediaPlayer.create(this, R.raw.boo)

            val clickedImageIndex = view.tag as? Int ?: -1
            // インデックスを使って必要な処理を行う
            val displayedMessage: String = textView.text.toString()
            val displayedMessageIndex: Int =
                if (displayedMessage != null) initialMessageArray.indexOf(displayedMessage) else -1

            if (clickedImageIndex != -1 && displayedMessageIndex != -1 && clickedImageIndex == displayedMessageIndex) {
// メッセージのインデックスと画像のインデックスが一致する場合の処理
                mediaPlayer1.start()
                correctCount += 1
                val clickedButton = view as? ImageButton
                clickedButton?.let {
                    it.alpha = 0.4f
                    it.isEnabled = false
                }

                if (currentIndex + 1  == messageArray.size) {

                    textView.text = "16問中${correctCount}問正解"
                    correctCount = 0

                    // currentIndexを増やすという部分の下に以下のコードを追加
                    for (i in 0 until shuffledDrawableArray.size) {
                        val button = findViewById<ImageButton>(
                            resources.getIdentifier(
                                "button${i + 1}",
                                "id",
                                packageName
                            )
                        )
                        button.isEnabled = false
                    }

                } else {
                    currentIndex++
                    // 次の要素をtextViewに表示する
                    textView.text = messageArray[currentIndex]
                }

            } else {
                mediaPlayer2.start()

                if (currentIndex + 1 == messageArray.size) {

                    textView.text = "16問中${correctCount}問正解"
                    correctCount = 0

                    // currentIndexを増やすという部分の下に以下のコードを追加
                    for (i in 0 until shuffledDrawableArray.size) {
                        val button = findViewById<ImageButton>(
                            resources.getIdentifier(
                                "button${i + 1}",
                                "id",
                                packageName
                            )
                        )
                        button.isEnabled = false
                    }

                } else {
                    currentIndex++
                    textView.text = messageArray[currentIndex]
                }
            }
        }

        // リセット処理を行う関数
        fun resetGame() {
            currentIndex = 0
            correctCount = 0

            val shuffledDrawableArray = drawableArray.clone().apply {
                shuffle()
            }
            val messageArray = initialMessageArray.toMutableList().shuffled().toTypedArray()
            val textView = binding.text
            textView.text = messageArray[currentIndex]

            for (i in 0 until shuffledDrawableArray.size) {
                val imageButton = findViewById<ImageButton>(
                    resources.getIdentifier(
                        "button${i + 1}",
                        "id",
                        packageName
                    )
                )

                val drawableIndex = drawableArray.indexOf(shuffledDrawableArray[i])
                imageButton.setImageResource(shuffledDrawableArray[i])
                imageButton.tag = drawableIndex

                imageButton.alpha = 1.0f
                imageButton.isEnabled = true
            }
        }

        // shuffledDrawableArrayを使用して画像を設定する
        for (i in 0 until shuffledDrawableArray.size) {
            val imageButton = findViewById<ImageButton>(
                resources.getIdentifier(
                    "button${i + 1}",
                    "id",
                    packageName
                )
            )

            val drawableIndex = drawableArray.indexOf(shuffledDrawableArray[i])
            imageButton.setImageResource(shuffledDrawableArray[i])
            imageButton.tag = drawableIndex

            imageButton.setOnClickListener(buttonClickListener) // リスナーを設定する
        }

        // スタートボタンのクリックでリセット処理を呼び出す
        val startButton = findViewById<Button>(R.id.startButton)
        startButton.setOnClickListener {
            resetGame()
        }
    }
}
