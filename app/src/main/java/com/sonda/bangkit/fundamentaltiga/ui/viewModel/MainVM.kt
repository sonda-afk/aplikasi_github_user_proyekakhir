package com.sonda.bangkit.fundamentaltiga.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.sonda.bangkit.fundamentaltiga.model.User
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class MainVM : ViewModel() {

    companion object {
        const val TOKEN = "token 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"
    }

    private val arrayUser = MutableLiveData<ArrayList<User>>()
    private val arrayUserSearch = MutableLiveData<ArrayList<User>>()
    private val user = ArrayList<User>()

    fun fetchDataUser() {
        user.clear()
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users"

        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", TOKEN)

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                val dataApi = String(responseBody)
                try {
                    val arrayJson = JSONArray(dataApi)
                    var jsonObject: JSONObject
                    var id: String
                    for (i in 0 until arrayJson.length()) {
                        jsonObject = arrayJson.getJSONObject(i)
                        id = jsonObject.getString("login")

                        val clientProfile = AsyncHttpClient()
                        val urlProfile = "https://api.github.com/users/$id"

                        clientProfile.addHeader("User-Agent", "request")
                        clientProfile.addHeader("Authorization", TOKEN)

                        clientProfile.get(urlProfile, object : AsyncHttpResponseHandler() {
                            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray
                            ) {
                                val resultProfile = String(responseBody)
                                try {
                                    val jsonObjectProfile = JSONObject(resultProfile)
                                    val username: String?
                                    val name: String?
                                    val image: String?
                                    val company: String?
                                    val location: String?
                                    val repository: String?
                                    val followers: String?
                                    val following: String?

                                    image = jsonObjectProfile.getString("avatar_url").toString()
                                    username = jsonObjectProfile.getString("login").toString()
                                    name = jsonObjectProfile.getString("name").toString()
                                    location = jsonObjectProfile.getString("location").toString()
                                    repository = jsonObjectProfile.getString("public_repos")
                                    company = jsonObjectProfile.getString("company").toString()
                                    followers = jsonObjectProfile.getString("followers").toString()
                                    following = jsonObjectProfile.getString("following").toString()

                                    user.add(
                                            User(image, username, name, location, repository, company, following, followers)
                                    )

                                    arrayUser.postValue(user)

                                } catch (e: Exception) {
                                    Log.d("Exception", e.message.toString())
                                    e.printStackTrace()
                                }
                            }

                            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                                errorMessage(statusCode, error)
                            }
                        })


                    }
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                errorMessage(statusCode, error)
            }
        })
    }


    fun fetchDataUserSearch(query: String) {
        user.clear()
        val client = AsyncHttpClient()
        val url = "https://api.github.com/search/users?q=$query"

        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", TOKEN)

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                val dataApi = String(responseBody)
                try {
                    val jsonArray = JSONObject(dataApi)
                    val row = jsonArray.getJSONArray("items")
                    var jsonObject: JSONObject
                    var id: String
                    for (i in 0 until row.length()) {
                        jsonObject = row.getJSONObject(i)
                        id = jsonObject.getString("login")

                        val clientProfile = AsyncHttpClient()
                        val urlProfile = "https://api.github.com/users/$id"

                        clientProfile.addHeader("User-Agent", "request")
                        clientProfile.addHeader("Authorization", TOKEN)

                        clientProfile.get(urlProfile, object : AsyncHttpResponseHandler() {
                            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray
                            ) {
                                val resultProfile = String(responseBody)
                                try {
                                    val jsonObjectProfile = JSONObject(resultProfile)
                                    val username: String?
                                    val name: String?
                                    val image: String?
                                    val company: String?
                                    val location: String?
                                    val repository: String?
                                    val followers: String?
                                    val following: String?

                                    image = jsonObjectProfile.getString("avatar_url").toString()
                                    username = jsonObjectProfile.getString("login").toString()
                                    name = jsonObjectProfile.getString("name").toString()
                                    location = jsonObjectProfile.getString("location").toString()
                                    repository = jsonObjectProfile.getString("public_repos")
                                    company = jsonObjectProfile.getString("company").toString()
                                    followers = jsonObjectProfile.getString("followers").toString()
                                    following = jsonObjectProfile.getString("following").toString()

                                    user.add(
                                            User(image, username, name, location, repository, company, following, followers)
                                    )

                                    arrayUserSearch.postValue(user)

                                } catch (e: Exception) {
                                    Log.d("Exception", e.message.toString())
                                    e.printStackTrace()
                                }
                            }

                            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                                errorMessage(statusCode, error)
                            }
                        })

                    }
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                errorMessage(statusCode, error)
            }
        })
    }

    fun getUser(): LiveData<ArrayList<User>> = arrayUser

    fun getUserSearch(): LiveData<ArrayList<User>> = arrayUserSearch

    private fun errorMessage(code: Int, error: Throwable) {
        val message = if (code == 401) {
            "$code : Bad Request"
        } else if (code == 402) {
            "$code : Forbidden"
        } else if (code == 402) {
            "$code : Not Found"
        } else {
            "$code : ${error.message}"
        }
        Log.d("Errors", message.toString())
    }

}