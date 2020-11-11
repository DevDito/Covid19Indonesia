package com.satriaamrudito.covid19indonesia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.satriaamrudito.covid19indonesia.adapter.AdapterProvince
import com.satriaamrudito.covid19indonesia.modul.DataItem
import com.satriaamrudito.covid19indonesia.modul.ResponseProvince
import com.satriaamrudito.covid19indonesia.network.ApiService
import com.satriaamrudito.covid19indonesia.network.RetrofitBuilder.retrofit_1
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    companion object{
        lateinit var adapterProvince : AdapterProvince
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        search_view.setOnQueryTextListener(object  : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapterProvince.filter.filter(newText)
                return false
            }
        })

        swipe_refresh.setOnRefreshListener {
            getProvince()
            swipe_refresh.isRefreshing = false
        }

        getProvince()
    }

    private fun getProvince(){
        val api = retrofit_1.create(ApiService::class.java)
        api.getAllProvince().enqueue(object : Callback<ResponseProvince> {
            override fun onFailure(call: Call<ResponseProvince>, t: Throwable) {
                progress_bar.visibility = View.GONE
            }

            override fun onResponse(
                    call: Call<ResponseProvince>,
                    response: Response<ResponseProvince>
            ) {
                if (response.isSuccessful){
                    rv_province.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(this@MainActivity)
                        progress_bar.visibility = View.GONE
                        adapterProvince = AdapterProvince(
                                response.body()!!.data as ArrayList<DataItem>
                        ){}
                        adapter = adapterProvince
                    }
                }else{
                    progress_bar?.visibility = View.GONE
                }
            }
        })
    }
}