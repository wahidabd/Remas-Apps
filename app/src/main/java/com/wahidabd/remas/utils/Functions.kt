package com.wahidabd.remas.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.wahidabd.remas.R
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

// a function for handle show or hide an password
fun showHidePassword(value: Boolean, text: EditText) {
    if (value) text.transformationMethod = HideReturnsTransformationMethod.getInstance()
    else text.transformationMethod = PasswordTransformationMethod.getInstance()
}

fun ImageView.setImageUrl(url: String) =
    Glide.with(this)
        .load(url)
        .into(this)

@RequiresApi(Build.VERSION_CODES.O)
fun myTimeStamp(): String =
    LocalDateTime.now().toString()

// a function for handle text from textview
fun EditText.textToTrim() = this.text.toString().trim()

// a function for handle toast or alert
fun Fragment.quickShowToast(msg: String) =
    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()

// a class from handle lottie loading
class Loading{
    private var dialog: Dialog? = null

    fun start(context: Context) {
        dialog = setDialog(context)
    }

    fun stop(){
        if (dialog?.isShowing == true) dialog?.cancel()
    }

    private fun setDialog(context: Context): Dialog? {
        dialog = Dialog(context)
        dialog.let {
            it?.show()
            it?.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
            it?.setContentView(R.layout.lottie_loading)
            it?.setCancelable(false)
            it?.setCanceledOnTouchOutside(false)
            return it
        }
    }
}