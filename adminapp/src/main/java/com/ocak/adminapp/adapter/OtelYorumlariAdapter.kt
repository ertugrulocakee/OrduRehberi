package com.ocak.adminapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.ocak.adminapp.R
import com.ocak.adminapp.model.OtelYorumlari
import kotlinx.android.synthetic.main.recycler_row_otel_yorumlari.view.*

class OtelYorumlariAdapter (var otelYorumlari : ArrayList<OtelYorumlari>) : RecyclerView.Adapter<OtelYorumlariAdapter.OtelYorumlariViewHolder>(){

  var database = FirebaseFirestore.getInstance()

  class OtelYorumlariViewHolder(itemView :View) : RecyclerView.ViewHolder(itemView){


  }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OtelYorumlariViewHolder {

      val inflater = LayoutInflater.from(parent.context)
      val view= inflater.inflate(R.layout.recycler_row_otel_yorumlari,parent,false)
      return OtelYorumlariAdapter.OtelYorumlariViewHolder(view)


    }

    override fun onBindViewHolder(holder: OtelYorumlariViewHolder, position: Int) {

      holder.itemView.reycler_row_otel_yorumlari_kullanici.text=otelYorumlari.get(position).kullaniciAdi
      holder.itemView.reycler_row_otel_yorumlari_yorum.text=otelYorumlari.get(position).otelYorumu
      holder.itemView.reycler_row_otel_yorumlari_puan.text=otelYorumlari.get(position).otelPuan

      holder.itemView.otelYorumuSil.setOnClickListener {

        val otelYorumKimligi = otelYorumlari.get(position).otelYorumId
        val otelKimligi =otelYorumlari.get(position).otelId


        database.collection("Oteller").document(otelKimligi).collection("OtelYorumlari").document(otelYorumKimligi).delete().addOnCompleteListener {

          if (it.isSuccessful){
            Toast.makeText(holder.itemView.context,"Yorum başarıyla silindi.",Toast.LENGTH_SHORT).show()

          }

        }.addOnFailureListener {
          Toast.makeText(holder.itemView.context,it.localizedMessage,Toast.LENGTH_SHORT).show()
        }


        }


    }

    override fun getItemCount(): Int {
        return  otelYorumlari.size
    }


}