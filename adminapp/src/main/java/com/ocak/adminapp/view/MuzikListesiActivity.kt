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
import com.ocak.adminapp.adapter.MuzikListesiAdapter
import com.ocak.adminapp.adapter.YemekListesiAdapter
import com.ocak.adminapp.model.Muzik
import com.ocak.adminapp.model.Yemek
import kotlinx.android.synthetic.main.activity_muzik_listesi.*
import kotlinx.android.synthetic.main.activity_yemek_listesi.*

class MuzikListesiActivity : AppCompatActivity() {


    var muzikListesi = ArrayList<Muzik>()

    private lateinit var database : FirebaseFirestore
    private  lateinit var recyclerAdapter: MuzikListesiAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_muzik_listesi)


        database= FirebaseFirestore.getInstance()

        verileriAl()


        val layoutManager = LinearLayoutManager(this)
        recyclerViewMuzik.layoutManager = layoutManager
        recyclerAdapter = MuzikListesiAdapter(muzikListesi)
        recyclerViewMuzik.adapter = recyclerAdapter


        swipeRefreshLayout7.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {

            verileriAl()

            swipeRefreshLayout7.isRefreshing = false

        })


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.muzik_ekle,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.muzikEkle){

            val intent = Intent(this, MuzikEkleActivity ::class.java)
            startActivity(intent)

        }


        return super.onOptionsItemSelected(item)
    }


    fun verileriAl(){


        database.collection("Muzik").addSnapshotListener { snapshot, exception ->

            if(exception != null ){
                Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()
            }else{

                if(snapshot != null){

                    if(snapshot.isEmpty == false){

                        val documents = snapshot.documents

                        muzikListesi.clear()

                        for ( document in documents){


                            val name = document.get("muzikadi") as String
                            val muzikId =document.id

                            val indirilen = Muzik(name,muzikId)

                            muzikListesi.add(indirilen)


                        }

                        recyclerAdapter.notifyDataSetChanged()


                    }

                }


            }

        }


    }


}
