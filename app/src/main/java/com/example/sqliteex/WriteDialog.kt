package com.example.sqliteex

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.activity_write_dialog.*


class WriteDialog : DialogFragment() {

    var listener : (String, String) -> Unit = { title, post ->}

    var title = ""
    var post = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_write_dialog, container, false)
    }

    override fun onStart() {
        super.onStart()

        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        titleField.setText(title)
        postField.setText(post)
        saveButton.setOnClickListener{
            dismiss()
            val title = titleField.text.toString()
            val post = postField.text.toString()
            if(TextUtils.isEmpty(title) || TextUtils.isEmpty(post)){
                Toast.makeText(context, "정보를 입력하세요", Toast.LENGTH_SHORT).show()
            } else{
                listener.invoke(title, post)
            }
        }
        cancelButton.setOnClickListener { dismiss() }
    }
}

