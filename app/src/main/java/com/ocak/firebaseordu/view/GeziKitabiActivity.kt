package com.ocak.firebaseordu.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.ocak.firebaseordu.R
import com.ocak.firebaseordu.adapter.GeziKitabiAdapter
import com.ocak.firebaseordu.model.GeziKitabi
import kotlinx.android.synthetic.main.activity_gezi_kitabi.*

class GeziKitabiActivity : AppCompatActivity() {

    var geziKitabiDizisi = ArrayList<GeziKitabi>()

    private lateinit var database : FirebaseFirestore
    private  lateinit var recyclerAdapter: GeziKitabiAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gezi_kitabi)

        database= FirebaseFirestore.getInstance()

        verileriAl()


        val layoutManager = LinearLayoutManager(this)
        recyclerViewGezi.layoutManager = layoutManager
        recyclerAdapter = GeziKitabiAdapter(geziKitabiDizisi)
        recyclerViewGezi.adapter = recyclerAdapter


        swipeRefreshLayout3.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {


            verileriAl()

            swipeRefreshLayout3.isRefreshing = false

        })


    }

    fun verileriAl(){


        database.collection("GeziMekanlari").addSnapshotListener { snapshot, exception ->

            if(exception != null ){
                Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()
            }else{

                if(snapshot != null){

                    if(snapshot.isEmpty == false){

                        val documents = snapshot.documents

                        geziKitabiDizisi.clear()

                        for ( document in documents){


                            val name = document.get("mekanAd") as String
                            val gorsel = document.get("mekanGorseli") as String
                            val mekanId = document.id


                            val indirilen = GeziKitabi(name,gorsel,mekanId)

                            geziKitabiDizisi.add(indirilen)


                        }

                        recyclerAdapter.notifyDataSetChanged()


                    }

                }


            }

        }


    }


}