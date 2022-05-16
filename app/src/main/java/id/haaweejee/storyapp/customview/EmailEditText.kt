package id.haaweejee.storyapp.customview

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import id.haaweejee.storyapp.R


class EmailEditText : AppCompatEditText {
    constructor(context: Context) : super(context){
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs){
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        hint = context.getString(R.string.hint_email)
    }

    private fun init(){
        maxLines = 1
        inputType = InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS
        addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, before: Int, count: Int, p3: Int) {
                setSelection(text!!.length)
                if (s != null) {
                    if (s.isEmpty()) {
                        error = null
                        val colorStateList = ColorStateList.valueOf(ContextCompat.getColor(context,
                            R.color.midnight_purple
                        ))
                        ViewCompat.setBackgroundTintList(this@EmailEditText, colorStateList)
                    }else if (!Patterns.EMAIL_ADDRESS.matcher(s).matches()){
                        val colorStateList = ColorStateList.valueOf(ContextCompat.getColor(context,
                            R.color.orange_red
                        ))
                        ViewCompat.setBackgroundTintList(this@EmailEditText, colorStateList)
                        error = "Format Email tidak valid"
                    }else{
                        error = null
                        val colorStateList = ColorStateList.valueOf(ContextCompat.getColor(context,
                            R.color.midnight_purple
                        ))
                        ViewCompat.setBackgroundTintList(this@EmailEditText, colorStateList)
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

}