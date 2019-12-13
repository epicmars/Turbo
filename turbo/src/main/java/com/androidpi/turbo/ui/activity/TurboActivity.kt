package com.androidpi.turbo.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.androidpi.turbo.R
import com.androidpi.turbo.ui.fragment.HomeFragment
import com.androidpi.turbo.ui.fragment.PlaceholderFragment
import com.androidpi.turbo.utils.PermissionUtils
import kotlinx.android.synthetic.main.activity_turbo.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.EasyPermissions.PermissionCallbacks

class TurboActivity : AppCompatActivity(), PermissionCallbacks {
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var mViewPager: ViewPager? = null

//    @BindLayout(R.layout.activity_turbo)
//    lateinit var binding: ActivityTurboBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_turbo)
        setSupportActionBar(toolbar)
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById<View>(R.id.container) as ViewPager
        mViewPager!!.adapter = mSectionsPagerAdapter
        if (!hasShortcutPermission()) {
            PermissionUtils.requestPermission(
                this,
                REQ_SHORTCUT_PERM,
                PermissionUtils.SHORTCUT_PERMS,
                "需要快捷方式权限以在桌面创建快捷图标"
            )
        }
    }

    private fun hasShortcutPermission(): Boolean {
        return EasyPermissions.hasPermissions(this, *PermissionUtils.SHORTCUT_PERMS)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onPermissionsGranted(
        requestCode: Int,
        perms: List<String>
    ) {
    }

    override fun onPermissionsDenied(
        requestCode: Int,
        perms: List<String>
    ) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean { // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_profile, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }

    inner class SectionsPagerAdapter(fm: FragmentManager?) :
        FragmentPagerAdapter(fm!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        private lateinit var fragments: List<Fragment>

        init {
            fragments = listOf(HomeFragment.newInstance(), PlaceholderFragment.newInstance(1))
        }

        override fun getItem(position: Int): Fragment {
            return fragments.get(position)
        }

        override fun getCount(): Int {
            return fragments.size
        }
    }

    companion object {
        private const val REQ_SHORTCUT_PERM = 1
    }
}