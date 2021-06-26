package com.ocak.adminapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.ocak.adminapp.R
import com.ocak.adminapp.model.MekanYorumlari
import com.ocak.adminapp.model.OtelYorumlari
import kotlinx.android.synthetic.main.recycler_row_mekan_yorumlari.view.*
import kotlinx.android.synthetic.main.recycler_row_otel_yorumlari.view.*
import kotlinx.android.synthetic.main.recycler_row_otel_yorumlari.view.reycler_row_otel_yorumlari_kullanici

class MekanYorumlariAdapter (var mekanYorumlari : ArrayList<MekanYorumlari>) : RecyclerView.Adapter<MekanYorumlariAdapter.MekanYorumlariViewHolder>(){

    var database = FirebaseFirestore.getInstance()

    class MekanYorumlariViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MekanYorumlariViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val view= inflater.inflate(R.layout.recycler_row_mekan_yorumlari,parent,false)
        return MekanYorumlariAdapter.MekanYorumlariViewHolder(view)


    }

    override fun onBindViewHolder(holder: MekanYorumlariViewHolder, position: Int) {

        holder.itemView.reycler_row_mekan_yorumlari_kullanici.text=mekanYorumlari.get(position).kullaniciAdi
        holder.itemView.reycler_row_mekan_yorumlari_yorum.text=mekanYorumlari.get(position).mekanYorumu
        holder.itemView.reycler_row_mekan_yorumlari_puan.text=mekanYorumlari.get(position).mekanPuan

        holder.itemView.mekanYorumuSil.setOnClickListener {

            val mekanYorumKimligi = mekanYorumlari.get(position).mekanYorumId
            val mekanKimligi =mekanYorumlari.get(position).mekanId


            database.collection("GeziMekanlari").document(mekanKimligi).collection("MekanYorumlari").document(mekanYorumKimligi).delete().addOnCompleteListener {

                if (it.isSuccessful){
                    Toast.makeText(holder.itemView.context,"Yorum başarıyla silindi.", Toast.LENGTH_SHORT).show()

                }

            }.addOnFailureListener {
                Toast.makeText(holder.itemView.context,it.localizedMessage, Toast.LENGTH_SHORT).show()
            }


        }


    }

    override fun getItemCount(): Int {
        return  mekanYorumlari.size
    }


}