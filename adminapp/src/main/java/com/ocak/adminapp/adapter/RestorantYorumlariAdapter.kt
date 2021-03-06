package com.ocak.adminapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.ocak.adminapp.R

import com.ocak.adminapp.model.RestorantYorumlari
import kotlinx.android.synthetic.main.recycler_restorant_yorumlari.view.*


class RestorantYorumlariAdapter (var restorantYorumlari : ArrayList<RestorantYorumlari>) : RecyclerView.Adapter<RestorantYorumlariAdapter.RestorantYorumlariViewHolder>(){

    var database = FirebaseFirestore.getInstance()

    class RestorantYorumlariViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestorantYorumlariViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val view= inflater.inflate(R.layout.recycler_restorant_yorumlari,parent,false)
        return RestorantYorumlariAdapter.RestorantYorumlariViewHolder(view)


    }

    override fun onBindViewHolder(holder: RestorantYorumlariViewHolder, position: Int) {

        holder.itemView.reycler_row_restorant_yorumlari_kullanici.text=restorantYorumlari.get(position).kullaniciAdi
        holder.itemView.reycler_row_restorant_yorumlari_yorum.text=restorantYorumlari.get(position).restorantYorumu
        holder.itemView.reycler_row_restorant_yorumlari_puan.text=restorantYorumlari.get(position).restorantPuan

        holder.itemView.restorantYorumuSil.setOnClickListener {

            val restorantYorumKimligi = restorantYorumlari.get(position).restorantYorumId
            val restorantKimligi =restorantYorumlari.get(position).restorantId


            database.collection("Restorantlar").document(restorantKimligi).collection("RestorantYorumlari").document(restorantYorumKimligi).delete().addOnCompleteListener {

                if (it.isSuccessful){
                    Toast.makeText(holder.itemView.context,"Yorum başarıyla silindi.", Toast.LENGTH_SHORT).show()

                }

            }.addOnFailureListener {
                Toast.makeText(holder.itemView.context,it.localizedMessage, Toast.LENGTH_SHORT).show()
            }


        }


    }

    override fun getItemCount(): Int {
        return  restorantYorumlari.size
    }


}
