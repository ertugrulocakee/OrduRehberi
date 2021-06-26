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
import com.ocak.adminapp.adapter.SporListesiAdapter
import com.ocak.adminapp.adapter.YemekListesiAdapter
import com.ocak.adminapp.model.Spor
import com.ocak.adminapp.model.Yemek
import kotlinx.android.synthetic.main.activity_spor_listesi.*
import kotlinx.android.synthetic.main.activity_yemek_listesi.*

class SporListesiActivity : AppCompatActivity() {

    var sporListesi = ArrayList<Spor>()

    private lateinit var database : FirebaseFirestore
    private  lateinit var recyclerAdapter: SporListesiAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spor_listesi)

        database= FirebaseFirestore.getInstance()

        verileriAl()


        val layoutManager = LinearLayoutManager(this)
        recyclerViewSporListesi.layoutManager = layoutManager
        recyclerAdapter = SporListesiAdapter(sporListesi)
        recyclerViewSporListesi.adapter = recyclerAdapter


        swipeRefreshLayout11.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {

            verileriAl()

            swipeRefreshLayout11.isRefreshing = false

        })


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.spor_ekle,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.sporEkle){

            val intent = Intent(this, SporOlusturActivity::class.java)
            startActivity(intent)

        }


        return super.onOptionsItemSelected(item)
    }

    fun verileriAl(){


        database.collection("Spor").addSnapshotListener { snapshot, exception ->

            if(exception != null ){
                Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()
            }else{

                if(snapshot != null){

                    if(snapshot.isEmpty == false){

                        val documents = snapshot.documents

                        sporListesi.clear()

                        for ( document in documents){


                            val name = document.get("sporadi") as String
                            val gorsel = document.get("sporgorseli") as String
                            val sporId =document.id

                            val indirilen = Spor(name,gorsel,sporId)

                            sporListesi.add(indirilen)


                        }

                        recyclerAdapter.notifyDataSetChanged()


                    }

                }


            }

        }


    }


}