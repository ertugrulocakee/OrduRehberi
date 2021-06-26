package com.ocak.firebaseordu.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.ocak.firebaseordu.R
import com.ocak.firebaseordu.model.IstasyonTanitim
import com.ocak.firebaseordu.view.IstasyonDetayActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_istasyon.view.*


class IstasyonAdapter(var istasyonDizisi : ArrayList<IstasyonTanitim>) : RecyclerView.Adapter<IstasyonAdapter.IstasyonViewHolder>(){
    private lateinit var database : FirebaseFirestore

    class IstasyonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IstasyonViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val view= inflater.inflate(R.layout.recycler_istasyon,parent,false)
        return IstasyonViewHolder(view)

    }

    override fun getItemCount(): Int {

        return istasyonDizisi.size
    }

    override fun onBindViewHolder(holder: IstasyonViewHolder, position: Int) {

        database= FirebaseFirestore.getInstance()

        holder.itemView.recycler_istasyon_ad.text = istasyonDizisi.get(position).istasyonAdi
        Picasso.get().load(istasyonDizisi.get(position).istasyonResmi).into(holder.itemView.recycler_istasyon_resim)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, IstasyonDetayActivity::class.java)
            intent.putExtra("ad",istasyonDizisi.get(position).istasyonAdi)
            intent.putExtra("id",istasyonDizisi.get(position).istasyonId)
            holder.itemView.context.startActivity(intent)

        }


    }


}