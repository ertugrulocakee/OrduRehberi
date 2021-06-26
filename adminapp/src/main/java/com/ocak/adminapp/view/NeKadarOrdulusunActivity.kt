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
import kotlinx.android.synthetic.main.activity_sinav_giris.*

class NeKadarOrdulusunActivity : AppCompatActivity() {

    var testSoruBir=""
    var testSoruIki =""
    var testSoruUc=""
    var testSoruDort=""
    var testSoruBes=""
    var testSoruAlti=""
    var testSoruYedi=""
    var testSoruSekiz=""
    var testSoruDokuz=""
    var testSoruOn=""
    var testBirCevap=""
    var testIkiCevap=""
    var testUcCevap=""
    var testDortCevap=""
    var testBesCevap=""
    var testAltiCevap=""
    var testYediCevap=""
    var testSekizCevap=""
    var testDokuzCevap=""
    var testOnCevap=""

    var soruTestBirA=""
    var soruTestBirB=""
    var soruTestBirC=""
    var soruTestBirD=""
    var soruTestIkiA=""
    var soruTestIkiB=""
    var soruTestIkiC=""
    var soruTestIkiD=""
    var soruTestUcA=""
    var soruTestUcB=""
    var soruTestUcC=""
    var soruTestUcD=""
    var soruTestDortA=""
    var soruTestDortB=""
    var soruTestDortC=""
    var soruTestDortD=""
    var soruTestBesA=""
    var soruTestBesB=""
    var soruTestBesC=""
    var soruTestBesD=""
    var soruTestAltiA=""
    var soruTestAltiB=""
    var soruTestAltiC=""
    var soruTestAltiD=""
    var soruTestYediA=""
    var soruTestYediB=""
    var soruTestYediC=""
    var soruTestYediD=""
    var soruTestSekizA=""
    var soruTestSekizB=""
    var soruTestSekizC=""
    var soruTestSekizD=""
    var soruTestDokuzA=""
    var soruTestDokuzB=""
    var soruTestDokuzC=""
    var soruTestDokuzD=""
    var soruTestOnA=""
    var soruTestOnB=""
    var soruTestOnC=""
    var soruTestOnD=""

    var kullaniciBirCevap=""
    var kullaniciIkiCevap=""
    var kullaniciUcCevap=""
    var kullaniciDortCevap=""
    var kullaniciBesCevap=""
    var kullaniciAltiCevap=""
    var kullaniciYediCevap=""
    var kullaniciSekizCevap=""
    var kullaniciDokuzCevap=""
    var kullaniciOnCevap=""

    private val dokuman="QnMBGnHiEiRpmFErYsho"

    private  lateinit var  database : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sinav_giris)

        database= FirebaseFirestore.getInstance()

        sinavSonucBilgisi.visibility = View.GONE
        verileriAl()

    }

    fun sinavSonuc(view: View){

     var puan = 0

     if (kullaniciBirCevap !="" && kullaniciIkiCevap != "" && kullaniciUcCevap !="" && kullaniciDortCevap != "" && kullaniciBesCevap != "" && kullaniciAltiCevap!="" && kullaniciYediCevap !="" && kullaniciSekizCevap !="" && kullaniciDokuzCevap != "" && kullaniciOnCevap !=""){


         if (kullaniciBirCevap == testBirCevap){
             puan = puan +10

         }

         if (kullaniciIkiCevap == testIkiCevap){
             puan = puan +10

         }

         if (kullaniciUcCevap == testUcCevap){
             puan = puan +10

         }
         if (kullaniciDortCevap == testDortCevap){
             puan = puan +10

         }
         if (kullaniciBesCevap == testBesCevap){
             puan = puan +10

         }
         if (kullaniciAltiCevap == testAltiCevap){
             puan = puan +10

         }
         if (kullaniciYediCevap == testYediCevap){
             puan = puan +10

         }
         if (kullaniciSekizCevap == testSekizCevap){
             puan = puan +10

         }
         if (kullaniciDokuzCevap == testDokuzCevap){
             puan = puan +10

         }
         if (kullaniciOnCevap == testOnCevap){
             puan = puan +10

         }

         sinavSonucBilgisi.text = puan.toString()+" "+"puan aldınız."
         sinavSonucBilgisi.visibility = View.VISIBLE



     }else{
         Toast.makeText(this,"Çözülmemiş soru var!",Toast.LENGTH_SHORT).show()
         return

     }

    }


        fun verileriAl(){


            database.collection("Test").document(dokuman).addSnapshotListener { snapshot, exception ->

                if (exception != null){

                    Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()

                }else{

                    if (snapshot != null){


                        testSoruBir = snapshot.getString("soruBir").toString()
                        testSoruIki = snapshot.getString("soruIki").toString()
                        testSoruUc = snapshot.getString("soruUc").toString()
                        testSoruDort = snapshot.getString("soruDort").toString()
                        testSoruBes = snapshot.getString("soruBes").toString()
                        testSoruAlti = snapshot.getString("soruAlti").toString()
                        testSoruYedi = snapshot.getString("soruYedi").toString()
                        testSoruSekiz = snapshot.getString("soruSekiz").toString()
                        testSoruDokuz = snapshot.getString("soruDokuz").toString()
                        testSoruOn = snapshot.getString("soruOn").toString()

                        testBirCevap = snapshot.getString("cevapBir").toString()
                        testIkiCevap = snapshot.getString("cevapIki").toString()
                        testUcCevap = snapshot.getString("cevapUc").toString()
                        testDortCevap = snapshot.getString("cevapDort").toString()
                        testBesCevap = snapshot.getString("cevapBes").toString()
                        testAltiCevap = snapshot.getString("cevapAlti").toString()
                        testYediCevap = snapshot.getString("cevapYedi").toString()
                        testSekizCevap = snapshot.getString("cevapSekiz").toString()
                        testDokuzCevap = snapshot.getString("cevapDokuz").toString()
                        testOnCevap = snapshot.getString("cevapOn").toString()

                        soruTestBirA = snapshot.getString("soruBirA").toString()
                        soruTestBirB = snapshot.getString("soruBirB").toString()
                        soruTestBirC = snapshot.getString("soruBirC").toString()
                        soruTestBirD = snapshot.getString("soruBirD").toString()
                        soruTestIkiA = snapshot.getString("soruIkiA").toString()
                        soruTestIkiB = snapshot.getString("soruIkiB").toString()
                        soruTestIkiC = snapshot.getString("soruIkiC").toString()
                        soruTestIkiD = snapshot.getString("soruIkiD").toString()
                        soruTestUcA = snapshot.getString("soruUcA").toString()
                        soruTestUcB = snapshot.getString("soruUcB").toString()
                        soruTestUcC = snapshot.getString("soruUcC").toString()
                        soruTestUcD = snapshot.getString("soruUcD").toString()
                        soruTestDortA = snapshot.getString("soruDortA").toString()
                        soruTestDortB = snapshot.getString("soruDortB").toString()
                        soruTestDortC = snapshot.getString("soruDortC").toString()
                        soruTestDortD = snapshot.getString("soruDortD").toString()
                        soruTestBesA = snapshot.getString("soruBesA").toString()
                        soruTestBesB = snapshot.getString("soruBesB").toString()
                        soruTestBesC = snapshot.getString("soruBesC").toString()
                        soruTestBesD = snapshot.getString("soruBesD").toString()
                        soruTestAltiA = snapshot.getString("soruAltiA").toString()
                        soruTestAltiB = snapshot.getString("soruAltiB").toString()
                        soruTestAltiC = snapshot.getString("soruAltiC").toString()
                        soruTestAltiD = snapshot.getString("soruAltiD").toString()
                        soruTestYediA = snapshot.getString("soruYediA").toString()
                        soruTestYediB = snapshot.getString("soruYediB").toString()
                        soruTestYediC = snapshot.getString("soruYediC").toString()
                        soruTestYediD = snapshot.getString("soruYediD").toString()
                        soruTestSekizA = snapshot.getString("soruSekizA").toString()
                        soruTestSekizB = snapshot.getString("soruSekizB").toString()
                        soruTestSekizC = snapshot.getString("soruSekizC").toString()
                        soruTestSekizD = snapshot.getString("soruSekizD").toString()
                        soruTestDokuzA = snapshot.getString("soruDokuzA").toString()
                        soruTestDokuzB = snapshot.getString("soruDokuzB").toString()
                        soruTestDokuzC = snapshot.getString("soruDokuzC").toString()
                        soruTestDokuzD = snapshot.getString("soruDokuzD").toString()
                        soruTestOnA = snapshot.getString("soruOnA").toString()
                        soruTestOnB = snapshot.getString("soruOnB").toString()
                        soruTestOnC = snapshot.getString("soruOnC").toString()
                        soruTestOnD = snapshot.getString("soruOnD").toString()

                        testBir.text = "1) "+testSoruBir
                        testIki.text ="2) "+testSoruIki
                        testUc.text = "3) "+testSoruUc
                        testDort.text ="4) "+testSoruDort
                        testBes.text ="5) "+testSoruBes
                        testAlti.text ="6) "+testSoruAlti
                        testYedi.text ="7) "+ testSoruYedi
                        testSekiz.text ="8) "+testSoruSekiz
                        testDokuz.text ="9) "+testSoruDokuz
                        testOn.text ="10) "+ testSoruOn


                        testBirAText.text ="A) "+soruTestBirA
                        testBirBText.text="B) "+soruTestBirB
                        testBirCText.text="C) "+soruTestBirC
                        testBirDText.text="D) "+soruTestBirD
                        testIkiAText.text ="A) "+soruTestIkiA
                        testIkiBText.text="B) "+soruTestIkiB
                        testIkiCText.text="C) "+soruTestIkiC
                        testIkiDText.text="D) "+soruTestIkiD
                        testUcAText.text ="A) "+soruTestUcA
                        testUcBText.text="B) "+soruTestUcB
                        testUcCText.text="C) "+soruTestUcC
                        testUcDText.text="D) "+soruTestUcD
                        testDortAText.text ="A) "+soruTestDortA
                        testDortBText.text="B) "+soruTestDortB
                        testDortCText.text="C) "+soruTestDortC
                        testDortDText.text="D) "+soruTestDortD
                        testBesAText.text ="A) "+soruTestBesA
                        testBesBText.text="B) "+soruTestBesB
                        testBesCText.text="C) "+soruTestBesC
                        testBesDText.text="D) "+soruTestBesD
                        testAltiAText.text ="A) "+soruTestAltiA
                        testAltiBText.text="B) "+soruTestAltiB
                        testAltiCText.text="C) "+soruTestAltiC
                        testAltiDText.text="D) "+soruTestAltiD
                        testYediAText.text ="A) "+soruTestYediA
                        testYediBText.text="B) "+soruTestYediB
                        testYediCText.text="C) "+soruTestYediC
                        testYediDText.text="D) "+soruTestYediD
                        testSekizAText.text ="A) "+soruTestSekizA
                        testSekizBText.text="B) "+soruTestSekizB
                        testSekizCText.text="C) "+soruTestSekizC
                        testSekizDText.text="D) "+soruTestSekizD
                        testDokuzAText.text ="A) "+soruTestDokuzA
                        testDokuzBText.text="B) "+soruTestDokuzB
                        testDokuzCText.text="C) "+soruTestDokuzC
                        testDokuzDText.text="D) "+soruTestDokuzD
                        testOnAText.text ="A) "+soruTestOnA
                        testOnBText.text="B) "+soruTestOnB
                        testOnCText.text="C) "+soruTestOnC
                        testOnDText.text="D) "+soruTestOnD


                    }


                }
            }
        }



    fun testBirCevap(view: View){

        val id = testBirCevapAlani.checkedRadioButtonId

        if (id==R.id.testBirA){
           kullaniciBirCevap ="A"

        }

        if (id==R.id.testBirB){
            kullaniciBirCevap="B"

        }
        if (id==R.id.testBirC){

            kullaniciBirCevap="C"
        }
        if (id==R.id.testBirD){

            kullaniciBirCevap="D"
        }

    }

    fun testIkiCevap(view: View){

        val id = testIkiCevapAlani.checkedRadioButtonId

        if (id==R.id.testIkiA){
            kullaniciIkiCevap ="A"

        }

        if (id==R.id.testIkiB){
            kullaniciIkiCevap="B"

        }
        if (id==R.id.testIkiC){

            kullaniciIkiCevap="C"
        }
        if (id==R.id.testIkiD){

            kullaniciIkiCevap="D"
        }

    }

    fun testUcCevap(view: View){

        val id = testUcCevapAlani.checkedRadioButtonId

        if (id==R.id.testUcA){
            kullaniciUcCevap ="A"

        }

        if (id==R.id.testUcB){
            kullaniciUcCevap="B"

        }
        if (id==R.id.testUcC){

            kullaniciUcCevap="C"
        }
        if (id==R.id.testUcD){

            kullaniciUcCevap="D"
        }

    }

    fun testDortCevap(view: View){

        val id = testDortCevapAlani.checkedRadioButtonId

        if (id==R.id.testDortA){
            kullaniciDortCevap ="A"

        }

        if (id==R.id.testDortB){
            kullaniciDortCevap="B"

        }
        if (id==R.id.testDortC){

            kullaniciDortCevap="C"
        }
        if (id==R.id.testDortD){

            kullaniciDortCevap="D"
        }

    }

    fun testBesCevap(view: View){

        val id = testBesCevapAlani.checkedRadioButtonId

        if (id==R.id.testBesA){
            kullaniciBesCevap ="A"

        }

        if (id==R.id.testBesB){
            kullaniciBesCevap="B"

        }
        if (id==R.id.testBesC){

            kullaniciBesCevap="C"
        }
        if (id==R.id.testBesD){

            kullaniciBesCevap="D"
        }

    }

    fun testAltiCevap(view: View){

        val id = testAltiCevapAlani.checkedRadioButtonId

        if (id==R.id.testAltiA){
            kullaniciAltiCevap ="A"

        }

        if (id==R.id.testAltiB){
            kullaniciAltiCevap="B"

        }
        if (id==R.id.testAltiC){

            kullaniciAltiCevap="C"
        }
        if (id==R.id.testAltiD){

            kullaniciAltiCevap="D"
        }

    }

    fun testYediCevap(view: View){

        val id = testYediCevapAlani.checkedRadioButtonId

        if (id==R.id.testYediA){
            kullaniciYediCevap ="A"

        }

        if (id==R.id.testYediB){
            kullaniciYediCevap="B"

        }
        if (id==R.id.testYediC){

            kullaniciYediCevap="C"
        }
        if (id==R.id.testYediD){

            kullaniciYediCevap="D"
        }

    }

    fun testSekizCevap(view: View){

        val id = testSekizCevapAlani.checkedRadioButtonId

        if (id==R.id.testSekizA){
            kullaniciSekizCevap ="A"

        }

        if (id==R.id.testSekizB){
            kullaniciSekizCevap="B"

        }
        if (id==R.id.testSekizC){

            kullaniciSekizCevap="C"
        }
        if (id==R.id.testSekizD){

            kullaniciSekizCevap="D"
        }

    }

    fun testDokuzCevap(view: View){

        val id = testDokuzCevapAlani.checkedRadioButtonId

        if (id==R.id.testDokuzA){
            kullaniciDokuzCevap ="A"

        }

        if (id==R.id.testDokuzB){
            kullaniciDokuzCevap="B"

        }
        if (id==R.id.testDokuzC){

            kullaniciDokuzCevap="C"
        }
        if (id==R.id.testDokuzD){

            kullaniciDokuzCevap="D"
        }

    }

    fun testOnCevap(view: View){

        val id = testOnCevapAlani.checkedRadioButtonId

        if (id==R.id.testOnA){
            kullaniciOnCevap ="A"

        }

        if (id==R.id.testOnB){
            kullaniciOnCevap="B"

        }
        if (id==R.id.testOnC){

            kullaniciOnCevap="C"
        }
        if (id==R.id.testOnD){

            kullaniciOnCevap="D"
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.test_ekle,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.testEkle){

            val intent = Intent(this, TestOlusturActivity::class.java)
            startActivity(intent)

        }


        return super.onOptionsItemSelected(item)
    }


}