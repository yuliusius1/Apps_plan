package com.coba.cobaya

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns
import android.view.View
import android.widget.Toast
import com.coba.cobaya.database.DbContract
import com.coba.cobaya.database.ReaderDbHelper
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    lateinit var dbHelper: ReaderDbHelper
    lateinit var db : SQLiteDatabase
    lateinit var data : DataModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        dbHelper = ReaderDbHelper(this)
        db = dbHelper.writableDatabase

        var status = intent.getBooleanExtra("status",false)
        if(status) {
            initListener()
        } else {
            data = intent.getParcelableExtra("data")!!

            et_title.setText(data.title)
            et_desc.setText(data.desc)

            btn_delete.visibility = View.VISIBLE
            btn_save.text = "Edit Plan"
            initListenerEdit()
        }
    }

    private fun initListener() {
        btn_save.setOnClickListener{
            var sTitle = et_title.text.toString()
            var sDesc = et_desc.text.toString()

            if(sTitle.isNullOrEmpty()){
                et_title.error = "Silahkan masukkan title"
                et_title.requestFocus()
            } else if(sDesc.isNullOrEmpty()){
                et_desc.error = "Silahkan masukkan Desc"
                et_desc.requestFocus()
            } else {
                val values = ContentValues().apply {
                    put(DbContract.DataEntry.COLUMN_NAMA_TITLE, sTitle)
                    put(DbContract.DataEntry.COLUMN_NAMA_DESC, sDesc)
                }

                val newRowId = db.insert(DbContract.DataEntry.TABLE_NAME,null ,values)
                if(newRowId == -1L){
                    Toast.makeText(this, "Data gagal disimpan", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private fun initListenerEdit(){

        btn_delete.setOnClickListener{
            val selection = "${BaseColumns._ID} LIKE ?"
            val selectionArg = arrayOf(data.id.toString())

            val deleteRows = db.delete(DbContract.DataEntry.TABLE_NAME, selection, selectionArg)
            if(deleteRows == -1){
                Toast.makeText(this, "Data gagal dihapus", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        btn_save.setOnClickListener{
            var sTitle = et_title.text.toString()
            var sDesc = et_desc.text.toString()

            if(sTitle.isNullOrEmpty()){
                et_title.error = "Silahkan masukkan title"
                et_title.requestFocus()
            } else if(sDesc.isNullOrEmpty()){
                et_desc.error = "Silahkan masukkan Desc"
                et_desc.requestFocus()
            } else {

                val selection = "${BaseColumns._ID} LIKE ?"
                val selectionArg = arrayOf(data.id.toString())

                val values = ContentValues().apply {
                    put(DbContract.DataEntry.COLUMN_NAMA_TITLE, sTitle)
                    put(DbContract.DataEntry.COLUMN_NAMA_DESC, sDesc)
                }

                val editRows = db.update(DbContract.DataEntry.TABLE_NAME,values ,selection,selectionArg)

                if(editRows == -1){
                    Toast.makeText(this, "Data gagal diubah", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Data berhasil diubah", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}