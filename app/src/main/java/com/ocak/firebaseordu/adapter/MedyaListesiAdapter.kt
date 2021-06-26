package com.ocak.firebaseordu.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.ocak.firebaseordu.R
import com.ocak.firebaseordu.model.Medya
import com.ocak.firebaseordu.view.MedyaDetayiActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.reycler_row_medya.view.*

class MedyaListesiAdapter (var medyaList : ArrayList<Medya>) : RecyclerView.Adapter<MedyaListesiAdapter.MedyaListesiViewHolder>(){

    var database = FirebaseFirestore.getInstance()

    class MedyaListesiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedyaListesiViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view= inflater.inflate(R.layout.reycler_row_medya,parent,false)
        return MedyaListesiAdapter.MedyaListesiViewHolder(view)

    }

    override fun onBindViewHolder(holder: MedyaListesiViewHolder, position: Int) {

        holder.itemView.recycler_row_medya_ad.text = medyaList.get(position).medyaAdi
        Picasso.get().load(medyaList.get(position).medyaGorsel).into(holder.itemView.recycler_row_medya_resim)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, MedyaDetayiActivity::class.java)
            intent.putExtra("ad",medyaList.get(position).medyaAdi)
            intent.putExtra("id",medyaList.get(position).medyaId)
            holder.itemView.context.startActivity(intent)

        }


    }


    override fun getItemCount(): Int {
        return medyaList.size
    }


}
