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
import com.ocak.adminapp.model.IstasyonTanitim
import kotlinx.android.synthetic.main.activity_ulasim_list.*

class IstasyonListesiActivity : AppCompatActivity() {

    var istasyonArray = ArrayList<IstasyonTanitim>()

    private lateinit var database :FirebaseFirestore
    private  lateinit var recyclerAdapter: IstasyonAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ulasim_list)

        database= FirebaseFirestore.getInstance()

        verileriAl()


        val layoutManager = LinearLayoutManager(this)
        ulasimList.layoutManager = layoutManager
        recyclerAdapter = IstasyonAdapter(istasyonArray)
        ulasimList.adapter = recyclerAdapter


        swipeRefreshLayout2.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {

            verileriAl()

            swipeRefreshLayout2.isRefreshing = false

        })


    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.ulasimlist_menu,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.addistasyon){

            val intent = Intent(this, IstasyonOlusturActivity::class.java)
            startActivity(intent)

        }


        return super.onOptionsItemSelected(item)
    }

     fun verileriAl(){


         database.collection("Stations").addSnapshotListener { snapshot, exception ->

             if(exception != null ){
                 Toast.makeText(this,exception.localizedMessage,Toast.LENGTH_LONG).show()
             }else{

                 if(snapshot != null){

                     if(snapshot.isEmpty == false){

                         val documents = snapshot.documents

                         istasyonArray.clear()

                         for ( document in documents){


                             val name = document.get("stationad") as String
                             val gorsel = document.get("gorselstation") as String
                             val istasyonId =document.id

                             val indirilen = IstasyonTanitim(name,gorsel,istasyonId)

                             istasyonArray.add(indirilen)


                         }

                          recyclerAdapter.notifyDataSetChanged()


                     }

                 }


             }

         }


     }


}