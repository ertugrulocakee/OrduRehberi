package com.ocak.adminapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.ocak.adminapp.R

import com.ocak.adminapp.adapter.RestorantAdapter

import com.ocak.adminapp.model.Restorant
import kotlinx.android.synthetic.main.activity_restorant_listesi.*


class RestorantListesiActivity : AppCompatActivity() {

    var restorantListesi = ArrayList<Restorant>()

    private lateinit var database : FirebaseFirestore
    private  lateinit var recyclerAdapter: RestorantAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restorant_listesi)

        database= FirebaseFirestore.getInstance()

        verileriAl()


        val layoutManager = LinearLayoutManager(this)
        recyclerViewRestorant.layoutManager = layoutManager
        recyclerAdapter = RestorantAdapter(restorantListesi)
        recyclerViewRestorant.adapter = recyclerAdapter


        swipeRefreshLayout4.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {

            verileriAl()

            swipeRefreshLayout4.isRefreshing = false

        })


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.restorant_ekle,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.restorantEkle){

            val intent = Intent(this,RestorantOlusturActivity::class.java)
            startActivity(intent)

        }


        return super.onOptionsItemSelected(item)
    }


    fun verileriAl(){


        database.collection("Restorantlar").addSnapshotListener { snapshot, exception ->

            if(exception != null ){
                Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()
            }else{

                if(snapshot != null){

                    if(snapshot.isEmpty == false){

                        val documents = snapshot.documents

                        restorantListesi.clear()

                        for ( document in documents){


                            val name = document.get("restorantAdi") as String
                            val gorsel = document.get("restorantGorseli") as String
                            val restorantId =document.id

                            val indirilen = Restorant(name,gorsel,restorantId)

                             restorantListesi.add(indirilen)


                        }

                        recyclerAdapter.notifyDataSetChanged()


                    }

                }


            }

        }


    }



}