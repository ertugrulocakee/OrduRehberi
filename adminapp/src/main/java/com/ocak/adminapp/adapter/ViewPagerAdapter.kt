package com.ocak.adminapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.ocak.adminapp.*


class ViewPagerAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(manager) {




     override fun getItem(position: Int): Fragment {

         lateinit var secilenFragment : Fragment

         when(position){
             0->{
                  secilenFragment= hepsi_fragment.newInstance()
             }
             1->{
                  secilenFragment= gezikitabi_fragment.newInstance()
             }
             2->{
                 secilenFragment= restorant_fragment.newInstance()
             }
             3->{
                 secilenFragment= otel_fragment.newInstance()
             }
             4->{
                 secilenFragment= istasyon_fragment.newInstance()
             }
             else->  null

         }


         return  secilenFragment

     }

    override fun getCount(): Int {
        return 5
    }

     override fun getPageTitle(position: Int): CharSequence? {

         lateinit var secilenBaslik : CharSequence

         when(position){
             0->{
                 secilenBaslik="Hepsi"
             }
             1->{
                 secilenBaslik="Gezi"
             }
             2->{
                 secilenBaslik="Yemek"
             }
             3->{
                 secilenBaslik="Konaklama"
             }
             4->{
                 secilenBaslik="Ulaşım"
             }
             else->  null

         }


         return secilenBaslik

     }


 }