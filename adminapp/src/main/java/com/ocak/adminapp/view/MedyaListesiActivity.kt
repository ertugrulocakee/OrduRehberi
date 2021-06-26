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

import com.ocak.adminapp.adapter.MedyaListesiAdapter

import com.ocak.adminapp.model.Medya

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


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.medya_ekle,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.medyaEkle){

            val intent = Intent(this, MedyaOlusturActivity::class.java)
            startActivity(intent)

        }


        return super.onOptionsItemSelected(item)
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