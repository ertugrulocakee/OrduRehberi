package com.ocak.firebaseordu.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.ocak.firebaseordu.R
import com.ocak.firebaseordu.model.Yemek
import com.ocak.firebaseordu.view.YemekDetayiActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_row_yemek.view.*


class YemekListesiAdapter (var yemekList : ArrayList<Yemek>) : RecyclerView.Adapter<YemekListesiAdapter.YemekListesiViewHolder>(){

    var database = FirebaseFirestore.getInstance()

    class YemekListesiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YemekListesiViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view= inflater.inflate(R.layout.recycler_row_yemek,parent,false)
        return YemekListesiAdapter.YemekListesiViewHolder(view)

    }

    override fun onBindViewHolder(holder: YemekListesiViewHolder, position: Int) {

        holder.itemView.recycler_row_yemek_ad.text = yemekList.get(position).yemekAdi
        Picasso.get().load(yemekList.get(position).yemekGorsel).into(holder.itemView.recycler_row_yemek_resim)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, YemekDetayiActivity::class.java)
            intent.putExtra("ad",yemekList.get(position).yemekAdi)
            intent.putExtra("id",yemekList.get(position).yemekId)
            holder.itemView.context.startActivity(intent)

        }


    }


    override fun getItemCount(): Int {
        return yemekList.size
    }


}
