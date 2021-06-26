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

import com.ocak.adminapp.adapter.YemekListesiAdapter

import com.ocak.adminapp.model.Yemek

import kotlinx.android.synthetic.main.activity_yemek_listesi.*

class YemekListesiActivity : AppCompatActivity() {

    var yemekListesi = ArrayList<Yemek>()

    private lateinit var database : FirebaseFirestore
    private  lateinit var recyclerAdapter: YemekListesiAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yemek_listesi)

        database= FirebaseFirestore.getInstance()

        verileriAl()


        val layoutManager = LinearLayoutManager(this)
        recyclerViewYemekler.layoutManager = layoutManager
        recyclerAdapter = YemekListesiAdapter(yemekListesi)
        recyclerViewYemekler.adapter = recyclerAdapter


        swipeRefreshLayout6.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {

            verileriAl()

            swipeRefreshLayout6.isRefreshing = false

        })

    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.yemek_ekle,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.yemekEkle){

            val intent = Intent(this, YemekOlusturActivity::class.java)
            startActivity(intent)

        }


        return super.onOptionsItemSelected(item)
    }

    fun verileriAl(){


        database.collection("Yemekler").addSnapshotListener { snapshot, exception ->

            if(exception != null ){
                Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()
            }else{

                if(snapshot != null){

                    if(snapshot.isEmpty == false){

                        val documents = snapshot.documents

                        yemekListesi.clear()

                        for ( document in documents){


                            val name = document.get("yemekadi") as String
                            val gorsel = document.get("yemekgorseli") as String
                            val yemekId =document.id

                            val indirilen = Yemek(name,gorsel,yemekId)

                            yemekListesi.add(indirilen)


                        }

                        recyclerAdapter.notifyDataSetChanged()


                    }

                }


            }

        }


    }

}