package com.nishanth.callrecorder

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.telephony.PhoneStateListener
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.nishanth.callrecorder.Constants.Companion.CONTACT_PICKER_RESULT
import com.nishanth.callrecorder.Constants.Companion.DEBUG_TAG
import com.nishanth.callrecorder.Constants.Companion.REQUEST_PHONE_CALL_PERMISSION
import com.nishanth.callrecorder.Constants.Companion.REQUEST_READ_CONTACTS_PERMISSION
import com.nishanth.callrecorder.databinding.ActivityMainBinding
import android.telephony.TelephonyManager
import com.nishanth.callrecorder.Constants.Companion.REQUEST_AUDIO_RECORD_PERMISSION
import com.nishanth.callrecorder.Constants.Companion.REQUEST_PHONE_CALL_STATE_PERMISSION
import com.nishanth.callrecorder.util.CallStateListener


class MainActivity : AppCompatActivity() {

    private var contact: Contact? = null
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        observeViewModel()
        binding.selectBtn.setOnClickListener {
            openContactList()
        }
        binding.unselectBtn.setOnClickListener {
            mainViewModel.updateContact(null)
        }
        binding.callBtn.setOnClickListener {
            callNumber(mainViewModel.contact?.value!!.phoneNumber)
        }
        requestPermissions()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                CONTACT_PICKER_RESULT -> {
                    val contactData = data?.data
                    if (contactData != null) {
                        val cursor = this.contentResolver.query(contactData, null, null, null, null)
                        if (cursor == null) {
                            Toast.makeText(this, "num", Toast.LENGTH_LONG).show()
                        }
                        if (cursor != null && cursor.count > 0) {
                            cursor.moveToFirst()
                            val phoneIndex =
                                cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                            val num = cursor.getString(phoneIndex)

                            val nameIndex =
                                cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                            val name = cursor.getString(nameIndex)
                            cursor.close()
                            mainViewModel.updateContact(Contact(name, num))

                        }
                    }
                }
//                PHONE_CALL_RESULT -> {
//                    Log.d("calllokesh", data?.data.toString())
//                    //Toast.makeText(this, data?.data.toString(), Toast.LENGTH_LONG).show()
//                }
            }
        } else {
            // gracefully handle failure
            Log.d(DEBUG_TAG, "Warning: activity result not ok")
        }
    }

    private fun hasContactsPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) ==
                PackageManager.PERMISSION_GRANTED
    }

    private fun hasPhoneCallPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) ==
                PackageManager.PERMISSION_GRANTED
    }

    private fun hasPhoneStatePermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) ==
                PackageManager.PERMISSION_GRANTED
    }

    private fun hasAudioRecordPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) ==
                PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        if (!hasContactsPermission()) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CONTACTS),
                REQUEST_READ_CONTACTS_PERMISSION
            )
        } else {
            updateButton(true, binding.selectBtn)
        }
        if (!hasPhoneCallPermission()) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CALL_PHONE),
                REQUEST_PHONE_CALL_PERMISSION
            )
        }
        if (!hasPhoneStatePermission()) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_PHONE_STATE),
                REQUEST_PHONE_CALL_STATE_PERMISSION
            )
        }
        if (!hasAudioRecordPermission()) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                REQUEST_AUDIO_RECORD_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_READ_CONTACTS_PERMISSION && grantResults.isNotEmpty()) {
            updateButton(grantResults[0] == PackageManager.PERMISSION_GRANTED, binding.selectBtn)
        }

    }

    private fun observeViewModel() {
        mainViewModel.contact?.observe(this, { contact ->
            if (contact != null) {
                updateButton(true, binding.callBtn)
                updateButton(true, binding.unselectBtn)
                updateButton(false, binding.selectBtn)
                Toast.makeText(this, contact.name, Toast.LENGTH_SHORT).show()
            } else {
                updateButton(false, binding.callBtn)
                updateButton(false, binding.unselectBtn)
                updateButton(true, binding.selectBtn)
            }
        })
    }

    private fun updateButton(enable: Boolean, button: Button) {
        button.isEnabled = enable
    }

    private fun openContactList() {
        val pickContact = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        pickContact.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
        startActivityForResult(pickContact, CONTACT_PICKER_RESULT)
    }

    private fun callNumber(number: String) {
        if (hasPhoneCallPermission() &&
            hasPhoneStatePermission() &&
            hasAudioRecordPermission()
        ) {

            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$number")
            startActivity(callIntent)
            //startActivityForResult(callIntent, PHONE_CALL_RESULT)
            val telephonyManager =
                getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            telephonyManager.listen(
                CallStateListener(true),
                PhoneStateListener.LISTEN_CALL_STATE
            )
        } else {
            if (!hasPhoneCallPermission()) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.call_permission_error),
                    Toast.LENGTH_SHORT
                ).show()
            } else if (!hasPhoneStatePermission()) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.phone_state_permission_error),
                    Toast.LENGTH_SHORT
                ).show()
            } else if(!hasAudioRecordPermission()){
                Toast.makeText(
                    this,
                    resources.getString(R.string.audio_record_permission_error),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

}