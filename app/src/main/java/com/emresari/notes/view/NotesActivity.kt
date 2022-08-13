package com.emresari.notes.view

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.Toast
import androidx.room.Room
import com.emresari.notes.databinding.ActivityNotesBinding
import com.emresari.notes.model.Notes
import com.emresari.notes.room.NotesDao
import com.emresari.notes.room.notesDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class NotesActivity : AppCompatActivity() {
    lateinit var compositeDisposable: CompositeDisposable
    lateinit var notesDao:NotesDao
    lateinit var db:notesDatabase
    lateinit var binding: ActivityNotesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityNotesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        compositeDisposable= CompositeDisposable()
        val intent=intent
        val selectedInfo=intent.getStringExtra("info")
        if(selectedInfo=="fromRow"){
            binding.layout.setBackgroundColor(Color.GRAY)
            binding.noteLayout.visibility=View.GONE
            binding.noteText.visibility=View.GONE
            binding.tittleLayout.visibility=View.GONE
            binding.tittleText.visibility=View.GONE
            binding.saveButton.visibility=View.GONE
            binding.textView.visibility=View.VISIBLE
            binding.textView3.visibility=View.VISIBLE
            val note=intent.getSerializableExtra("array" ) as Notes
            binding.textView.text=note.tittle
            binding.textView3.text=note.note
            binding.scrollId.visibility=View.VISIBLE





        }
        db=Room.databaseBuilder(applicationContext,notesDatabase::class.java,"Notes").build()
        notesDao=db.notesDao()

    }
    fun save(view: View){
        if(binding.noteText.text.toString()=="" || binding.tittleText.text.toString()==""){
            Toast.makeText(this,"Lütfen not ekleyiniz",Toast.LENGTH_SHORT).show()


        }

        else{
            val note=Notes(binding.tittleText.text.toString(),binding.noteText.text.toString())
                compositeDisposable.add(notesDao.insert(note)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleResponse))

        }



    }
    fun handleResponse(){
        val intent=Intent(this,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}