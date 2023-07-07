package com.example.androidautoclick.ui.uitils

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * 对输入的金额进行格式化处理
 */
fun EditText.FormatMoney(back: ((String) -> (Unit))?) {
    var et = this
    var input = ""
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable) {
            var str = et.text?.toString()!!.trim()
            //用于处理多个小数点输入问题
            if (!TextUtils.isEmpty(str) && str.indexOf(".") != str.lastIndexOf(".")) {
                et.setText(input)
                et.setSelection(et.text!!.length)//将光标移至文字末尾
                return
            }
            //控制输入时只能输入1个0
            if (!TextUtils.isEmpty(str) && str == "00") {
                et.setText("0")
                et.setSelection(et.text!!.length)//将光标移至文字末尾
                return
            }
            //控制输入时第一个为0第二位不为.的情况
            if (!TextUtils.isEmpty(str) && str.length >= 2 && str.indexOf("0") == 0 && str.indexOf(".") != 1) {
                et.setText(str.substring(1, str.length))
                et.setSelection(et.text!!.length)//将光标移至文字末尾
                return
            }
            input = str
            //用于处理限制输入小数点后2位问题
            val index = et.text?.indexOf(".")
            if (index!! > 0 && index < et.text?.length!! - 3) {
                val str = et.text?.substring(0, et.text?.toString()?.trim()?.indexOf(".")!! + 3)
                et.setText(str)
                et.setSelection(et.text!!.length)//将光标移至文字末尾
            }
            if (index == 0) {
                et.setText("0" + et.text!!)
                et.setSelection(et.text!!.length)//将光标移至文字末尾
            }
            back?.invoke(str)
        }
    })
}

/**
 * 对输入的内容进行格式化处理
 * 使输入的数字大于0
 */
fun EditText.formatNum(back: ((String?) -> (Unit))? = null) {
    inputType = InputType.TYPE_CLASS_NUMBER
    var et = this
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable) {
            var str = et.text?.toString()!!.trim()
            //控制输入时只能输入1个0
            if (!TextUtils.isEmpty(str) && str.startsWith("0") && str != "0") {
                try {
                    et.setText(str.toInt().toString())
                    et.setSelection(str.length)
                } catch (e: Exception) {
                    et.setText("1")
                }
            }
            back?.invoke(str)
        }
    })
}

/**
 * 添加键盘搜索监听
 */
fun EditText.addEditorActionListener(search: ((String?) -> (Unit))?) {
    setOnEditorActionListener { v, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            search?.invoke(text.toString())
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm?.hideSoftInputFromWindow(v.windowToken, 0)
        }
        false
    }
}