package com.ocak.firebaseordu.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.ocak.firebaseordu.R
import com.ocak.firebaseordu.model.Etkinlik
import com.ocak.firebaseordu.view.EtkinlikDetayiActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_row_etkinlik.view.*


class EtkinlikListesiAdapter (var etkinlikList : ArrayList<Etkinlik>) : RecyclerView.Adapter<EtkinlikListesiAdapter.EtkinlikListesiViewHolder>(){

    var database = FirebaseFirestore.getInstance()

    class EtkinlikListesiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EtkinlikListesiViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view= inflater.inflate(R.layout.recycler_row_etkinlik,parent,false)
        return EtkinlikListesiAdapter.EtkinlikListesiViewHolder(view)

    }

    override fun onBindViewHolder(holder: EtkinlikListesiViewHolder, position: Int) {

        holder.itemView.recycler_row_etkinlik_ad.text = etkinlikList.get(position).etkinlikAdi
        Picasso.get().load(etkinlikList.get(position).etkinlikGorseli).into(holder.itemView.recycler_row_etkinlik_resim)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, EtkinlikDetayiActivity::class.java)
            intent.putExtra("ad",etkinlikList.get(position).etkinlikAdi)
            intent.putExtra("id",etkinlikList.get(position).etkinlikId)
            holder.itemView.context.startActivity(intent)

        }

    }


    override fun getItemCount(): Int {
        return etkinlikList.size
    }


}
