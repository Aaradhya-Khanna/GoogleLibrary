package com.example.pageflipper

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.gtappdevelopers.kotlingfgproject.BookRVAdapter

class MainActivity : AppCompatActivity() {

    // on below line we are creating variables.
    lateinit var mRequestQueue: RequestQueue
    lateinit var booksList: ArrayList<BookDetails>
    lateinit var loadingPB: ProgressBar
    var str=""
    lateinit var prog: Button
    lateinit var his:Button
    lateinit var comm:Button
    lateinit var law:Button
    lateinit var ai:Button
    lateinit var cps:Button
    lateinit var iot:Button
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        // on below line we are initializing
        // our variable with their ids.
        loadingPB = findViewById(R.id.idLoadingPB)
        prog=findViewById(R.id.prog)
        his=findViewById(R.id.his)
        comm=findViewById(R.id.comm)
        law=findViewById(R.id.law)
        ai=findViewById(R.id.ai)
        cps=findViewById(R.id.cps)
        iot=findViewById(R.id.iot)
        loadingPB.visibility = View.VISIBLE
        prog.setOnClickListener()
        {
            str="Programming"
            getBooksData(str);
        }
        his.setOnClickListener()
        {
            str="History"
            getBooksData(str);
        }
        comm.setOnClickListener()
        {
            str="Commerce"
            getBooksData(str);
        }
        law.setOnClickListener()
        {
            str="Law"
            getBooksData(str);
        }
        ai.setOnClickListener()
        {
            str="Artificial Intelligence"
            getBooksData(str);
        }
        cps.setOnClickListener()
        {
            str="Cyber Physical System"
            getBooksData(str);
        }
        iot.setOnClickListener()
        {
            str="Internet of things"
            getBooksData(str);
        }



    }

    private fun getBooksData(searchQuery: String) {
        // creating a new array list.
        booksList = ArrayList()

        // below line is use to initialize
        // the variable for our request queue.
        mRequestQueue = Volley.newRequestQueue(this@MainActivity)

        // below line is use to clear cache this
        // will be use when our data is being updated.
        mRequestQueue.cache.clear()

        // below is the url for getting data from API in json format.
        val url = "https://www.googleapis.com/books/v1/volumes?q=$searchQuery"

        // below line we are creating a new request queue.
        val queue = Volley.newRequestQueue(this@MainActivity)

        // on below line we are creating a variable for request
        // and initializing it with json object request
        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            loadingPB.setVisibility(View.GONE);
            // inside on response method we are extracting all our json data.
            try {
                val itemsArray = response.getJSONArray("items")
                for (i in 0 until itemsArray.length()) {
                    val itemsObj = itemsArray.getJSONObject(i)
                    val volumeObj = itemsObj.getJSONObject("volumeInfo")
                    val title = volumeObj.optString("title")
                    val subtitle = volumeObj.optString("subtitle")
                    val authorsArray = volumeObj.getJSONArray("authors")
                    val publisher = volumeObj.optString("publisher")
                    val publishedDate = volumeObj.optString("publishedDate")
                    val description = volumeObj.optString("description")
                    val pageCount = volumeObj.optInt("pageCount")
                    val imageLinks = volumeObj.optJSONObject("imageLinks")
                    val thumbnail = imageLinks.optString("thumbnail")
                    val previewLink = volumeObj.optString("previewLink")
                    val infoLink = volumeObj.optString("infoLink")
                    val saleInfoObj = itemsObj.optJSONObject("saleInfo")
                    val buyLink = saleInfoObj.optString("buyLink")
                    val authorsArrayList: ArrayList<String> = ArrayList()
                    if (authorsArray.length() != 0) {
                        for (j in 0 until authorsArray.length()) {
                            authorsArrayList.add(authorsArray.optString(i))
                        }
                    }

                    // after extracting all the data we are
                    // saving this data in our modal class.
                    val bookInfo = BookDetails(
                        title,
                        subtitle,
                        authorsArrayList,
                        publisher,
                        publishedDate,
                        description,
                        pageCount,
                        thumbnail,
                        previewLink,
                        infoLink,
                        buyLink
                    )

                    // below line is use to pass our modal
                    // class in our array list.
                    booksList.add(bookInfo)

                    // below line is use to pass our
                    // array list in adapter class.
                    val adapter = BookRVAdapter(booksList, this@MainActivity)

                    // below line is use to add linear layout
                    // manager for our recycler view.
                    val layoutManager = GridLayoutManager(this, 3)
                    val mRecyclerView = findViewById<RecyclerView>(R.id.list) as RecyclerView

                    // in below line we are setting layout manager and
                    // adapter to our recycler view.
                    mRecyclerView.layoutManager = layoutManager
                    mRecyclerView.adapter = adapter
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }


        }, { error ->
            // in this case we are simply displaying a toast message.
            Toast.makeText(this@MainActivity, "No books found..", Toast.LENGTH_SHORT)
                .show()
        })
        // at last we are adding our
        // request to our queue.
        queue.add(request)

    }
}




