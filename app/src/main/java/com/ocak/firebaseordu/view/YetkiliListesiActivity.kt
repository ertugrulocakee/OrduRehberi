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
import com.ocak.firebaseordu.adapter.YetkiliListesiAdapter
import com.ocak.firebaseordu.model.Yetkili
import kotlinx.android.synthetic.main.activity_yetkili_listesi.*

class YetkiliListesiActivity : AppCompatActivity() {

    var yetkiliListesi = ArrayList<Yetkili>()

    private lateinit var database : FirebaseFirestore
    private  lateinit var recyclerAdapter: YetkiliListesiAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yetkili_listesi)

        database= FirebaseFirestore.getInstance()

        verileriAl()


        val layoutManager = LinearLayoutManager(this)
        recyclerViewYetkili.layoutManager = layoutManager
        recyclerAdapter = YetkiliListesiAdapter(yetkiliListesi)
        recyclerViewYetkili.adapter = recyclerAdapter


        swipeRefreshLayout12.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {

            verileriAl()

            swipeRefreshLayout12.isRefreshing = false

        })


    }

    fun verileriAl(){


        database.collection("Yetkililer").addSnapshotListener { snapshot, exception ->

            if(exception != null ){
                Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()
            }else{

                if(snapshot != null){

                    if(snapshot.isEmpty == false){

                        val documents = snapshot.documents

                        yetkiliListesi.clear()

                        for ( document in documents){


                            val name = document.get("yetkiliadi") as String
                            val gorsel = document.get("yetkiligorseli") as String
                            val yetkiliId =document.id

                            val indirilen = Yetkili(name,gorsel,yetkiliId)

                            yetkiliListesi.add(indirilen)


                        }

                        recyclerAdapter.notifyDataSetChanged()


                    }

                }


            }

        }


    }


}