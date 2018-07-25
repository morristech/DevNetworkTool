package app.deadmc.devnetworktool.ui.presentation.activities

import android.support.v4.app.Fragment
import android.util.Log
import android.view.MenuItem
import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.ui.presentation.fragments.BaseFragment
import com.arellomobile.mvp.MvpAppCompatActivity

abstract class BaseActivity : MvpAppCompatActivity() {
    fun openFragment(fragment: Fragment, addToBackStack: Boolean) {
        val fragmentManager = supportFragmentManager
        if (addToBackStack) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_main_id, fragment)
                    .addToBackStack(null)
                    .commit()
        } else {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_main_id, fragment)
                    .commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }

    fun runFragment(fragment: BaseFragment, addToBackStack: Boolean = true) {
        Log.e("runFragment",fragment.javaClass.simpleName)
        openFragment(fragment, addToBackStack)
    }
}