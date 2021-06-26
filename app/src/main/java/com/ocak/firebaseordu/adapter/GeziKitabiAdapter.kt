package com.ocak.firebaseordu.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.ocak.firebaseordu.R
import com.ocak.firebaseordu.model.GeziKitabi
import com.ocak.firebaseordu.view.MekanDetayiActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_row_gezikitabi.view.*


class GeziKitabiAdapter(var geziKitabiDizisi : ArrayList<GeziKitabi>) : RecyclerView.Adapter<GeziKitabiAdapter.GeziKitabiViewHolder>(){

    private lateinit var database : FirebaseFirestore

    class GeziKitabiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){




    }

    override fun onBindViewHolder(holder: GeziKitabiViewHolder, position: Int) {

        database= FirebaseFirestore.getInstance()

        holder.itemView.recycler_row_mekanad.text = geziKitabiDizisi.get(position).mekanAdi
        Picasso.get().load(geziKitabiDizisi.get(position).mekanGorseli).into(holder.itemView.recycler_row_mekanresim)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, MekanDetayiActivity::class.java)
            intent.putExtra("ad",geziKitabiDizisi.get(position).mekanAdi)
            intent.putExtra("id",geziKitabiDizisi.get(position).mekanId)
            holder.itemView.context.startActivity(intent)

        }

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GeziKitabiViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val view= inflater.inflate(R.layout.recycler_row_gezikitabi,parent,false)
        return GeziKitabiViewHolder(view)


    }

    override fun getItemCount(): Int {
        return  geziKitabiDizisi.size
    }


}