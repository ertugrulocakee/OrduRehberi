package com.ocak.firebaseordu.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.ocak.firebaseordu.R
import com.ocak.firebaseordu.model.Otel
import com.ocak.firebaseordu.view.OtelDetayiActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_row_otel.view.*


class OtelAdapter(var otelListesi : ArrayList<Otel>) : RecyclerView.Adapter<OtelAdapter.OtelViewHolder>(){

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


    }


    override fun getItemCount(): Int {
        return otelListesi.size
    }


}