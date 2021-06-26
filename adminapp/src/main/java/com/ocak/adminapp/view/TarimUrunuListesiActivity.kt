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
import com.ocak.adminapp.adapter.HayvanListesiAdapter
import com.ocak.adminapp.adapter.TarimUrunuListesiAdapter
import com.ocak.adminapp.model.Hayvan
import com.ocak.adminapp.model.TarimUrunu
import kotlinx.android.synthetic.main.activity_hayvan_listesi.*
import kotlinx.android.synthetic.main.activity_tarim_listesi.*
import java.util.ArrayList

class TarimUrunuListesiActivity : AppCompatActivity() {


    var tarimUrunuListesi = ArrayList<TarimUrunu>()

    private lateinit var database : FirebaseFirestore
    private  lateinit var recyclerAdapter: TarimUrunuListesiAdapter






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tarim_listesi)


        database = FirebaseFirestore.getInstance()

        verileriAl()


        val layoutManager = LinearLayoutManager(this)
        recyclerViewTarimUrunleri.layoutManager = layoutManager
        recyclerAdapter = TarimUrunuListesiAdapter(tarimUrunuListesi)
        recyclerViewTarimUrunleri.adapter = recyclerAdapter


        swipeRefreshLayout9.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {

            verileriAl()

            swipeRefreshLayout9.isRefreshing = false

        })


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.tarimurunu_ekle,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.tarimUrunEkle){

            val intent = Intent(this, TarimUrunuOlusturActivity::class.java)
            startActivity(intent)

        }


        return super.onOptionsItemSelected(item)
    }


    fun verileriAl(){


        database.collection("TarimUrunleri").addSnapshotListener { snapshot, exception ->

            if(exception != null ){
                Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()
            }else{

                if(snapshot != null){

                    if(snapshot.isEmpty == false){

                        val documents = snapshot.documents

                        tarimUrunuListesi.clear()

                        for ( document in documents){


                            val tur = document.get("tarimurunuturu") as String
                            val gorsel = document.get("tarimurunugorseli") as String
                            val tarimUrunuId =document.id

                            val indirilen = TarimUrunu(tur,gorsel,tarimUrunuId)

                            tarimUrunuListesi.add(indirilen)


                        }

                        recyclerAdapter.notifyDataSetChanged()


                    }

                }


            }

        }


    }






}