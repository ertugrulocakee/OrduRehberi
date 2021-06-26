package com.ocak.adminapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.ocak.adminapp.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_hayvan_detayi.*
import kotlinx.android.synthetic.main.activity_tarim_urunu_detayi.*

class TarimUrunuDetayiActivity : AppCompatActivity() {


    var secilenTarimUrunu = ""
    var tarimUrunuId = ""

    private lateinit var database: FirebaseFirestore



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tarim_urunu_detayi)

        database = FirebaseFirestore.getInstance()

        val intent = getIntent()
        secilenTarimUrunu = intent.getStringExtra("ad").toString()
        tarimUrunuId= intent.getStringExtra("id").toString()


        verileriAl()



    }


    fun verileriAl() {



        database.collection("TarimUrunleri").whereEqualTo("tarimurunuturu",secilenTarimUrunu).addSnapshotListener { snapshot, exception ->

            if (exception != null){

                Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()

            }else{

                if (snapshot != null){

                    if(snapshot.isEmpty == false){


                        val documents = snapshot.documents

                        for( document in documents){


                            val tarimUrunuGorseli = document.get("tarimurunugorseli") as String
                            val tarimUrunuTuru = document.get("tarimurunuturu") as String
                            val tarimUrunuBakimi = document.get("tarimurunubakimi") as String


                            tarim_detayi_turu.text="Tarım ürününün türü:"+" "+tarimUrunuTuru
                            tarim_detayi_bakim.text=tarimUrunuBakimi
                            Picasso.get().load(tarimUrunuGorseli).into(tarim_detayi_resim)


                        }


                    }
                }
            }



        }



    }


}