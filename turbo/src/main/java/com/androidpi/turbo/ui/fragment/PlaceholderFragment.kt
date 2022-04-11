package com.androidpi.turbo.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.androidpi.turbo.R
import com.androidpi.turbo.base.ui.BaseFragment

class PlaceholderFragment : BaseFragment() {
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
        ): View? {
            val rootView =
                inflater.inflate(R.layout.turbo_fragment_profile, container, false)
            val textView =
                rootView.findViewById<View>(R.id.section_label) as TextView
            textView.text = getString(
                R.string.turbo_section_format,
                arguments!!.getInt(ARG_SECTION_NUMBER)
            )
            return rootView
        }

        companion object {
            private const val ARG_SECTION_NUMBER = "section_number"
            fun newInstance(sectionNumber: Int): PlaceholderFragment {
                val fragment =
                    PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }
    }