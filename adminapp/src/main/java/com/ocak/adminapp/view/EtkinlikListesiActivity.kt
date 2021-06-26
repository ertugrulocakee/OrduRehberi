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
import com.ocak.adminapp.adapter.EtkinlikListesiAdapter
import com.ocak.adminapp.model.Etkinlik
import kotlinx.android.synthetic.main.activity_kulturel_sanat_listesi.*

class EtkinlikListesiActivity : AppCompatActivity() {

    var etkinlikListesi = ArrayList<Etkinlik>()

    private lateinit var database : FirebaseFirestore
    private  lateinit var recyclerAdapter: EtkinlikListesiAdapter




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kulturel_sanat_listesi)

        database= FirebaseFirestore.getInstance()

        verileriAl()


        val layoutManager = LinearLayoutManager(this)
        recyclerViewKulturelSanat.layoutManager = layoutManager
        recyclerAdapter = EtkinlikListesiAdapter(etkinlikListesi)
        recyclerViewKulturelSanat.adapter = recyclerAdapter


        swipeRefreshLayout14.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {

            verileriAl()

            swipeRefreshLayout14.isRefreshing = false

        })





    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.sanat_ekle,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.sanatEkle){

            val intent = Intent(this, EtkinlikOlusturActivity::class.java)
            startActivity(intent)

        }


        return super.onOptionsItemSelected(item)
    }


    fun verileriAl(){


        database.collection("KulturSanatEtkinlikleri").addSnapshotListener { snapshot, exception ->

            if(exception != null ){
                Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()
            }else{

                if(snapshot != null){

                    if(snapshot.isEmpty == false){

                        val documents = snapshot.documents

                        etkinlikListesi.clear()

                        for ( document in documents){


                            val name = document.get("etkinlikad") as String
                            val gorsel = document.get("etkinlikgorsel") as String
                            val etkinlikId =document.id

                            val indirilen =Etkinlik(name,gorsel,etkinlikId)

                            etkinlikListesi.add(indirilen)


                        }

                        recyclerAdapter.notifyDataSetChanged()


                    }

                }


            }

        }


    }


}