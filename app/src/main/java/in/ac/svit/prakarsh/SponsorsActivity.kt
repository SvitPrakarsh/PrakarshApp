package `in`.ac.svit.prakarsh

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_sponsors.*
import kotlinx.android.synthetic.main.item_sponsor.view.*
import org.json.JSONArray
import java.util.*

/**
 * Created by itsarjunsinh on 1/22/18.
 */
class SponsorsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sponsors)
        Log.d(javaClass.name, "Started")

        supportActionBar?.title = getString(R.string.title_sponsors)
        updateViewsFromJson()
    }

    private fun updateViewsFromJson() {

        val url = applicationContext?.getString(R.string.url_sponsors)
        val req = JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener { response ->
                    Log.d(javaClass.name, "JSON Successfully fetched")
                    val jsonArray: JSONArray = response.getJSONArray("sponsors")
                    var sponsorsDataAdapterList: ArrayList<SponsorsDataAdapter> = ArrayList()
                    for (i in 0..(jsonArray.length() - 1)) {
                        val name = jsonArray.getJSONObject(i).getString("name")
                        val description = jsonArray.getJSONObject(i).getString("description")
                        val imageUrl = jsonArray.getJSONObject(i).getString("imageUrl")
                        val websiteUrl = jsonArray.getJSONObject(i).getString("websiteUrl")
                        sponsorsDataAdapterList.add(SponsorsDataAdapter(name, description, imageUrl, websiteUrl))
                    }


                    sponsors_rv_main?.apply {
                        layoutManager = GridLayoutManager(applicationContext, 2)
                        adapter = SponsorsRecyclerAdapter(applicationContext, sponsorsDataAdapterList)
                    }
                }, Response.ErrorListener { error ->
            Log.d(javaClass.name, "Volley Response Error Occurred, URL: $url Error: ${error.message}")
        })

        VolleySingleton.getInstance(applicationContext).requestQueue.add(req.setShouldCache(false))
    }

    class SponsorsDataAdapter(val name: String, val description: String, val imageUrl: String, val websiteUrl: String)

    class SponsorsRecyclerAdapter(private val context: Context, private val sponsorsDataAdapterList: ArrayList<SponsorsDataAdapter>) : RecyclerView.Adapter<SponsorsRecyclerAdapter.CustomViewHolder>() {

        class CustomViewHolder(val view: View) : RecyclerView.ViewHolder(view)

        override fun getItemCount(): Int {
            return sponsorsDataAdapterList.size
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CustomViewHolder {
            val layoutInflater = LayoutInflater.from(parent?.context)
            val cellForRow = layoutInflater.inflate(R.layout.item_sponsor, parent, false)
            return CustomViewHolder(cellForRow)
        }

        override fun onBindViewHolder(holder: CustomViewHolder?, position: Int) {
            holder?.view?.sponsor_name?.text = sponsorsDataAdapterList[position].name
            holder?.view?.sponsor_img_main?.apply {
                setDefaultImageResId(R.drawable.ic_image_black)
                setErrorImageResId(R.drawable.ic_broken_image_black)
                setImageUrl(sponsorsDataAdapterList[position].imageUrl, VolleySingleton.getInstance(context).imageLoader)
            }

            holder?.view?.setOnClickListener {
                Log.d(javaClass.name, "${sponsorsDataAdapterList[position].name} Clicked")
                val website = Uri.parse(sponsorsDataAdapterList[position].websiteUrl)
                var intent = Intent(Intent.ACTION_VIEW, website)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                try {
                    context?.startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    Log.d(javaClass.name, "Exception occurred while opening pdf in web browser.", e)
                    Toast.makeText(context, "No suitable application found to open file.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}