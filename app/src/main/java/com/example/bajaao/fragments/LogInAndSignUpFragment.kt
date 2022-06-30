package com.example.bajaao.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.example.bajaao.R
import com.example.bajaao.model.AuthModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LogInAndSignUpFragment : Fragment() {


    private lateinit var signIn_Button: LinearLayout


    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private var dataRef: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("User Info")

    private val RC_SIGN_INT: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_in_and_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signIn_Button = view.findViewById(R.id.signInWithGoogle)


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)


        signIn_Button.setOnClickListener {
            signInActivity()
        }
    }

    private fun signInActivity() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(
            signInIntent, RC_SIGN_INT
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_INT) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account?.idToken.toString())
        } catch (e: ApiException) {
            Log.e("signIn error", e.message.toString())
        }
    }


    private fun firebaseAuthWithGoogle(idToken: String) {
        val authCredential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(authCredential)
            .addOnCompleteListener(requireActivity()) {
                if (it.isSuccessful) {
                    updateUi()
                    val fragmentManager = requireActivity().supportFragmentManager
                    val fragmentTransition = fragmentManager.beginTransaction()
                    val selectMusicDoYouLike = SelectMusicDoYouLike()
                    fragmentTransition.setCustomAnimations(R.anim.fragment_enter_animation , R.anim.fragment_exit_animation )
                    fragmentTransition.replace(
                        R.id.fragmentContainerView, selectMusicDoYouLike , "LOGIN_FRAGMENT"
                    ).commit()
                    fragmentTransition.addToBackStack(null)
                } else if (it.isCanceled) {
                    Toast.makeText(
                        requireContext(),
                        "Check internet connection",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun updateUi() {
        val userSignIn = GoogleSignIn.getLastSignedInAccount(requireContext())
        dataRef.child(FirebaseAuth.getInstance().uid.toString())
            .setValue(AuthModel(userSignIn?.displayName.toString()))

    }


}



