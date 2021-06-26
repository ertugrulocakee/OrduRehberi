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
import com.ocak.adminapp.adapter.OkulListesiAdapter
import com.ocak.adminapp.model.Okul
import kotlinx.android.synthetic.main.activity_egitim_listesi.*

class OkulListesiActivity : AppCompatActivity() {


    var okulListesi = ArrayList<Okul>()

    private lateinit var database : FirebaseFirestore
    private  lateinit var recyclerAdapter: OkulListesiAdapter




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_egitim_listesi)


        database= FirebaseFirestore.getInstance()

        verileriAl()


        val layoutManager = LinearLayoutManager(this)
        recyclerViewEgitim.layoutManager = layoutManager
        recyclerAdapter = OkulListesiAdapter(okulListesi)
        recyclerViewEgitim.adapter = recyclerAdapter


        swipeRefreshLayout15.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {

            verileriAl()

            swipeRefreshLayout15.isRefreshing = false

        })



    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.egitim_ekle,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.egitimEkle){

            val intent = Intent(this, OkulOlusturActivity::class.java)
            startActivity(intent)

        }


        return super.onOptionsItemSelected(item)
    }


    fun verileriAl(){


        database.collection("Okullar").addSnapshotListener { snapshot, exception ->

            if(exception != null ){
                Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()
            }else{

                if(snapshot != null){

                    if(snapshot.isEmpty == false){

                        val documents = snapshot.documents

                        okulListesi.clear()

                        for ( document in documents){


                            val name = document.get("okulad") as String
                            val gorsel = document.get("okulgorsel") as String
                            val okulId =document.id

                            val indirilen = Okul(name,gorsel,okulId)

                            okulListesi.add(indirilen)


                        }

                        recyclerAdapter.notifyDataSetChanged()


                    }

                }


            }

        }


    }






}