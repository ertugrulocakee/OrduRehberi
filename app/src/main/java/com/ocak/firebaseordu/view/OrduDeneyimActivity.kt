package com.ocak.firebaseordu.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.ocak.firebaseordu.R
import com.ocak.firebaseordu.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_share.*

class OrduDeneyimActivity : AppCompatActivity() {


    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)

        auth= FirebaseAuth.getInstance()

        val adapter = ViewPagerAdapter(supportFragmentManager)
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)



    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.ana_menu,menu)


        return super.onCreateOptionsMenu(menu)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        if (item.itemId== R.id.cikisYap){

            val alert = AlertDialog.Builder(this)
            alert.setTitle("Çıkış")
            alert.setMessage("Çıkış yapmak istediğinize emin misiniz?")
            alert.setCancelable(false);
            alert.setPositiveButton("Evet") { dialogInterface: DialogInterface, i: Int ->

                Toast.makeText(applicationContext, "Uygulamadan çıkıldı!", Toast.LENGTH_LONG).show()
                auth.signOut()
                val intent = Intent(this, UygulamaGirisActivity::class.java)
                startActivity(intent)
                finish()
            }
            alert.setNegativeButton("Hayır") { dialogInterface: DialogInterface, i: Int ->

            }

            alert.show()

        }

        if(item.itemId== R.id.ordulokasyonlar){
            val intent= Intent(this, OrduSeyahatActivity::class.java)
            startActivity(intent)

        }

        if(item.itemId== R.id.ordu){
            val intent= Intent(this, OrduTanitimActivity::class.java)
            startActivity(intent)
        }

        if (item.itemId == R.id.useroptions){
            val intent = Intent(this, UygulamaAyarlariActivity::class.java)
            startActivity(intent)

        }

        if(item.itemId == R.id.appOptions){
            val intent = Intent(this, UygulamaBilgileriActivity::class.java)
            startActivity(intent)

        }

        if(item.itemId == R.id.sinav){

            val intent = Intent(this, NeKadarOrdulusunActivity::class.java)
            startActivity(intent)

        }


        return super.onOptionsItemSelected(item)
    }



}

