package com.ocak.firebaseordu.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.ocak.firebaseordu.R
import com.ocak.firebaseordu.adapter.SanayiListesiAdapter
import com.ocak.firebaseordu.model.Sanayi
import kotlinx.android.synthetic.main.activity_sanayi_listesi.*

class SanayiListesiActivity : AppCompatActivity() {

    var sanayiListesi = ArrayList<Sanayi>()

    private lateinit var database : FirebaseFirestore
    private  lateinit var recyclerAdapter: SanayiListesiAdapter




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sanayi_listesi)

        database= FirebaseFirestore.getInstance()

        verileriAl()


        val layoutManager = LinearLayoutManager(this)
        recyclerViewSanayiListesi.layoutManager = layoutManager
        recyclerAdapter = SanayiListesiAdapter(sanayiListesi)
        recyclerViewSanayiListesi.adapter = recyclerAdapter


        swipeRefreshLayout13.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {

            verileriAl()

            swipeRefreshLayout13.isRefreshing = false

        })



    }


    fun verileriAl(){


        database.collection("SanayiFirmalari").addSnapshotListener { snapshot, exception ->

            if(exception != null ){
                Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()
            }else{

                if(snapshot != null){

                    if(snapshot.isEmpty == false){

                        val documents = snapshot.documents

                        sanayiListesi.clear()

                        for ( document in documents){


                            val name = document.get("sanayiad") as String
                            val gorsel = document.get("sanayigorsel") as String
                            val sanayiId =document.id

                            val indirilen = Sanayi(name,gorsel,sanayiId)

                            sanayiListesi.add(indirilen)


                        }

                        recyclerAdapter.notifyDataSetChanged()


                    }

                }


            }

        }


    }




}