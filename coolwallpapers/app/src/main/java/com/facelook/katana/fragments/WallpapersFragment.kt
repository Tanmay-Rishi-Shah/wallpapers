package com.facelook.katana.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facelook.katana.R
import com.facelook.katana.adapters.WallpapersAdapter
import com.facelook.katana.models.ImageData
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.random.Random
class WallpapersFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WallpapersAdapter
    private val imageList = mutableListOf<ImageData>()
    private val db = FirebaseFirestore.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_wallpapers, container, false)
//        recyclerView = view.findViewById(R.id.recycle)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        adapter = WallpapersAdapter(imageList)
        recyclerView.adapter = adapter
        fetchImagesFromFirebase()
        return view
    }

    private fun fetchImagesFromFirebase() {
        val collections = listOf("csk", "mi")
        collections.forEach { collection ->
            db.collection("wallpapers").document(collection)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        for (i in 1..document.data!!.size) {
                            val url = document.getString(i.toString())
                            if (url != null) {
                                imageList.add(ImageData(url))
                            }
                        }
                    }
                    if (collection == collections.last()) {
                        shuffleAndNotify()
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle error
                }
        }
    }
    private fun shuffleAndNotify() {
        imageList.shuffle(Random(System.currentTimeMillis()))
        adapter.notifyDataSetChanged()
    }
}
