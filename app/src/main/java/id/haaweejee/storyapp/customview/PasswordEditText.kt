package id.haaweejee.storyapp.customview

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import id.haaweejee.storyapp.R


class PasswordEditText : AppCompatEditText {
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
        hint = context.getString(R.string.hint_password)
    }


    private fun init(){
        maxLines = 1
        addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, before: Int, count: Int, p3: Int) {
                inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                transformationMethod = PasswordTransformationMethod.getInstance()
                setSelection(text!!.length)
                if (s != null) {
                    when {
                        s.isEmpty() -> {
                            error = null
                            val colorStateList = ColorStateList.valueOf(ContextCompat.getColor(context,
                                R.color.midnight_purple
                            ))
                            ViewCompat.setBackgroundTintList(this@PasswordEditText, colorStateList)
                        }
                        s.length < 6 -> {
                            val colorStateList = ColorStateList.valueOf(ContextCompat.getColor(context,
                                R.color.orange_red
                            ))
                            ViewCompat.setBackgroundTintList(this@PasswordEditText, colorStateList)
                            error = "Jumlah Karakter Password kurang"
                        }
                        else -> {
                            val colorStateList = ColorStateList.valueOf(ContextCompat.getColor(context,
                                R.color.midnight_purple
                            ))
                            ViewCompat.setBackgroundTintList(this@PasswordEditText, colorStateList)
                            error = null
                        }
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

}