package com.turskyi.gallery

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager
import com.turskyi.gallery.adapter.RecyclerAdapter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var quantityOfColumns: Int = 1

    private val PERMISSION_READ_STATE = 10001

    lateinit var aRecyclerView: RecyclerView

    lateinit var aFolderList: IntArray

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_READ_STATE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(this,"permission allowed", Toast.LENGTH_SHORT).show()
                } else {
                    restart()
                }
                return
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) getPermission()

        increment.setOnClickListener {
            increment()
        }

        decrement.setOnClickListener {
            decrement()
        }

        aRecyclerView  = findViewById(R.id.rv)

        getNumbersOfColumns()

        aFolderList = intArrayOf(
            R.drawable.any_language,
            R.drawable.balcony,
            R.drawable.cabinet,
            R.drawable.edward_hopper_rooms_by_the_sea,
            R.drawable.gun,
            R.drawable.projector
        )

        val aRandomList = Array(30) {aFolderList.random()}

        val recyclerAdapter = RecyclerAdapter(
            this@MainActivity,
            aRandomList
        )
        aRecyclerView.adapter = recyclerAdapter
    }

    private fun getNumbersOfColumns() {
        val aGridLayoutManager = GridLayoutManager(this@MainActivity, quantityOfColumns)
        aRecyclerView.layoutManager = aGridLayoutManager
    }

    /**
     * This method is called when the plus button is clicked.
     */
    private fun increment() {
        if (quantityOfColumns == 10) {
            return
        }
        quantityOfColumns += 1
        displayQuantity(quantityOfColumns)
    }

    /**
     * This method is called when the minus button is clicked.
     */
    private fun decrement() {
        if (quantityOfColumns == 1) {
            return
        }
        quantityOfColumns -= 1
        displayQuantity(quantityOfColumns)
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private fun displayQuantity(quantityOfColumns: Int) {
        quantity_text_view.text = "$quantityOfColumns"
        getNumbersOfColumns()
    }

    private fun restart() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun getPermission() {
        ActivityCompat.requestPermissions(
            this,
            listOf(Manifest.permission.WRITE_EXTERNAL_STORAGE).toTypedArray(),
            PERMISSION_READ_STATE
        )
    }
}
