package com.skunpham.colorpicker

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.Window
import androidx.appcompat.app.AlertDialog
import com.skunpham.colorpicker.databinding.LayoutDialogPickColorBinding

class DialogPickColor(private val context: Context) {

    private val binding by lazy {
        LayoutDialogPickColorBinding.inflate(LayoutInflater.from(context))
    }

    private val dialog: AlertDialog by lazy {
        AlertDialog.Builder(context, R.style.dialog_transparent_width).setView(binding.root)
            .create()
    }

    init {
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
    }

    fun isShowing(): Boolean {
        return dialog.isShowing
    }

    fun hide() {
        dialog.dismiss()
    }

    fun show(color: Int = Color.BLUE, action: (color: Int) -> Unit) {

        dialog.setCancelable(false)

        binding.colorPickerView.setInitialColor(color)

        binding.txtOk.setOnClickListener{
            action(
                binding.colorPickerView.color
            )
            hide()
        }

        binding.txtCancel.setOnClickListener{
            hide()
        }

        if (!dialog.isShowing)
            dialog.show()
    }
}