package myjin.pro.ahoora.myjin.utils

import android.content.Context
import android.util.Log
import android.widget.Toast

import myjin.pro.ahoora.myjin.interfaces.ServerStatusResponse
import myjin.pro.ahoora.myjin.models.TempModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServerStatus(private val serverStatusResponse: ServerStatusResponse, private val mContext: Context) {
    private var v = ""

    fun checkServer() {
        try {
            KotlinApiClient.client.create(ApiInterface::class.java).IsOkServer().enqueue(object : Callback<TempModel> {
                override fun onResponse(call: Call<TempModel>, response: Response<TempModel>) {

                    try {
                        val tempModel = response.body()!!
                        v = tempModel.getVal()
                        Log.e("val", v + "")
                        VarableValues.NetworkState = true
                        if (v == "ok") {
                            serverStatusResponse.isOk()
                        } else {
                            serverStatusResponse.notOk()
                        }
                    } catch (e: Exception) {
                        VarableValues.NetworkState = true
                        serverStatusResponse.notOk()
                    }

                }

                override fun onFailure(call: Call<TempModel>, t: Throwable) {
                    VarableValues.NetworkState = false
                    serverStatusResponse.notOk()
                }
            })

        } catch (e: Exception) {
            Toast.makeText(mContext, "خطایی رخ داده است", Toast.LENGTH_SHORT).show()
            serverStatusResponse.notOk()
        }

    }

}
