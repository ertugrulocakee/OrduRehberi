package com.ocak.adminapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.ocak.adminapp.R
import com.ocak.adminapp.model.Hayvan
import com.ocak.adminapp.view.HayvanIsiDetayiActivity
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


        holder.itemView.silHayvanButonu.setOnClickListener {

            val string = hayvanList.get(position).hayvanId

            database.collection("Hayvanlar").document(string).delete().addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(holder.itemView.context, "Seçilen hayvan başarıyla silindi", Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener {
                Toast.makeText(holder.itemView.context, it.localizedMessage, Toast.LENGTH_LONG).show()
            }



        }


    }


    override fun getItemCount(): Int {
        return hayvanList.size
    }


}

