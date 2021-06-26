package com.ocak.adminapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.ocak.adminapp.R
import com.ocak.adminapp.model.Yemek
import com.ocak.adminapp.view.YemekDetayiActivity
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
            val intent = Intent(holder.itemView.context,YemekDetayiActivity::class.java)
            intent.putExtra("ad",yemekList.get(position).yemekAdi)
            intent.putExtra("id",yemekList.get(position).yemekId)
            holder.itemView.context.startActivity(intent)

        }


        holder.itemView.silYemekButonu.setOnClickListener {

            val string = yemekList.get(position).yemekId

            database.collection("Yemekler").document(string).delete().addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(holder.itemView.context, "Seçilen yemek başarıyla silindi", Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener {
                Toast.makeText(holder.itemView.context, it.localizedMessage, Toast.LENGTH_LONG).show()
            }



        }


    }


    override fun getItemCount(): Int {
        return yemekList.size
    }


}
