package com.ocak.firebaseordu

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.ocak.firebaseordu.adapter.OrduDeneyimleriRecyclerAdapter
import com.ocak.firebaseordu.model.Post
import com.ocak.firebaseordu.view.OrduDeneyimPaylasActivity
import kotlinx.android.synthetic.main.fragment_hepsi_fragment.*

class hepsi_fragment : Fragment() {

    private  lateinit var  auth : FirebaseAuth
    private  lateinit var database : FirebaseFirestore
    private  lateinit var recyclerAdapter: OrduDeneyimleriRecyclerAdapter


    var postListesi = ArrayList<Post>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hepsi_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        auth= FirebaseAuth.getInstance()
        database= FirebaseFirestore.getInstance()


        verileriAl()


        activity?.let {
            val layoutManager = LinearLayoutManager(it)
            recyclerView.layoutManager = layoutManager
            recyclerAdapter = OrduDeneyimleriRecyclerAdapter(it,postListesi)
            recyclerView.adapter = recyclerAdapter
        }



        swipeRefreshLayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {

            verileriAl()

            swipeRefreshLayout.isRefreshing = false

        })


        buttonAdd.setOnClickListener{
            activity?.let {

                val intent= Intent(it, OrduDeneyimPaylasActivity::class.java)
                startActivity(intent)
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }


    fun verileriAl(){

        activity?.let {

            database.collection("Post").orderBy("tarih", Query.Direction.DESCENDING).addSnapshotListener { snapshot, exception ->
                if(exception !=null){
                    Toast.makeText(it,exception.localizedMessage, Toast.LENGTH_LONG).show()
                }else{
                    if(snapshot != null){
                        if(snapshot.isEmpty == false){

                            val documents = snapshot.documents

                            postListesi.clear()

                            for (document in documents){

                                val yorum = document.get("yorum") as String
                                val puan = document.get("puan") as String
                                val gorselUrl = document.get("gorselurl") as String
                                val kullaniciAdi  = document.get("kullaniciadi") as String
                                val kullaniciGorseli = document.get("kullaniciresmi") as String
                                val kullaniciOnay = document.get("onaykutusu") as String
                                val kullaniciPuan = "Deneyim puanım:"+" "+puan
                                val kullaniciUid = document.get("uid") as String
                                val postId = document.id

                                val indirilenPost = Post(kullaniciOnay, kullaniciAdi, gorselUrl, kullaniciGorseli, yorum,kullaniciPuan,kullaniciUid,postId)
                                postListesi.add(indirilenPost)


                            }

                            recyclerAdapter.notifyDataSetChanged()

                        }


                    }
                }


            }

        }


    }



    companion object {
        fun newInstance(): hepsi_fragment {

            val fragment = hepsi_fragment()

            return fragment
        }
    }




}