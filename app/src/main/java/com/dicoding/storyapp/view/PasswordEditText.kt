package com.dicoding.storyapp.view

import android.content.Context
import android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import androidx.core.widget.doOnTextChanged
import com.dicoding.storyapp.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class PasswordEditText : TextInputLayout {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        val editText = TextInputEditText(context)
        editText.id = R.id.edt_password_input
        editText.inputType = TYPE_TEXT_VARIATION_PASSWORD
        editText.transformationMethod = PasswordTransformationMethod.getInstance()
        editText.doOnTextChanged { text, _, _, _ ->
            if (text!!.length < 6) {
                isErrorEnabled = true
                error = resources.getString(R.string.error_min_length_password_message)
            } else {
                isErrorEnabled = false
                error = null
            }
        }
        addView(editText)
    }
}
