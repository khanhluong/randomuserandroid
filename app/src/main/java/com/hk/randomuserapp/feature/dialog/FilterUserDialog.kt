package com.hk.randomuserapp.feature.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.widget.CheckBox
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.hk.randomuserapp.R

class FilterUserDialog : DialogFragment() {

    private lateinit var filterDialogListener: FilterDialogListener

    private var isMaleCheck = false

    interface FilterDialogListener {
        fun onChooseFilter(isMale: Boolean, dialogFragment: DialogFragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            filterDialogListener = (parentFragment as FilterDialogListener?)!!
        } catch (e: ClassCastException) {
            throw ClassCastException("Calling fragment must implement FilterDialogListener interface")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_user_random_filter, null)
            val cbMale = view.findViewById<CheckBox>(R.id.cbMale)
            cbMale.isChecked = isMaleCheck


            builder.setView(view)
                .setTitle(R.string.filter)
                .setPositiveButton(R.string.ok, DialogInterface.OnClickListener { dialog, id ->
                    filterDialogListener.onChooseFilter(cbMale.isChecked, this)
                    isMaleCheck = cbMale.isChecked

                })
                .setNegativeButton(R.string.cancel, DialogInterface.OnClickListener { dialog, id ->
                    getDialog()?.cancel()
                })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }


}