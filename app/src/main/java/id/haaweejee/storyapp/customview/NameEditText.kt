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


class NameEditText : AppCompatEditText {
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
        hint = context.getString(R.string.hint_name)
    }

    private fun init(){
        maxLines = 1
        inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
    }

}