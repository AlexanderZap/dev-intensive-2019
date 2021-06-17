package ru.skillbranch.devintensive

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ru.skillbranch.devintensive.extensions.hideKeyboard
import ru.skillbranch.devintensive.models.Bender

class MainActivity : AppCompatActivity(), View.OnClickListener, TextView.OnEditorActionListener {
   lateinit var sendBtn: ImageView
   lateinit var benderImage: ImageView
   lateinit var textTxt: TextView
   lateinit var messageEt: EditText

   lateinit var benderObj: Bender

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sendBtn = findViewById(R.id.iv_send)
        sendBtn.setOnClickListener(this)

        textTxt = findViewById(R.id.tv_text)

        messageEt = findViewById(R.id.et_message)
        messageEt.setOnEditorActionListener(this)

        benderImage = findViewById(R.id.benderImage)

        val status = savedInstanceState?.getString("STATUS") ?:Bender.Status.NORMAL.name
        val question = savedInstanceState?.getString("QUESTION") ?:Bender.Question.NAME .name
        benderObj = Bender(Bender.Status.valueOf(status), Bender.Question.valueOf(question))

        val(r,g,b) = benderObj.status.color
        benderImage.setColorFilter(Color.rgb(r,g,b), PorterDuff.Mode.MULTIPLY)

        textTxt.text = benderObj.askQuestion()
    }

    override fun onClick(v: View?) {
        if (v?.id ==R.id.iv_send) {
            sendMessage()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("STATUS", benderObj.status.name)
        outState.putString("QUESTION", benderObj.question.name)
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        return if(actionId == EditorInfo.IME_ACTION_DONE){
            sendMessage()
            true
        } else {
            false
        }
    }

    private fun sendMessage(): Unit {
        val (phrase, color) = benderObj.listenAnswer(messageEt.text.toString())
        messageEt.setText("")
        val (r, g, b) = color
        benderImage.setColorFilter(Color.rgb(r, g, b), PorterDuff.Mode.MULTIPLY)
        textTxt.text = phrase
        hideKeyboard()
    }
}