package com.ocak.adminapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.ocak.adminapp.R
import com.ocak.adminapp.model.TarimUrunu
import com.ocak.adminapp.view.TarimUrunuDetayiActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_row_tarimurunu.view.*

class TarimUrunuListesiAdapter (var tarimUrunuList : ArrayList<TarimUrunu>) : RecyclerView.Adapter<TarimUrunuListesiAdapter.TarimUrunuListesiViewHolder>(){

    var database = FirebaseFirestore.getInstance()

    class TarimUrunuListesiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TarimUrunuListesiViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view= inflater.inflate(R.layout.recycler_row_tarimurunu,parent,false)
        return TarimUrunuListesiAdapter.TarimUrunuListesiViewHolder(view)

    }

    override fun onBindViewHolder(holder: TarimUrunuListesiViewHolder, position: Int) {

        holder.itemView.recycler_row_tarimurunu_ad.text = tarimUrunuList.get(position).tarimTuru
        Picasso.get().load(tarimUrunuList.get(position).tarimUrunuGorseli).into(holder.itemView.recycler_row_tarimurunu_resim)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, TarimUrunuDetayiActivity::class.java)
            intent.putExtra("ad",tarimUrunuList.get(position).tarimTuru)
            intent.putExtra("id",tarimUrunuList.get(position).tarimUrunuId)
            holder.itemView.context.startActivity(intent)

        }


        holder.itemView.silTarimUrunuButonu.setOnClickListener {

            val string = tarimUrunuList.get(position).tarimUrunuId

            database.collection("TarimUrunleri").document(string).delete().addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(holder.itemView.context, "Seçilen tarım ürünü başarıyla silindi", Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener {
                Toast.makeText(holder.itemView.context, it.localizedMessage, Toast.LENGTH_LONG).show()
            }



        }


    }


    override fun getItemCount(): Int {
        return tarimUrunuList.size
    }


}

