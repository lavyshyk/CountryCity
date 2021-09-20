package com.lavyshyk.countrycity.ui.startFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lavyshyk.countrycity.R
import com.lavyshyk.countrycity.RESULT_KEY
import com.lavyshyk.countrycity.SETTINGS_KEY

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
        mButton.setOnClickListener {
            setFragmentResult(
               SETTINGS_KEY,
                bundleOf(RESULT_KEY to true)
            )
            dismiss()
        }
    }
}