package com.ocak.firebaseordu.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.ocak.firebaseordu.R
import com.ocak.firebaseordu.adapter.IstasyonYorumlariAdapter
import com.ocak.firebaseordu.model.IstasyonYorumlari
import kotlinx.android.synthetic.main.activity_istasyon_yorumlari.*

class IstasyonYorumlariActivity : AppCompatActivity() {

    var istasyonYorumlariListesi = ArrayList<IstasyonYorumlari>()
    private lateinit var database : FirebaseFirestore
    private  lateinit var recyclerAdapter: IstasyonYorumlariAdapter
    var istasyonDokumanId= ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_istasyon_yorumlari)

        database= FirebaseFirestore.getInstance()

        istasyonDokumanId = intent.getStringExtra("istasyonId").toString()

        verileriAl()


        val layoutManager = LinearLayoutManager(this)
        recyclerViewIstasyonYorumlari.layoutManager = layoutManager
        recyclerAdapter = IstasyonYorumlariAdapter(istasyonYorumlariListesi)
        recyclerViewIstasyonYorumlari.adapter = recyclerAdapter


    }

    fun verileriAl(){

        database.collection("Stations").document(istasyonDokumanId).collection("IstasyonYorumlari").orderBy("tarih", Query.Direction.DESCENDING).addSnapshotListener { snapshot, exception ->

            if(exception != null ){
                Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()
            }else{

                if(snapshot != null){

                    if(snapshot.isEmpty == false){

                        val documents = snapshot.documents

                        istasyonYorumlariListesi.clear()

                        for ( document in documents){


                            val kullaniciAdi = document.get("yorumuYapanKullanici") as String
                            val kullaniciYorum = document.get("istasyonYorumu") as String
                            val istasyonPuani = document.get("istasyonPuan") as String
                            val istasyonYorumId =document.id
                            val kullanici = document.get("kullaniciUid") as String

                            val puan ="Puanım:"+" "+istasyonPuani

                            val indirilen = IstasyonYorumlari(kullaniciAdi,kullaniciYorum,puan,istasyonDokumanId,istasyonYorumId,kullanici)

                            istasyonYorumlariListesi.add(indirilen)


                        }

                        recyclerAdapter.notifyDataSetChanged()


                    }

                }


            }


        }

    }
}