package com.ocak.adminapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.ocak.adminapp.R
import kotlinx.android.synthetic.main.activity_sinav_olustur.*

class TestOlusturActivity : AppCompatActivity() {

    var soruBir=""
    var soruIki=""
    var soruUc=""
    var soruDort=""
    var soruBes=""
    var soruAlti=""
    var soruYedi=""
    var soruSekiz=""
    var soruDokuz=""
    var soruOn=""
    var cevapBir=""
    var cevapIki=""
    var cevapUc=""
    var cevapDort=""
    var cevapBes=""
    var cevapAlti=""
    var cevapYedi=""
    var cevapSekiz=""
    var cevapDokuz=""
    var cevapOn=""

    var soruBirA=""
    var soruBirB=""
    var soruBirC=""
    var soruBirD=""
    var soruIkiA=""
    var soruIkiB=""
    var soruIkiC=""
    var soruIkiD=""
    var soruUcA=""
    var soruUcB=""
    var soruUcC=""
    var soruUcD=""
    var soruDortA=""
    var soruDortB=""
    var soruDortC=""
    var soruDortD=""
    var soruBesA=""
    var soruBesB=""
    var soruBesC=""
    var soruBesD=""
    var soruAltiA=""
    var soruAltiB=""
    var soruAltiC=""
    var soruAltiD=""
    var soruYediA=""
    var soruYediB=""
    var soruYediC=""
    var soruYediD=""
    var soruSekizA=""
    var soruSekizB=""
    var soruSekizC=""
    var soruSekizD=""
    var soruDokuzA=""
    var soruDokuzB=""
    var soruDokuzC=""
    var soruDokuzD=""
    var soruOnA=""
    var soruOnB=""
    var soruOnC=""
    var soruOnD=""

    private val dokumanId="QnMBGnHiEiRpmFErYsho"

    private lateinit var database : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sinav_olustur)

        database= FirebaseFirestore.getInstance()

    }

    fun sinavOlusturGuncelle(view: View){

        soruBir = soruBirOlustur.text.toString()
        soruIki = soruIkiOlustur.text.toString()
        soruUc = soruUcOlustur.text.toString()
        soruDort = soruDortOlustur.text.toString()
        soruBes = soruBesOlustur.text.toString()
        soruAlti = soruAltiOlustur.text.toString()
        soruYedi = soruYediOlustur.text.toString()
        soruSekiz = soruSekizOlustur.text.toString()
        soruDokuz = soruDokuzOlustur.text.toString()
        soruOn = soruOnOlustur.text.toString()

        soruBirA= soruBirAOlustur.text.toString()
        soruBirB= soruBirBOlustur.text.toString()
        soruBirC = soruBirCOlustur.text.toString()
        soruBirD= soruBirDOlustur.text.toString()
        soruIkiA= soruIkiAOlustur.text.toString()
        soruIkiB= soruIkiBOlustur.text.toString()
        soruIkiC = soruIkiCOlustur.text.toString()
        soruIkiD= soruIkiDOlustur.text.toString()
        soruUcA= soruUcAOlustur.text.toString()
        soruUcB= soruUcBOlustur.text.toString()
        soruUcC = soruUcCOlustur.text.toString()
        soruUcD= soruUcDOlustur.text.toString()
        soruDortA= soruDortAOlustur.text.toString()
        soruDortB= soruDortBOlustur.text.toString()
        soruDortC = soruDortCOlustur.text.toString()
        soruDortD= soruDortDOlustur.text.toString()
        soruBesA= soruBesAOlustur.text.toString()
        soruBesB= soruBesBOlustur.text.toString()
        soruBesC = soruBesCOlustur.text.toString()
        soruBesD= soruBesDOlustur.text.toString()
        soruAltiA= soruAltiAOlustur.text.toString()
        soruAltiB= soruAltiBOlustur.text.toString()
        soruAltiC = soruAltiCOlustur.text.toString()
        soruAltiD= soruAltiDOlustur.text.toString()
        soruYediA= soruYediAOlustur.text.toString()
        soruYediB= soruYediBOlustur.text.toString()
        soruYediC = soruYediCOlustur.text.toString()
        soruYediD= soruYediDOlustur.text.toString()
        soruSekizA= soruSekizAOlustur.text.toString()
        soruSekizB= soruSekizBOlustur.text.toString()
        soruSekizC = soruSekizCOlustur.text.toString()
        soruSekizD= soruSekizDOlustur.text.toString()
        soruDokuzA= soruDokuzAOlustur.text.toString()
        soruDokuzB= soruDokuzBOlustur.text.toString()
        soruDokuzC = soruDokuzCOlustur.text.toString()
        soruDokuzD= soruDokuzDOlustur.text.toString()
        soruOnA= soruOnAOlustur.text.toString()
        soruOnB= soruOnBOlustur.text.toString()
        soruOnC = soruOnCOlustur.text.toString()
        soruOnD= soruOnDOlustur.text.toString()




        if(soruBir!= "" && cevapBir != "" && soruIki != "" && cevapIki != "" && soruUc!="" && cevapUc!=""&& soruDort!=""&& cevapDort!=""&& soruBes!=""&&cevapBes!="" && soruAlti!=""&& cevapAlti!=""&& soruYedi!=""&&cevapYedi!=""&&soruSekiz!=""&&cevapSekiz!=""&&soruDokuz!=""&&cevapDokuz!=""&&soruOn!=""&&cevapOn!=""){

       if (soruBirA!=""&&soruBirB!=""&&soruBirC!=""&&soruBirD!=""&&soruIkiA!=""&&soruIkiB!=""&&soruIkiC!=""&&soruIkiD!=""&&soruUcA!=""&&soruUcB!=""&& soruUcC!=""&&soruDortD!=""&& soruBesA!="" && soruBesB!="" && soruBesC!=""&&soruBesD!=""&&soruAltiA!=""&&soruAltiB!=""&&soruAltiC!=""&&soruAltiD!=""&& soruYediA!="" &&soruYediB!="" && soruYediC!="" && soruYediD!="" && soruSekizA!="" && soruSekizB!="" && soruSekizC!=""&& soruSekizD!="" && soruDokuzA!="" && soruDokuzB!="" && soruDokuzC!="" && soruDokuzD!="" && soruOnA!="" && soruOnB!="" && soruOnC!=""  && soruOnD!=""){


           val postHashMap = hashMapOf<String,Any>()
           postHashMap.put("soruBir",soruBir)
           postHashMap.put("soruIki",soruIki)
           postHashMap.put("soruUc",soruUc)
           postHashMap.put("soruDort",soruDort)
           postHashMap.put("soruBes",soruBes)
           postHashMap.put("soruAlti",soruAlti)
           postHashMap.put("soruYedi",soruYedi)
           postHashMap.put("soruSekiz",soruSekiz)
           postHashMap.put("soruDokuz",soruDokuz)
           postHashMap.put("soruOn",soruOn)
           postHashMap.put("cevapBir",cevapBir)
           postHashMap.put("cevapIki",cevapIki)
           postHashMap.put("cevapUc",cevapUc)
           postHashMap.put("cevapDort",cevapDort)
           postHashMap.put("cevapBes",cevapBes)
           postHashMap.put("cevapAlti",cevapAlti)
           postHashMap.put("cevapYedi",cevapYedi)
           postHashMap.put("cevapSekiz",cevapSekiz)
           postHashMap.put("cevapDokuz",cevapDokuz)
           postHashMap.put("cevapOn",cevapOn)
           postHashMap.put("soruBirA",soruBirA)
           postHashMap.put("soruBirB",soruBirB)
           postHashMap.put("soruBirC",soruBirC)
           postHashMap.put("soruBirD",soruBirD)
           postHashMap.put("soruIkiA",soruIkiA)
           postHashMap.put("soruIkiB",soruIkiB)
           postHashMap.put("soruIkiC",soruIkiC)
           postHashMap.put("soruIkiD",soruIkiD)
           postHashMap.put("soruUcA",soruUcA)
           postHashMap.put("soruUcB",soruUcB)
           postHashMap.put("soruUcC",soruUcC)
           postHashMap.put("soruUcD",soruUcD)
           postHashMap.put("soruDortA",soruDortA)
           postHashMap.put("soruDortB",soruDortB)
           postHashMap.put("soruDortC",soruDortC)
           postHashMap.put("soruDortD",soruDortD)
           postHashMap.put("soruBesA",soruBesA)
           postHashMap.put("soruBesB",soruBesB)
           postHashMap.put("soruBesC",soruBesC)
           postHashMap.put("soruBesD",soruBesD)
           postHashMap.put("soruAltiA",soruAltiA)
           postHashMap.put("soruAltiB",soruAltiB)
           postHashMap.put("soruAltiC",soruAltiC)
           postHashMap.put("soruAltiD",soruAltiD)
           postHashMap.put("soruYediA",soruYediA)
           postHashMap.put("soruYediB",soruYediB)
           postHashMap.put("soruYediC",soruYediC)
           postHashMap.put("soruYediD",soruYediD)
           postHashMap.put("soruSekizA",soruSekizA)
           postHashMap.put("soruSekizB",soruSekizB)
           postHashMap.put("soruSekizC",soruSekizC)
           postHashMap.put("soruSekizD",soruSekizD)
           postHashMap.put("soruDokuzA",soruDokuzA)
           postHashMap.put("soruDokuzB",soruDokuzB)
           postHashMap.put("soruDokuzC",soruDokuzC)
           postHashMap.put("soruDokuzD",soruDokuzD)
           postHashMap.put("soruOnA",soruOnA)
           postHashMap.put("soruOnB",soruOnB)
           postHashMap.put("soruOnC",soruOnC)
           postHashMap.put("soruOnD",soruOnD)







           database.collection("Test").document(dokumanId).update(postHashMap).addOnCompleteListener {
               if (it.isSuccessful){

                   Toast.makeText(applicationContext,"İşlem tamamlandı.", Toast.LENGTH_LONG).show()
                   val intent = Intent(this,NeKadarOrdulusunActivity::class.java)
                   startActivity(intent)
                   finish()

               }
           }.addOnFailureListener {
               Toast.makeText(applicationContext,it.localizedMessage, Toast.LENGTH_LONG).show()
           }








       }else{

           Toast.makeText(this,"Eksik işlem var!",Toast.LENGTH_SHORT).show()
           return

       }


     }else{
         Toast.makeText(this,"Eksik işlem var!",Toast.LENGTH_SHORT).show()
         return
     }




    }

    fun cevapBir(view: View){

       val id = soruBirAlani.checkedRadioButtonId

       if (id==R.id.soruBirA){

          cevapBir ="A"
       }
       if (id==R.id.soruBirB){
          cevapBir ="B"

       }
       if (id==R.id.soruBirC){
           cevapBir="C"

       }
       if (id==R.id.soruBirD){
           cevapBir="D"

       }


    }

    fun cevapIki(view: View){

        val id = soruIkiAlani.checkedRadioButtonId

        if (id==R.id.soruIkiA){

            cevapIki ="A"
        }
        if (id==R.id.soruIkiB){
            cevapIki ="B"

        }
        if (id==R.id.soruIkiC){
            cevapIki="C"

        }
        if (id==R.id.soruIkiD){
            cevapIki="D"

        }


    }

    fun cevapUc(view: View){

        val id = soruUcAlani.checkedRadioButtonId

        if (id==R.id.soruUcA){

            cevapUc ="A"
        }
        if (id==R.id.soruUcB){
            cevapUc ="B"

        }
        if (id==R.id.soruUcC){
            cevapUc="C"

        }
        if (id==R.id.soruUcD){
            cevapUc="D"

        }


    }

    fun cevapDort(view: View){

        val id = soruDortAlani.checkedRadioButtonId

        if (id==R.id.soruDortA){

            cevapDort ="A"
        }
        if (id==R.id.soruDortB){
            cevapDort ="B"

        }
        if (id==R.id.soruDortC){
            cevapDort="C"

        }
        if (id==R.id.soruDortD){
            cevapDort="D"

        }


    }

    fun cevapBes(view: View){

        val id = soruBesAlani.checkedRadioButtonId

        if (id==R.id.soruBesA){

            cevapBes ="A"
        }
        if (id==R.id.soruBesB){
            cevapBes ="B"

        }
        if (id==R.id.soruBesC){
            cevapBes="C"

        }
        if (id==R.id.soruBesD){
            cevapBes="D"

        }


    }

    fun cevapAlti(view: View){

        val id = soruAltiAlani.checkedRadioButtonId

        if (id==R.id.soruAltiA){

            cevapAlti ="A"
        }
        if (id==R.id.soruAltiB){
            cevapAlti ="B"

        }
        if (id==R.id.soruAltiC){
            cevapAlti="C"

        }
        if (id==R.id.soruAltiD){
            cevapAlti="D"

        }


    }

    fun cevapYedi(view: View){

        val id = soruYediAlani.checkedRadioButtonId

        if (id==R.id.soruYediA){

            cevapYedi ="A"
        }
        if (id==R.id.soruYediB){
            cevapYedi ="B"

        }
        if (id==R.id.soruYediC){
            cevapYedi="C"

        }
        if (id==R.id.soruYediD){
            cevapYedi="D"

        }


    }

    fun cevapSekiz(view: View){

        val id = soruSekizAlani.checkedRadioButtonId

        if (id==R.id.soruSekizA){

            cevapSekiz ="A"
        }
        if (id==R.id.soruSekizB){
            cevapSekiz ="B"

        }
        if (id==R.id.soruSekizC){
            cevapSekiz="C"

        }
        if (id==R.id.soruSekizD){
            cevapSekiz="D"

        }


    }

    fun cevapDokuz(view: View){

        val id = soruDokuzAlani.checkedRadioButtonId

        if (id==R.id.soruDokuzA){

            cevapDokuz ="A"
        }
        if (id==R.id.soruDokuzB){
            cevapDokuz ="B"

        }
        if (id==R.id.soruDokuzC){
            cevapDokuz="C"

        }
        if (id==R.id.soruDokuzD){
            cevapDokuz="D"

        }


    }

    fun cevapOn(view: View){

        val id = soruOnAlani.checkedRadioButtonId

        if (id==R.id.soruOnA){

            cevapOn ="A"
        }
        if (id==R.id.soruOnB){
            cevapOn ="B"

        }
        if (id==R.id.soruOnC){
            cevapOn="C"

        }
        if (id==R.id.soruOnD){
            cevapOn="D"

        }

    }


}