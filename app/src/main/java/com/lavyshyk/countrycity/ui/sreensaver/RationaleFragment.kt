package com.lavyshyk.countrycity.ui.sreensaver

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lavyshyk.countrycity.RATIONALE_KEY
import com.lavyshyk.countrycity.RESULT_KEY
import com.lavyshyk.countrycity.databinding.BottomSheetRationaleFrafmentBinding

class RationaleFragment : BottomSheetDialogFragment() {
 private var binding: BottomSheetRationaleFrafmentBinding? = null

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            binding = BottomSheetRationaleFrafmentBinding.inflate(layoutInflater)
            return binding?.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            binding?.buttonAllow?.setOnClickListener {
                setFragmentResult(
                   RATIONALE_KEY, bundleOf(RESULT_KEY to true)
                )
                dismiss()
            }
            binding?.buttonDeny?.setOnClickListener {
                setFragmentResult(
                   RATIONALE_KEY, bundleOf(RESULT_KEY to false)
                )
                dismiss()
            }
        }
}