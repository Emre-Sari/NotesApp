package com.emresari.notes.view

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import androidx.appcompat.app.AppCompatActivity

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.emresari.notes.adapter.NotesAdapter
import com.emresari.notes.databinding.ActivityMainBinding
import com.emresari.notes.model.Notes
import com.emresari.notes.room.NotesDao
import com.emresari.notes.room.notesDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var compositeDisposable: CompositeDisposable
    lateinit var db:notesDatabase
    lateinit var notesDao:NotesDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        compositeDisposable= CompositeDisposable()
        db=Room.databaseBuilder(applicationContext,notesDatabase::class.java,"Notes").build()
        notesDao=db.notesDao()

        getData()


        binding.fab.setOnClickListener {
            val intent=Intent(this, NotesActivity::class.java)
            startActivity(intent)

        }


    }
    fun getData(){
        compositeDisposable.add(notesDao.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleResponse))




    }
    fun handleResponse(list:List<Notes>){
        binding.recyclerView.layoutManager=LinearLayoutManager(this)
        val adapter=NotesAdapter(list)
        binding.recyclerView.adapter=adapter


    }

    override fun onDestroy() {
            super.onDestroy()
        compositeDisposable.clear()

    }


}