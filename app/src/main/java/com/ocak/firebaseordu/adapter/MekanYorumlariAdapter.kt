package com.ocak.firebaseordu.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ocak.firebaseordu.R
import com.ocak.firebaseordu.model.MekanYorumlari
import kotlinx.android.synthetic.main.recycler_restorant_yorumlari.view.*
import kotlinx.android.synthetic.main.recycler_row_mekan_yorumlari.view.*

class MekanYorumlariAdapter (var mekanYorumlari : ArrayList<MekanYorumlari>) : RecyclerView.Adapter<MekanYorumlariAdapter.MekanYorumlariViewHolder>(){

    var database = FirebaseFirestore.getInstance()
    var auth = FirebaseAuth.getInstance()

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


        val guncelKullanici = auth.currentUser?.uid.toString()
        if (guncelKullanici.equals(mekanYorumlari.get(position).kullaniciUid)) {

            holder.itemView.mekanYorumuSil.visibility = View.VISIBLE


            holder.itemView.mekanYorumuSil.setOnClickListener {

                val mekanYorumKimligi = mekanYorumlari.get(position).mekanYorumId
                val mekanKimligi = mekanYorumlari.get(position).mekanId


                database.collection("GeziMekanlari").document(mekanKimligi)
                    .collection("MekanYorumlari").document(mekanYorumKimligi).delete()
                    .addOnCompleteListener {

                        if (it.isSuccessful) {
                            Toast.makeText(
                                holder.itemView.context,
                                "Yorum başarıyla silindi.",
                                Toast.LENGTH_SHORT
                            ).show()

                        }

                    }.addOnFailureListener {
                    Toast.makeText(holder.itemView.context, it.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                }


            }
        }else{

            holder.itemView.mekanYorumuSil.visibility = View.GONE
        }


    }

    override fun getItemCount(): Int {
        return  mekanYorumlari.size
    }


}