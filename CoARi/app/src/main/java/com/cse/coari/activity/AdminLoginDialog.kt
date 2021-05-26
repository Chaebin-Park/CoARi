package com.cse.coari.activity

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import com.cse.coari.R

class AdminLoginDialog(context: Context) {
    private val dialog = Dialog(context)
    val editAdminId = dialog.findViewById<EditText>(R.id.et_admin_id)
    val editAdminPw = dialog.findViewById<EditText>(R.id.et_admin_pw)
    val btnInput = dialog.findViewById<Button>(R.id.btn_admin_login)
    val btnCancel = dialog.findViewById<Button>(R.id.btn_admin_cancel)

    fun showDialog(){
        dialog.setContentView(R.layout.admin_login_dialog)
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)

        btnInput.setOnClickListener{
            onClickedListener.onClicked(editAdminId.text.toString(), editAdminPw.text.toString())
            dialog.dismiss()
        }

        btnCancel.setOnClickListener{
            dialog.dismiss()
        }

        dialog.show()
    }

    interface ButtonClickListener{
        fun onClicked(id: String, pw: String)
    }

    private lateinit var onClickedListener: ButtonClickListener

    fun setOnClickedListener(listener: ButtonClickListener) {
        onClickedListener = listener
    }
}