package com.ocak.firebaseordu.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.ocak.firebaseordu.R
import com.ocak.firebaseordu.adapter.HayvanListesiAdapter
import com.ocak.firebaseordu.model.Hayvan
import kotlinx.android.synthetic.main.activity_hayvan_listesi.*
import java.util.ArrayList

class HayvanIsiListesiActivity : AppCompatActivity() {



    var hayvanListesi = ArrayList<Hayvan>()

    private lateinit var database : FirebaseFirestore
    private  lateinit var recyclerAdapter: HayvanListesiAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hayvan_listesi)


        database = FirebaseFirestore.getInstance()

        verileriAl()


        val layoutManager = LinearLayoutManager(this)
        recyclerViewHayvanlar.layoutManager = layoutManager
        recyclerAdapter = HayvanListesiAdapter(hayvanListesi)
        recyclerViewHayvanlar.adapter = recyclerAdapter


        swipeRefreshLayout8.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {

            verileriAl()

            swipeRefreshLayout8.isRefreshing = false

        })


    }


    fun verileriAl(){


        database.collection("Hayvanlar").addSnapshotListener { snapshot, exception ->

            if(exception != null ){
                Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()
            }else{

                if(snapshot != null){

                    if(snapshot.isEmpty == false){

                        val documents = snapshot.documents

                        hayvanListesi.clear()

                        for ( document in documents){


                            val tur = document.get("hayvanturu") as String
                            val gorsel = document.get("hayvangorseli") as String
                            val hayvanId =document.id

                            val indirilen = Hayvan(tur,gorsel,hayvanId)

                            hayvanListesi.add(indirilen)


                        }

                        recyclerAdapter.notifyDataSetChanged()


                    }

                }


            }

        }


    }




}