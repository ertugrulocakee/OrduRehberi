package com.ocak.adminapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.ocak.adminapp.R
import com.ocak.adminapp.model.Spor
import com.ocak.adminapp.model.Yemek
import com.ocak.adminapp.view.SporDetayiActivity
import com.ocak.adminapp.view.YemekDetayiActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_row_spor.view.*
import kotlinx.android.synthetic.main.recycler_row_yemek.view.*
import kotlinx.android.synthetic.main.recycler_row_yemek.view.recycler_row_yemek_ad

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


        holder.itemView.silSporButonu.setOnClickListener {

            val string = sporList.get(position).sporId

            database.collection("Spor").document(string).delete().addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(holder.itemView.context, "Seçilen sporcu/takım başarıyla silindi", Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener {
                Toast.makeText(holder.itemView.context, it.localizedMessage, Toast.LENGTH_LONG).show()
            }



        }


    }


    override fun getItemCount(): Int {
        return sporList.size
    }


}
