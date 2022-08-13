package com.emresari.notes.adapter

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.Intent.getIntent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.room.RoomDatabase
import com.emresari.notes.databinding.RecyclerRowBinding
import com.emresari.notes.model.Notes
import com.emresari.notes.room.NotesDao
import com.emresari.notes.room.notesDatabase
import com.emresari.notes.view.MainActivity
import com.emresari.notes.view.NotesActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.Serializable

class NotesAdapter(var array:List<Notes>) : RecyclerView.Adapter<NotesHolder>()  {
    lateinit var compositeDisposable: CompositeDisposable
    lateinit var notesdao: NotesDao
    lateinit var db:notesDatabase


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesHolder {

        val binding=RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NotesHolder(binding)
    }


    override fun onBindViewHolder(holder: NotesHolder, position: Int) {
        compositeDisposable=CompositeDisposable()
        db=Room.databaseBuilder(holder.itemView.context,notesDatabase::class.java,"Notes").build()
        notesdao=db.notesDao()
        holder.binding.itemId.text=array.get(position).tittle
        val toMain=Intent(holder.itemView.context,MainActivity::class.java)
        holder.itemView.setOnClickListener {
            val intent=Intent(holder.itemView.context,NotesActivity::class.java)
            intent.putExtra("array", array.get(position))
            intent.putExtra("info","fromRow")
            holder.itemView.context.startActivity(intent)
        }

        holder.itemView.setOnLongClickListener {
            val alertDialog=AlertDialog.Builder(holder.itemView.context)
            alertDialog.setTitle("Delete")
                .setCancelable(false)
                .setMessage("Do you want delete ? ")
                .setPositiveButton("Yes",DialogInterface.OnClickListener { dialogInterface, i ->

                    compositeDisposable.add(notesdao.delete(array.get(position))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::handleResponse))
                    holder.itemView.context.startActivity(toMain)






                })
                .setNegativeButton("No",DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.cancel()

                }).create().show()





            return@setOnLongClickListener false
        }




    }

    fun handleResponse(){
        notifyDataSetChanged()

    }
    override fun getItemCount(): Int {
        return array.size
    }

}