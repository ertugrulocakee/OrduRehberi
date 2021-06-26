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
import com.ocak.firebaseordu.adapter.MedyaListesiAdapter
import com.ocak.firebaseordu.model.Medya
import kotlinx.android.synthetic.main.activity_medya_listesi.*
import java.util.ArrayList

class MedyaListesiActivity : AppCompatActivity() {


    var medyaListesi = ArrayList<Medya>()

    private lateinit var database : FirebaseFirestore
    private  lateinit var recyclerAdapter: MedyaListesiAdapter




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medya_listesi)



        database = FirebaseFirestore.getInstance()

        verileriAl()


        val layoutManager = LinearLayoutManager(this)
        recyclerViewMedyalar.layoutManager = layoutManager
        recyclerAdapter = MedyaListesiAdapter(medyaListesi)
        recyclerViewMedyalar.adapter = recyclerAdapter


        swipeRefreshLayout10.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {

            verileriAl()

            swipeRefreshLayout10.isRefreshing = false

        })

    }


    fun verileriAl(){


        database.collection("Medyalar").addSnapshotListener { snapshot, exception ->

            if(exception != null ){
                Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()
            }else{

                if(snapshot != null){

                    if(snapshot.isEmpty == false){

                        val documents = snapshot.documents

                        medyaListesi.clear()

                        for ( document in documents){


                            val ad = document.get("medyaadi") as String
                            val gorsel = document.get("medyagorseli") as String
                            val medyaId =document.id

                            val indirilen = Medya(ad,gorsel,medyaId)

                            medyaListesi.add(indirilen)


                        }

                        recyclerAdapter.notifyDataSetChanged()


                    }

                }


            }

        }


    }



}