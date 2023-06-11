package com.nishanth.firestoredemo

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TITLE = "Title"
        private const val DESCRIPTION = "Description"
    }

    lateinit var title: EditText
    lateinit var description: EditText
    lateinit var priority:EditText
    lateinit var result: TextView
    private val db = FirebaseFirestore.getInstance()
    private val noteRef = db.document("Notebook/First Note")
    private val collectionRef = db.collection("Notebook")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = findViewById(R.id.title)
        description = findViewById(R.id.text)
        priority=findViewById(R.id.priority)
        result = findViewById(R.id.result)
    }

    override fun onStart() {
        super.onStart()
        collectionRef.addSnapshotListener(this, object : EventListener<QuerySnapshot> {
            override fun onEvent(querySnapshots: QuerySnapshot?, e: FirebaseFirestoreException?) {
                if (e != null) {
                    result.setText(e.message.toString())
                    return
                }
                var notesData = ""
                querySnapshots?.let {
                    for (querySnapshot in it) {
                        val note: Note = querySnapshot.toObject(Note::class.java)
                        notesData += "Title : ${note.title}\nTitle : ${note.description}\nPriority : ${note.priority}\n\n"
                    }
                }
                result.setText(notesData)
            }

        })
        /*
        noteRef.addSnapshotListener(this) { documentSnapshot, e ->
            //exception may arise when accessing the doc is not possible
            if (e != null) {
                result.setText(e.message.toString())
                return@addSnapshotListener;
            }
            if (documentSnapshot!!.exists()) {
                val note:Note = documentSnapshot.toObject(Note.class)
                result.setText ("Title : ${note.title}\n" + "Title : ${note.getString(DESCRIPTION)}\n")
                //result.setText("Title : ${documentSnapshot!!.getString(TITLE)}\n" + "Title : ${documentSnapshot.getString(DESCRIPTION)}\n")
            } else {
                result.setText("")
            }
        }

         */
    }

    fun addNote(view: View) {
        val note = Note()
        note.title=title.text.toString()
        note.description=description.text.toString()
        note.priority=priority.text.toString().toInt()
        collectionRef.add(note)
        /*
        val note = HashMap<String, Any>()
        note[TITLE] = title.text.toString()
        note[DESCRIPTION] = description.text.toString()

         */
        /*
        noteRef.set(note)
                .addOnSuccessListener {
                    Toast.makeText(this, "saved successfully", LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, it.message.toString(), LENGTH_SHORT).show()
                }

         */
        /*db.collection("Notebook").document("First Note").set(note)
                .addOnSuccessListener {
                    Toast.makeText(this, "saved successfully", LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, it.message.toString(), LENGTH_SHORT).show()
                }*/
    }

    fun loadNotes(view: View) {
        /*
        noteRef.get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        //it.data returns the map we had passed
                        //result.setText("Title : ${it.getString(TITLE)}\n" + "Title : ${it.getString(DESCRIPTION)}\n")
                        val note:Note = it.toObject(Note.class)
                        result . setText ("Title : ${note.title}\n" + "Title : ${note.getString(DESCRIPTION)}\n")
                    } else {
                        result.setText("Document does not exist")
                    }
                }
                .addOnFailureListener {
                    result.setText(it.message.toString())
                }

         */
        collectionRef
                .whereGreaterThanOrEqualTo("priority", 2)
                .orderBy("priority", Query.Direction.DESCENDING)
                .limit(3)
                .get()
                .addOnSuccessListener(object : OnSuccessListener<QuerySnapshot> {
                    override fun onSuccess(querySnapshots: QuerySnapshot?) {
                        var notesData = ""
                        querySnapshots?.let {
                            for (querySnapshot in it) {
                                val note: Note = querySnapshot.toObject(Note::class.java)
                                notesData += "Title : ${note.title}\nTitle : ${note.description}\nPriority : ${note.priority}\n\n"
                            }
                        }
                        result.setText(notesData)
                    }

                })
    }
/*
    fun deleteDescription(view: View) {
        /*val note = HashMap<String, Any>()
        note[DESCRIPTION] = description.text.toString()
        noteRef.update(DESCRIPTION, FieldValue.delete())*/
        noteRef.update(DESCRIPTION, FieldValue.delete())
    }

    fun deleteNotes(view: View) {
        noteRef.delete()
    }

 */
}