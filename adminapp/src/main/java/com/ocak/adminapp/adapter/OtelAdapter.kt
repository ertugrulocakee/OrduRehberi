package com.ocak.adminapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.ocak.adminapp.R
import com.ocak.adminapp.model.Otel
import com.ocak.adminapp.view.OtelDetayiActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_row_otel.view.*

class OtelAdapter(var otelListesi : ArrayList<Otel>) :RecyclerView.Adapter<OtelAdapter.OtelViewHolder>(){

    var database = FirebaseFirestore.getInstance()

    class OtelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OtelViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view= inflater.inflate(R.layout.recycler_row_otel,parent,false)
        return OtelAdapter.OtelViewHolder(view)

    }

    override fun onBindViewHolder(holder: OtelViewHolder, position: Int) {

        holder.itemView.recycler_row_otelad.text = otelListesi.get(position).otelAdi
        Picasso.get().load(otelListesi.get(position).otelGorseli).into(holder.itemView.recycler_row_otelresim)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, OtelDetayiActivity::class.java)
            intent.putExtra("ad",otelListesi.get(position).otelAdi)
            intent.putExtra("id",otelListesi.get(position).otelId)
            holder.itemView.context.startActivity(intent)

        }


        holder.itemView.silOtelButonu.setOnClickListener {

            val string = otelListesi.get(position).otelId

            database.collection("Oteller").document(string).delete().addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(holder.itemView.context, "Seçilen otel başarıyla silindi", Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener {
                Toast.makeText(holder.itemView.context, it.localizedMessage, Toast.LENGTH_LONG).show()
            }



        }


    }


    override fun getItemCount(): Int {
        return otelListesi.size
    }


}