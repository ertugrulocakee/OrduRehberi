package com.ocak.firebaseordu.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.ocak.firebaseordu.R
import com.ocak.firebaseordu.adapter.OtelAdapter
import com.ocak.firebaseordu.model.Otel
import kotlinx.android.synthetic.main.activity_otel_listesi.*

class OtelListesiActivity : AppCompatActivity() {


    var otelListesi = ArrayList<Otel>()


    private lateinit var database : FirebaseFirestore
    private  lateinit var recyclerAdapter: OtelAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otel_listesi)


        database= FirebaseFirestore.getInstance()

        verileriAl()

        val layoutManager = LinearLayoutManager(this)
        recyclerViewOtelListesi.layoutManager = layoutManager
        recyclerAdapter = OtelAdapter(otelListesi)
        recyclerViewOtelListesi.adapter = recyclerAdapter


        swipeRefreshLayout5.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {

            verileriAl()

            swipeRefreshLayout5.isRefreshing = false

        })


    }


    fun verileriAl(){


        database.collection("Oteller").addSnapshotListener { snapshot, exception ->

            if(exception != null ){
                Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()
            }else{

                if(snapshot != null){

                    if(snapshot.isEmpty == false){

                        val documents = snapshot.documents

                        otelListesi.clear()

                        for ( document in documents){


                            val name = document.get("otelAdi") as String
                            val gorsel = document.get("otelGorseli") as String
                            val otelId =document.id

                            val indirilen = Otel(name,gorsel,otelId)

                            otelListesi.add(indirilen)


                        }

                        recyclerAdapter.notifyDataSetChanged()


                    }

                }


            }

        }


    }






}

