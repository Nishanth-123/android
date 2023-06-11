package com.example.custom_camera.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.custom_camera.R
import com.example.custom_camera.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        prePopulateFields()
        binding.loginButton.setOnClickListener {
            if (validateFields()) {
                saveFields()
                navigateToTest()
            }
        }
    }

    private fun navigateToTest() {
        val intent = Intent(this, TestActivity::class.java)
        startActivity(intent)
    }

    private fun prePopulateFields() {
        val preferences = getPreferences(MODE_PRIVATE)
        if (preferences.contains("name")) {
            binding.name.setText(preferences.getString("name", ""))
        }
        if (preferences.contains("email")) {
            binding.email.setText(preferences.getString("email", ""))
        }
    }

    private fun saveFields() {
        val preferences = getPreferences(MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("name", binding.name.text.toString())
        editor.putString("email", binding.email.text.toString())
        editor.apply()
    }

    private fun validateFields(): Boolean {
        val nameField = binding.name
        val emailField = binding.email
        if (nameField.text!!.isEmpty() || nameField.text!!.isBlank()) {
            nameField.error = getString(R.string.name_error)
            return false
        }
        if (emailField.text!!.isEmpty() || !emailField.text?.let { android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches() }!!) {
            emailField.error = getString(R.string.email_error)
            return false
        }
        return true
    }
}