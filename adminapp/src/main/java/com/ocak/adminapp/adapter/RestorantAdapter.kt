package com.ocak.adminapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.ocak.adminapp.R
import com.ocak.adminapp.model.Restorant

import com.ocak.adminapp.view.RestorantDetayiActivity
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.recyler_row_restorant.view.*

class RestorantAdapter(var restorantListesi:ArrayList<Restorant>):RecyclerView.Adapter<RestorantAdapter.RestorantViewHolder>() {
     var database = FirebaseFirestore.getInstance()

    class  RestorantViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestorantViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view= inflater.inflate(R.layout.recyler_row_restorant,parent,false)
        return RestorantViewHolder(view)

    }

    override fun onBindViewHolder(holder: RestorantViewHolder, position: Int) {


        holder.itemView.recycler_row_restorantad.text = restorantListesi.get(position).restorantAdi
        Picasso.get().load(restorantListesi.get(position).restorantGorseli).into(holder.itemView.recycler_row_restorantresim)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, RestorantDetayiActivity::class.java)
            intent.putExtra("ad",restorantListesi.get(position).restorantAdi)
            intent.putExtra("id",restorantListesi.get(position).restorantId)
            holder.itemView.context.startActivity(intent)

        }


        holder.itemView.silRestorantiButonu.setOnClickListener {

            val string = restorantListesi.get(position).restorantId

            database.collection("Restorantlar").document(string).delete().addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(holder.itemView.context, "Seçilen restorant başarıyla silindi", Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener {
                Toast.makeText(holder.itemView.context, it.localizedMessage, Toast.LENGTH_LONG).show()
            }



        }



    }

    override fun getItemCount(): Int {
        return  restorantListesi.size
    }


}