package com.ocak.firebaseordu.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.ocak.firebaseordu.R
import com.ocak.firebaseordu.model.Hayvan
import com.ocak.firebaseordu.view.HayvanIsiDetayiActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_view_hayvan.view.*


class HayvanListesiAdapter (var hayvanList : ArrayList<Hayvan>) : RecyclerView.Adapter<HayvanListesiAdapter.HayvanListesiViewHolder>(){

    var database = FirebaseFirestore.getInstance()

    class HayvanListesiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HayvanListesiViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view= inflater.inflate(R.layout.recycler_view_hayvan,parent,false)
        return HayvanListesiAdapter.HayvanListesiViewHolder(view)

    }

    override fun onBindViewHolder(holder: HayvanListesiViewHolder, position: Int) {

        holder.itemView.recycler_row_hayvan_ad.text = hayvanList.get(position).hayvanTuru
        Picasso.get().load(hayvanList.get(position).hayvanGorseli).into(holder.itemView.recycler_row_hayvan_resim)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, HayvanIsiDetayiActivity::class.java)
            intent.putExtra("ad",hayvanList.get(position).hayvanTuru)
            intent.putExtra("id",hayvanList.get(position).hayvanId)
            holder.itemView.context.startActivity(intent)

        }

    }


    override fun getItemCount(): Int {
        return hayvanList.size
    }


}

