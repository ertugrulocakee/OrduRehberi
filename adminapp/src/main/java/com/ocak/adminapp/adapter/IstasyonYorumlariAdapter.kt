package com.ocak.adminapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.ocak.adminapp.R
import com.ocak.adminapp.model.IstasyonYorumlari
import com.ocak.adminapp.model.RestorantYorumlari
import kotlinx.android.synthetic.main.recycler_restorant_yorumlari.view.*
import kotlinx.android.synthetic.main.recycler_restorant_yorumlari.view.reycler_row_restorant_yorumlari_kullanici
import kotlinx.android.synthetic.main.recycler_view_istasyon_yorumlari.view.*

class IstasyonYorumlariAdapter (var istasyonYorumlari : ArrayList<IstasyonYorumlari>) : RecyclerView.Adapter<IstasyonYorumlariAdapter.IstasyonYorumlariViewHolder>(){

    var database = FirebaseFirestore.getInstance()

    class IstasyonYorumlariViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IstasyonYorumlariViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val view= inflater.inflate(R.layout.recycler_view_istasyon_yorumlari,parent,false)
        return IstasyonYorumlariAdapter.IstasyonYorumlariViewHolder(view)


    }

    override fun onBindViewHolder(holder: IstasyonYorumlariViewHolder, position: Int) {

        holder.itemView.reycler_row_istasyon_yorumlari_kullanici.text=istasyonYorumlari.get(position).kullaniciAdi
        holder.itemView.reycler_row_istasyon_yorumlari_yorum.text=istasyonYorumlari.get(position).istasyonYorumu
        holder.itemView.reycler_row_istasyon_yorumlari_puan.text=istasyonYorumlari.get(position).istasyonPuan

        holder.itemView.istasyonYorumuSil.setOnClickListener {

            val istasyonYorumKimligi = istasyonYorumlari.get(position).istasyonYorumId
            val istasyonKimligi =istasyonYorumlari.get(position).istasyonId


            database.collection("Stations").document(istasyonKimligi).collection("IstasyonYorumlari").document(istasyonYorumKimligi).delete().addOnCompleteListener {

                if (it.isSuccessful){
                    Toast.makeText(holder.itemView.context,"Yorum başarıyla silindi.", Toast.LENGTH_SHORT).show()

                }

            }.addOnFailureListener {
                Toast.makeText(holder.itemView.context,it.localizedMessage, Toast.LENGTH_SHORT).show()
            }


        }


    }

    override fun getItemCount(): Int {
        return  istasyonYorumlari.size
    }


}

