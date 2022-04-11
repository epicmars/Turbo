package com.androidpi.turbo.ui.fragment

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.androidpi.turbo.NavCenter
import com.androidpi.turbo.R
import com.androidpi.turbo.base.ui.BaseFragment
import com.androidpi.turbo.business.model.HomeEntryItem
import com.androidpi.turbo.ui.viewholder.HomeEntryViewHolder
import com.androidpi.turbo.utils.AppHelper

/**
 * Created on 2019-12-12.
 */
class HomeFragment : BaseFragment(){

    lateinit var adapter: HomeEntryAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        adapter = HomeEntryAdapter(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.turbo_fragment_home, null)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = adapter
    }

    class HomeEntryAdapter() : RecyclerView.Adapter<HomeEntryViewHolder>() {
        private var context : Context? = null
        private var homeEntries = mutableListOf<HomeEntryItem>()

        constructor(context: Context?) : this() {
            this.context = context
            // 返回APP
            val item1 = HomeEntryItem(context, object : Runnable {
                override fun run() {
                    NavCenter.navToApp(context)
                }
            })
            item1.title = AppHelper.appLabel(context)
            item1.icon = AppHelper.appIcon(context)

            // 应用信息
            val item2 = HomeEntryItem(context, object: Runnable {
                override fun run() {
                    NavCenter.navToAppDetailSettings(context)
                }
            })
            item2.title = "应用信息"
            val icon = AppHelper.appIcon(context)
            var width = icon?.intrinsicWidth ?: -1
            var height = icon?.intrinsicHeight ?: -1
            if (width <= 0 || height <= 0) {
                width = 100
                height = 100
            }
            var bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            icon?.setBounds(0, 0, width, height)
            icon?.draw(canvas)

            var icInfo = ContextCompat.getDrawable(context!!, R.drawable.turbo_ic_info)
            val bounds = Rect()
            bounds.set(width/2, height/2, width, height)
            icInfo?.bounds = bounds
            icInfo?.draw(canvas)

            item2.icon = bitmap.toDrawable(context.resources)

            // 开发者选项
            val item3 = HomeEntryItem(context, object: Runnable {
                override fun run() {
                    NavCenter.navToDeveloperOptions(context)
                }
            })
            item3.title = "开发者选项"
            item3.iconRes = R.drawable.turbo_ic_development_options

            // 设置
            val item4 = HomeEntryItem(context, object: Runnable {
                override fun run() {
                    NavCenter.navToSystemSettings(context)
                }
            })
            item4.title = "系统设置"
            item4.icon = AppHelper.systemSettingsIcon(context)

//            // 展示布局边界
//            val item5 = HomeEntryItem(context, object: Runnable {
//                override fun run() {
//
//                }
//            })
//            item5.title = "展示布局边界"
            homeEntries.add(item1)
            homeEntries.add(item2)
            homeEntries.add(item3)
            homeEntries.add(item4)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): HomeEntryViewHolder {
            val view = LayoutInflater.from(p0.context).inflate(R.layout.turbo_vh_home_entry, p0, false)
            return HomeEntryViewHolder(view)
        }

        override fun getItemCount(): Int {
            return homeEntries.size
        }

        override fun onBindViewHolder(p0: HomeEntryViewHolder, p1: Int) {
            val item = homeEntries.get(p1)
            if (item == null)
                return
            p0.itemView.setOnClickListener {
                item.run()
            }
            if (item.icon != null) {
                p0.setIcon(item.icon)
            } else {
                p0.setIcon(item.iconRes)
            }
            p0.setName(item.title)
        }
    }

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"
        fun newInstance(): HomeFragment {
            val fragment =
                HomeFragment()
            val args = Bundle()
            args.putInt(ARG_SECTION_NUMBER, 1)
            fragment.arguments = args
            return fragment
        }
    }
}