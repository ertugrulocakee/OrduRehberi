package com.ocak.firebaseordu.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ocak.firebaseordu.model.Post
import com.ocak.firebaseordu.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_row_share.view.*
import kotlin.collections.ArrayList

class OrduDeneyimleriRecyclerAdapter : RecyclerView.Adapter<OrduDeneyimleriRecyclerAdapter.PaylasimlarVH> {

    private lateinit  var auth : FirebaseAuth
    private lateinit var database :FirebaseFirestore
    var postList: ArrayList<Post>
    private val mContext : Context


    constructor(mContext: Context, postList: ArrayList<Post>){

        this.postList =postList
        this.mContext = mContext
    }

    class PaylasimlarVH(itemView: View) :  RecyclerView.ViewHolder(itemView){


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaylasimlarVH {
        val inflater = LayoutInflater.from(parent.context)
        val view= inflater.inflate(R.layout.recycler_row_share,parent,false)

        return PaylasimlarVH(view)

    }

    override fun onBindViewHolder(holder: PaylasimlarVH, position: Int) {

        auth= FirebaseAuth.getInstance()
        database= FirebaseFirestore.getInstance()

        holder.itemView.onayrecycler.text = postList.get(position).kullaniciOnay
        holder.itemView.kullaniciadirecycler.text=postList.get(position).kullaniciAdi
        holder.itemView.puanrecycler.text=postList.get(position).kullaniciPuan
        holder.itemView.yorumrecycler.text=postList.get(position).kullaniciYorum
        Picasso.get().load(postList.get(position).gorselUrl).into(holder.itemView.gorselrecycler)
        Picasso.get().load(postList.get(position).kullaniciGorseli).into(holder.itemView.userPicture)



        val guncelKullanici = auth.currentUser?.uid.toString()
        if (guncelKullanici.equals(postList.get(position).kullaniciUid)) {

            holder.itemView.recycler_guncelle.visibility = View.VISIBLE

            holder.itemView.recycler_guncelle.setOnClickListener {

                val popupMenu = PopupMenu(mContext,it)
                popupMenu.setOnMenuItemClickListener { item  ->

                    when(item.itemId){
                        R.id.sildeneyimitem -> {
                            silItemi(position)
                            true
                        }


                        else -> false

                    }

                }

                popupMenu.menuInflater.inflate(R.menu.deneyim_menu,popupMenu.menu)
                popupMenu.show()

            }



        }else{

            holder.itemView.recycler_guncelle.visibility = View.GONE
        }


    }



    override fun getItemCount(): Int {

        return  postList.size
    }

    private fun silItemi(position: Int){

        val string = postList.get(position).postId

        database.collection("Post").document(string).delete().addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(mContext, "Seçilen deneyim başarıyla silindi", Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener {
            Toast.makeText(mContext, it.localizedMessage, Toast.LENGTH_LONG).show()
        }

    }


}








