package com.ocak.firebaseordu.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ocak.firebaseordu.R
import com.ocak.firebaseordu.model.IstasyonYorumlari
import kotlinx.android.synthetic.main.recycler_row_share.view.*
import kotlinx.android.synthetic.main.recycler_view_istasyon_yorumlari.view.*


class IstasyonYorumlariAdapter (var istasyonYorumlari : ArrayList<IstasyonYorumlari>) : RecyclerView.Adapter<IstasyonYorumlariAdapter.IstasyonYorumlariViewHolder>(){

    var database = FirebaseFirestore.getInstance()
    var auth = FirebaseAuth.getInstance()

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

        val guncelKullanici = auth.currentUser?.uid.toString()
        if (guncelKullanici.equals(istasyonYorumlari.get(position).kullaniciUid)) {

            holder.itemView.istasyonYorumuSil.visibility = View.VISIBLE

            holder.itemView.istasyonYorumuSil.setOnClickListener {

                val istasyonYorumKimligi = istasyonYorumlari.get(position).istasyonYorumId
                val istasyonKimligi = istasyonYorumlari.get(position).istasyonId


                database.collection("Stations").document(istasyonKimligi)
                    .collection("IstasyonYorumlari").document(istasyonYorumKimligi).delete()
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

            holder.itemView.istasyonYorumuSil.visibility = View.GONE
        }


    }

    override fun getItemCount(): Int {
        return  istasyonYorumlari.size
    }


}

