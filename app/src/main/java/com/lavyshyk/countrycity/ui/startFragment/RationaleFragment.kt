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
import com.lavyshyk.countrycity.RATIONALE_KEY

class RationaleFragment : BottomSheetDialogFragment() {

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            return inflater.inflate(R.layout.bottom_sheet_rationale_frafment, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            val mButton = view.findViewById<AppCompatButton>(R.id.buttonAllow)
            mButton.setOnClickListener {
                setFragmentResult(
                   RATIONALE_KEY,
                    bundleOf(RATIONALE_KEY to true)
                )
                dismiss()
            }
        }
}