package com.pragati.communeapp.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.pragati.communeapp.R
import com.pragati.communeapp.activity.SplashScreenActivity
import kotlinx.android.synthetic.*


class LogoutFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    lateinit var btnLogout : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        auth = FirebaseAuth.getInstance()
        var view = inflater.inflate(R.layout.fragment_logout, container, false)

        btnLogout = view.findViewById(R.id.Logout)

        btnLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(activity as Context,SplashScreenActivity::class.java))
        }
        return view

    }


}