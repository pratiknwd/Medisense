package com.example.mediscan.report

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mediscan.report.full_report.views.FullReportFragment


class SmartReportViewPagerAdapter(
    private val reportTypeId: Int,
    frag: Fragment,
) : FragmentStateAdapter(frag) {
    
    override fun getItemCount(): Int = 3
    
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CriticalReportFragment.newInstance(reportTypeId)
            1 -> FullReportFragment.newInstance(reportTypeId)
            else -> RecommendationFragment.newInstance(reportTypeId)
        }
    }
}