package com.ocak.firebaseordu.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.ocak.firebaseordu.R
import com.ocak.firebaseordu.model.Yetkili
import com.ocak.firebaseordu.view.YetkiliDetayiActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_row_yetkili.view.*

class YetkiliListesiAdapter (var yetkiliList : ArrayList<Yetkili>) : RecyclerView.Adapter<YetkiliListesiAdapter.YetkiliListesiViewHolder>(){

    var database = FirebaseFirestore.getInstance()

    class YetkiliListesiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YetkiliListesiViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view= inflater.inflate(R.layout.recycler_row_yetkili,parent,false)
        return YetkiliListesiAdapter.YetkiliListesiViewHolder(view)

    }

    override fun onBindViewHolder(holder: YetkiliListesiViewHolder, position: Int) {

        holder.itemView.recycler_row_yetkili_ad.text = yetkiliList.get(position).yetkiliAdi
        Picasso.get().load(yetkiliList.get(position).yetkiliGorseli).into(holder.itemView.recycler_row_yetkili_resim)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, YetkiliDetayiActivity::class.java)
            intent.putExtra("ad",yetkiliList.get(position).yetkiliAdi)
            intent.putExtra("id",yetkiliList.get(position).yetkiliId)
            holder.itemView.context.startActivity(intent)

        }


    }


    override fun getItemCount(): Int {
        return yetkiliList.size
    }


}
