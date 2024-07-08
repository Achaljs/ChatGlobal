package com.example.newapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.newapp.data.AuthViewModle
import com.example.newapp.data.Resource
import com.example.newapp.databinding.ActivityHomeBinding
import com.example.newapp.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private lateinit var viewModel: AuthViewModle

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    //for google signIN
    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            viewModel.firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            Toast.makeText(this, "Google sign in failed: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(AuthViewModle::class.java)


        binding.googleSignIN.setOnClickListener {

            val intent = viewModel.getGoogleSignInIntent()

            googleSignInLauncher.launch(intent)

        }

        lifecycleScope.launchWhenStarted {
            viewModel.loginFlow.collect { resource ->
                when (resource) {
                    is Resource.loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Resource.success -> {
                        binding.progressBar.visibility = View.GONE

                        startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                        finish()
                    }

                    is Resource.Failure -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            this@MainActivity,
                            "Error: ${resource.exception.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    null -> {

                    }

                }


            }

            checkcurrentUserLogin()
        }
    }


        fun checkcurrentUserLogin() {
            viewModel.loginFlow.value?.let {
                if (it is Resource.success) {
                    val displayName = it.data.displayName ?: "User"
                    // User is already logged in, navigate to the home screen or main activity
                    Toast.makeText(this, "Welcome back, $displayName", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
