package com.hotta.hoho.view.custom

import android.app.Dialog
import android.content.Context
import android.icu.text.CaseMap.Title
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.hotta.hoho.R
import com.hotta.hoho.databinding.ViewTwobtnPoopupBinding

class CustomPopup(
    context: Context,
    listener: PopupListener,
    title: String,
    content: String,
    isCancelBtn: Boolean
) : Dialog(context) {

    val binding: ViewTwobtnPoopupBinding = ViewTwobtnPoopupBinding.inflate(layoutInflater)
    var title: String
    var content: String
    var isCancelBtn: Boolean = isCancelBtn
    var listener: PopupListener

    interface PopupListener {
        fun conFirmClick()
        fun cancelClick()
    }

    init {
        setContentView(binding.root)
        this.title = title
        this.content = content
        this.isCancelBtn = isCancelBtn
        this.listener = listener
        setMsg()
    }

    fun setMsg() {
        val txtTitle = binding.txtTitle
        val txtContent = binding.txtContent
        val cancel = binding.txtCancel
        val confirm = binding.txtConfirm


        if (isCancelBtn) {
            cancel.setOnClickListener {
                listener.cancelClick()
                dismiss()
            }
        } else {
            cancel.visibility = View.GONE
        }

        confirm.setOnClickListener {
            listener.conFirmClick()
            dismiss()
        }
        txtTitle.setText(title)
        txtContent.setText(content)
    }

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_twobtn_poopup)
    }*/


}