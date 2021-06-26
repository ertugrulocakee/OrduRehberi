package com.ocak.firebaseordu.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.ocak.firebaseordu.R
import com.ocak.firebaseordu.model.Muzik
import com.ocak.firebaseordu.view.MuzikDetayiActivity
import kotlinx.android.synthetic.main.recycler_row_muzik.view.*

class MuzikListesiAdapter (var muzikList : ArrayList<Muzik>) : RecyclerView.Adapter<MuzikListesiAdapter.MuzikListesiViewHolder>(){

    var database = FirebaseFirestore.getInstance()

    class MuzikListesiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MuzikListesiViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view= inflater.inflate(R.layout.recycler_row_muzik,parent,false)
        return MuzikListesiAdapter.MuzikListesiViewHolder(view)

    }

    override fun onBindViewHolder(holder: MuzikListesiViewHolder, position: Int) {

        holder.itemView.recycler_row_muzik_ad.text = muzikList.get(position).muzikAdi

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, MuzikDetayiActivity::class.java)
            intent.putExtra("ad",muzikList.get(position).muzikAdi)
            intent.putExtra("id",muzikList.get(position).muzikId)
            holder.itemView.context.startActivity(intent)

        }


    }


    override fun getItemCount(): Int {
        return muzikList.size
    }


}
