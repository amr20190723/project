package com.example.project
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.DialogFragment


class CustomDialogFragment: DialogFragment(R.layout.fragment_custom_dialog) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val cancelbt : Button = view.findViewById(R.id.cancelBT);
        cancelbt.setOnClickListener{
            dismiss()
        }
    }}