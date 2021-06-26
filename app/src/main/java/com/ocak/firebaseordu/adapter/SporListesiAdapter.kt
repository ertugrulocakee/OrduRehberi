package com.ocak.firebaseordu.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.ocak.firebaseordu.R
import com.ocak.firebaseordu.model.Spor
import com.ocak.firebaseordu.view.SporDetayiActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_row_spor.view.*

class SporListesiAdapter (var sporList : ArrayList<Spor>) : RecyclerView.Adapter<SporListesiAdapter.SporListesiViewHolder>(){

    var database = FirebaseFirestore.getInstance()

    class SporListesiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SporListesiViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view= inflater.inflate(R.layout.recycler_row_spor,parent,false)
        return SporListesiAdapter.SporListesiViewHolder(view)

    }

    override fun onBindViewHolder(holder: SporListesiViewHolder, position: Int) {

        holder.itemView.recycler_row_spor_ad.text = sporList.get(position).sporAdi
        Picasso.get().load(sporList.get(position).sporGorseli).into(holder.itemView.recycler_row_spor_resim)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, SporDetayiActivity::class.java)
            intent.putExtra("ad",sporList.get(position).sporAdi)
            intent.putExtra("id",sporList.get(position).sporId)
            holder.itemView.context.startActivity(intent)

        }


    }


    override fun getItemCount(): Int {
        return sporList.size
    }


}
