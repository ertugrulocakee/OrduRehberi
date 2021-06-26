package com.ocak.adminapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.ocak.adminapp.R
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.activity_medya_detayi.*

class MedyaDetayiActivity : AppCompatActivity() {

    var secilenMedya = ""
    var medyaId = ""
    var medyaWeb=""
    var medyaTuru=""

    private lateinit var database: FirebaseFirestore



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medya_detayi)

        medya_detayi_frekans.visibility = View.GONE

        database = FirebaseFirestore.getInstance()

        val intent = getIntent()

        secilenMedya = intent.getStringExtra("ad").toString()
        medyaId= intent.getStringExtra("id").toString()


        verileriAl()

    }

    fun verileriAl() {

        database.collection("Medyalar").whereEqualTo("medyaadi",secilenMedya).addSnapshotListener { snapshot, exception ->

            if (exception != null){

                Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()

            }else{

                if (snapshot != null){

                    if(snapshot.isEmpty == false){


                        val documents = snapshot.documents

                        for( document in documents){


                            val medyaGorseli = document.get("medyagorseli") as String
                            medyaTuru = document.get("medyaturu") as String
                            val medyaAdi = document.get("medyaadi") as String
                            val medyaFrekans=  document.get("medyafrekans") as String
                            val medyaAciklama = document.get("medyaaciklama") as String
                            medyaWeb= document.get("medyaweb") as String

                            if (medyaTuru == "Televizyon" || medyaTuru == "Radyo")
                            {
                                medya_detayi_frekans.visibility = View.VISIBLE
                            }


                            medya_detayi_turu.text="Medyanın türü:"+" "+medyaTuru
                            medya_detayi_ad.text=medyaAdi
                            medya_detayi_frekans.text="$medyaAdi"+" "+medyaFrekans
                            medya_detayi_aciklama.text=medyaAciklama

                            Picasso.get().load(medyaGorseli).into(medya_detayi_resim)


                        }


                    }
                }
            }



        }

    }


    fun medyaWebeGit(view: View){

        val intent = Intent(this,WebActivity::class.java)
        intent.putExtra("web",medyaWeb)
        startActivity(intent)


    }





}