package com.example.mediscan

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
    protected fun initToolbar(toolbar: Toolbar, title: String?) {
        initToolbar(toolbar, title, -1, -1)
    }
    
    protected fun initToolbar(toolbar: Toolbar, title: String?, icon: Int, contentDescResId: Int) {
        initToolbar(toolbar, title, icon, contentDescResId, -1)
    }
    
    protected fun initToolbar(toolbar: Toolbar, title: String?, icon: Int, contentDescResId: Int, menuResId: Int) {
        toolbar.title = title
        for (i in 0 until toolbar.childCount) {
            val titleView = toolbar.getChildAt(i)
            if (titleView is TextView) {
                break
            }
        }
        
        if (icon != -1) {
            toolbar.setNavigationIcon(icon)
            toolbar.setNavigationOnClickListener { view: View? -> this.onNavigationIconClick(view) }
            toolbar.setNavigationContentDescription(contentDescResId)
        }
        
        if (menuResId != -1) {
            toolbar.inflateMenu(menuResId)
        }
    }
    
    protected abstract fun onNavigationIconClick(iconTapped: View?)
}