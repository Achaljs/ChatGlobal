package com.example.newapp

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.example.newapp.data.AuthViewModle
import com.example.newapp.databinding.ActivityHomeBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import java.util.Calendar

class HomeActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }

    private lateinit var viewModel: AuthViewModle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        viewModel = ViewModelProvider(this).get(AuthViewModle::class.java)



        binding.name.setText(Firebase.auth.currentUser?.displayName)


        binding.done.setOnClickListener{

            viewModel.logoutUser()
            startActivity(Intent(HomeActivity@this,MainActivity::class.java))
            finish()
        }




        //Language selection code start
        val languageSpinner: Spinner = binding.LanguageSpinner
        // Define the list of languages
        val languages = arrayListOf(
            "English", "French", "German", "Spanish", "Italian",
            "Chinese", "Japanese", "Korean", "Arabic", "Russian",
            "Portuguese", "Dutch", "Swedish", "Greek", "Turkish",
            "Hindi", "Bengali", "Tamil", "Telugu", "Marathi"
        )

        // Create an ArrayAdapter using the list of languages
        val adapter = ArrayAdapter(this, R.layout.spinnerlayout, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Set the ArrayAdapter to the Spinner
        languageSpinner.adapter = adapter

        // Set item selected listener on the spinner
        languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedLanguage = parent?.getItemAtPosition(position).toString()


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
//Language selection END



        binding.calendarButton.setOnClickListener {

            // on below line we are getting
            // the instance of our calendar.
            val c = Calendar.getInstance()

            // on below line we are getting
            // our day, month and year.
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            // on below line we are creating a
            // variable for date picker dialog.
            val datePickerDialog = DatePickerDialog(
                // on below line we are passing context.
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    // on below line we are setting
                    // date to our text view.
                    binding.textDate.text =
                        (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                },
                // on below line we are passing year, month
                // and day for the selected date in our date picker.
                year,
                month,
                day
            )
            // at last we are calling show
            // to display our date picker dialog.
            datePickerDialog.show()

        }





        //Profile Pic Piker

         val startForProfileImageResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                val resultCode = result.resultCode
                val data = result.data

                if (resultCode == Activity.RESULT_OK) {
                    //Image Uri will not be null for RESULT_OK
                    val fileUri = data?.data!!


                    binding.imageView.setImageURI(fileUri)
                } else if (resultCode == ImagePicker.RESULT_ERROR) {
                    Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }

        //on Button click

        binding.imageButton.setOnClickListener {

            ImagePicker.with(this)
                .cropSquare()	    			//Crop image(Optional), Check Customization for more option

                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
                }

        }





    }



