package com.ocak.firebaseordu.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.ocak.firebaseordu.R
import com.ocak.firebaseordu.model.Sanayi
import com.ocak.firebaseordu.view.SanayiDetayiActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_row_sanayi.view.*

class SanayiListesiAdapter (var sanayiList : ArrayList<Sanayi>) : RecyclerView.Adapter<SanayiListesiAdapter.SanayiListesiViewHolder>(){

    var database = FirebaseFirestore.getInstance()

    class SanayiListesiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SanayiListesiViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view= inflater.inflate(R.layout.recycler_row_sanayi,parent,false)
        return SanayiListesiAdapter.SanayiListesiViewHolder(view)

    }

    override fun onBindViewHolder(holder: SanayiListesiViewHolder, position: Int) {

        holder.itemView.recycler_row_sanayi_ad.text = sanayiList.get(position).sanayiAdi
        Picasso.get().load(sanayiList.get(position).sanayiGorseli).into(holder.itemView.recycler_row_sanayi_resim)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, SanayiDetayiActivity::class.java)
            intent.putExtra("ad",sanayiList.get(position).sanayiAdi)
            intent.putExtra("id",sanayiList.get(position).sanayiId)
            holder.itemView.context.startActivity(intent)

        }



    }


    override fun getItemCount(): Int {
        return sanayiList.size
    }


}

