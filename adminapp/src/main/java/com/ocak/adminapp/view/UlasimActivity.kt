package com.ocak.adminapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.ocak.adminapp.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_ulasim.*

class UlasimActivity : AppCompatActivity() {

    private val dokuman = "N06fB0gksEIAZuEPSf8d"

    private lateinit var database: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ulasim)


        database = FirebaseFirestore.getInstance()

        verileriAl()



    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.ulasim_guncelle,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.ulasimGuncelle){

            val intent = Intent(this, UlasimOlusturGuncelleActivity::class.java)
            startActivity(intent)

        }


        return super.onOptionsItemSelected(item)
    }


    fun verileriAl(){


        database.collection("Ulasim").document(dokuman).addSnapshotListener { snapshot, exception ->

            if (exception != null){

                Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()

            }else{

                if (snapshot != null){


                     val nasilGidilirYazisi = snapshot.getString("ulasimyazisi")
                     val nasilGidilirGorseli =snapshot.getString("ulasimgorseli")


                    orduNasilGidilirText.text = nasilGidilirYazisi
                    Picasso.get().load(nasilGidilirGorseli).into(orduNasilGidilirResim)


                }


                    }
                }
            }


    fun ulasimIstasyonlarinaBak(view: View){

        val intent = Intent(this,IstasyonListesiActivity::class.java)
        this.startActivity(intent)


    }


}