package `in`.ac.svit.prakarsh

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_team_info.*
import kotlinx.android.synthetic.main.item_team_info.view.*
import org.json.JSONArray
import java.util.*

/**
 * Created by itsarjunsinh on 1/16/18.
 */
class TeamInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_info)
        Log.d(javaClass.name, "Started")

        // If dataUrl is received load views otherwise go to Main Activity.
        val dataUrl = intent.getStringExtra("url")
        if (dataUrl != null) {
            updateViewsFromJson(dataUrl)
        } else {
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }

    private fun updateViewsFromJson(url: String) {

        val req = JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener { response ->
                    Log.d(javaClass.name, "JSON Successfully fetched")

                    val jsonArray: JSONArray = response.getJSONArray("members")
                    var dataAdapterList: ArrayList<TeamInfoDataAdapter> = ArrayList()

                    for (i in 0..(jsonArray.length() - 1)) {

                        var name = ""
                        var role = ""
                        var department = ""
                        var imageUrl = ""

                        if (jsonArray.getJSONObject(i).has("name")) {
                            name = jsonArray.getJSONObject(i).getString("name")
                        }

                        if (jsonArray.getJSONObject(i).has("role")) {
                            role = jsonArray.getJSONObject(i).getString("role")
                        }

                        if (jsonArray.getJSONObject(i).has("department")) {
                            department = jsonArray.getJSONObject(i).getString("department")
                        }

                        if (jsonArray.getJSONObject(i).has("imageUrl")) {
                            imageUrl = jsonArray.getJSONObject(i).getString("imageUrl")
                        }

                        dataAdapterList.add(TeamInfoDataAdapter(name, role, department, imageUrl))
                    }

                    team_info_rv_main?.apply {
                        layoutManager = GridLayoutManager(applicationContext, 2)
                        adapter = TeamInfoRecyclerAdapter(applicationContext, dataAdapterList)
                    }
                }, Response.ErrorListener { error ->
            Log.d(javaClass.name, "Volley Response Error Occurred, URL: $url Error: ${error.message}")
        })

        VolleySingleton.getInstance(applicationContext).requestQueue.add(req.setShouldCache(false))
    }

    class TeamInfoDataAdapter(val name: String, val role: String, val department: String, val imageUrl: String)

    class TeamInfoRecyclerAdapter(private val context: Context, private val dataAdapterList: ArrayList<TeamInfoDataAdapter>) : RecyclerView.Adapter<TeamInfoRecyclerAdapter.CustomViewHolder>() {

        class CustomViewHolder(val view: View) : RecyclerView.ViewHolder(view)

        override fun getItemCount(): Int {
            return dataAdapterList.size
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CustomViewHolder {
            val layoutInflater = LayoutInflater.from(parent?.context)
            val cellForRow = layoutInflater.inflate(R.layout.item_team_info, parent, false)
            return CustomViewHolder(cellForRow)
        }

        override fun onBindViewHolder(holder: CustomViewHolder?, position: Int) {
            Log.d(javaClass.name, "${dataAdapterList[position].name} view")
            holder?.view?.team_info_txt_name?.text = dataAdapterList[position].name
            holder?.view?.team_info_txt_role?.text = dataAdapterList[position].role
            holder?.view?.team_info_txt_department?.text = dataAdapterList[position].department
            holder?.view?.team_info_img_member?.apply {
                setDefaultImageResId(R.drawable.ic_image_black)
                setErrorImageResId(R.drawable.ic_broken_image_black)
                setImageUrl(dataAdapterList[position].imageUrl, VolleySingleton.getInstance(context).imageLoader)
            }
        }
    }
}