package com.lavyshyk.countrycity.ui.sreensaver

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lavyshyk.countrycity.R

class DontAskAgainFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_dont_ask_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mButton = view.findViewById<AppCompatButton>(R.id.buttonSetting)
//        mButton.setOnClickListener {
//            setFragmentResult(
//               SETTINGS_KEY,
//                bundleOf(RESULT_KEY to true)
//            )
//            dismiss()
//        }
    }
}