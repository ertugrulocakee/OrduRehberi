package com.ocak.firebaseordu.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.ocak.firebaseordu.R
import com.ocak.firebaseordu.model.Okul
import com.ocak.firebaseordu.view.OkulDetayiActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_row_okul.view.*

class OkulListesiAdapter(var okulList : ArrayList<Okul>) : RecyclerView.Adapter<OkulListesiAdapter.OkulListesiViewHolder>(){

    var database = FirebaseFirestore.getInstance()

    class OkulListesiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OkulListesiViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view= inflater.inflate(R.layout.recycler_row_okul,parent,false)
        return OkulListesiAdapter.OkulListesiViewHolder(view)

    }

    override fun onBindViewHolder(holder: OkulListesiViewHolder, position: Int) {

        holder.itemView.recycler_row_okul_ad.text = okulList.get(position).okulAdi
        Picasso.get().load(okulList.get(position).okulGorseli).into(holder.itemView.recycler_row_okul_resim)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, OkulDetayiActivity::class.java)
            intent.putExtra("ad",okulList.get(position).okulAdi)
            intent.putExtra("id",okulList.get(position).okulId)
            holder.itemView.context.startActivity(intent)

        }


    }


    override fun getItemCount(): Int {
        return okulList.size
    }


}

