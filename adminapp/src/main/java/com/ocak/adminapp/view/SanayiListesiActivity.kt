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
import com.ocak.adminapp.adapter.IstasyonAdapter
import com.ocak.adminapp.adapter.SanayiListesiAdapter
import com.ocak.adminapp.model.IstasyonTanitim
import com.ocak.adminapp.model.Sanayi
import kotlinx.android.synthetic.main.activity_sanayi_listesi.*
import kotlinx.android.synthetic.main.activity_ulasim_list.*

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


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.sanayi_ekle,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.sanayiEkle){

            val intent = Intent(this, SanayiOlusturActivity::class.java)
            startActivity(intent)

        }


        return super.onOptionsItemSelected(item)
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