package com.ocak.firebaseordu.view

import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.SeekBar
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.ocak.firebaseordu.R
import kotlinx.android.synthetic.main.activity_muzik_detayi.*

class MuzikDetayiActivity : AppCompatActivity() {


    var mediaPlayer : MediaPlayer? = null
    var secilenMuzik = ""
    var muzikId = ""
    var muzikDosyasi =""


    private lateinit var database: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_muzik_detayi)




        database = FirebaseFirestore.getInstance()

        val intent = getIntent()
        secilenMuzik = intent.getStringExtra("ad").toString()
        muzikId= intent.getStringExtra("id").toString()

        verileriAl()


        sesKontrol()


    }

    fun sesKontrol(){

        idBtnPlay.setOnClickListener {

            if (mediaPlayer == null){

                mediaPlayer = MediaPlayer.create(this@MuzikDetayiActivity, Uri.parse(muzikDosyasi))

                ilklendirSeekBar()

            }


            mediaPlayer?.start()

        }

        idBtnPause.setOnClickListener {
            if (mediaPlayer!==null) mediaPlayer?.pause()
        }


        idBtnStop.setOnClickListener {

            if (mediaPlayer!==null){
                mediaPlayer?.stop()
                mediaPlayer?.reset()
                mediaPlayer?.release()
                mediaPlayer = null
            }
        }

        muzikIlerleme.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) mediaPlayer?.seekTo(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {


            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })


    }



    fun ilklendirSeekBar(){


        muzikIlerleme.max = mediaPlayer!!.duration
        val handler = Handler()
        handler.postDelayed(object : Runnable{

            override fun run() {
                try {

                    muzikIlerleme.progress = mediaPlayer!!.currentPosition
                    handler.postDelayed(this, 1000)

                }catch (e:Exception){

                    muzikIlerleme.progress = 0

                }
            }


        },0)



    }

    fun verileriAl() {



        database.collection("Muzik").whereEqualTo("muzikadi",secilenMuzik).addSnapshotListener { snapshot, exception ->

            if (exception != null){

                Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()

            }else{

                if (snapshot != null){

                    if(snapshot.isEmpty == false){


                        val documents = snapshot.documents

                        for( document in documents){

                            val muzikAdi = document.get("muzikadi") as String
                            muzikDosyasi = document.get("muzikdosyasi") as String
                            val muzikTuru = document.get("muzikturu") as String
                            val muzikSeslendiren = document.get("muzikseslendiren") as String


                            muzikDetayiAd.text = "Parçanın adı:"+" "+muzikAdi
                            muzikDetayiTur.text ="Parçanın türü:"+" "+muzikTuru
                            muzikDetayiSeslendiren.text ="Parçayı seslendiren sanatçı:"+" "+muzikSeslendiren


                        }


                    }
                }
            }



        }



    }


    override fun onStop() {
        super.onStop()


        if (mediaPlayer!==null){
            mediaPlayer?.stop()
            mediaPlayer?.reset()
            mediaPlayer?.release()
            mediaPlayer = null
        }

    }

    override fun onPause() {
        super.onPause()
        if (mediaPlayer!==null) mediaPlayer?.pause()
    }


}