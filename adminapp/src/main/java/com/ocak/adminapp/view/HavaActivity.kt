package com.ocak.adminapp.view

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import com.ocak.adminapp.R
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HavaActivity : AppCompatActivity() {


    val CITY: String = "Ordu, TR"
    val API: String = "4f8f38ff3efb6d963ec1d2ae3e9dda04"
    val engWeatherList  = arrayOf("clear sky", "few clouds","overcast clouds", "scattered clouds", "broken clouds", "shower rain", "rain", "thunderstorm","snow","mist")
    val trWeatherList = arrayOf("Güneşli", "Az Bulutlu","Çok Bulutlu(Kapalı)", "Alçak Bulutlu", "Yer Yer Açık Bulutlu", "Sağanak Yağmurlu", "Yağmurlu", "Gök Gürültülü Fırtına", "Karlı", "Puslu")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hava)

        weatherTask().execute()

    }

    inner class weatherTask() : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()

            findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
            findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.GONE
            findViewById<TextView>(R.id.errorText).visibility = View.GONE

        }

        override fun doInBackground(vararg params: String?): String? {
            var response:String?
            try{
                response = URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&appid=$API").readText(
                    Charsets.UTF_8
                )
            }catch (e: Exception){
                response = null
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {

                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)

                val updatedAt:Long = jsonObj.getLong("dt")
                val updatedAtText = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(Date(updatedAt*1000))+" "+"anında güncellendi."
                val temp = main.getString("temp")+"°C"
                val tempMin = "En Düşük Sıcaklık: "+ main.getString("temp_min")+"°C"
                val tempMax = "En Yüksek Sıcaklık: " + main.getString("temp_max")+"°C"
                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity")

                val sunrise:Long = sys.getLong("sunrise")
                val sunset:Long = sys.getLong("sunset")
                val windSpeed = wind.getString("speed")
                var weatherDescription = weather.getString("description")


                if (weatherDescription == engWeatherList.get(0)){
                    weatherDescription = trWeatherList.get(0)
                }
                if (weatherDescription == engWeatherList.get(1)){
                    weatherDescription = trWeatherList.get(1)
                }
                if (weatherDescription == engWeatherList.get(2)){
                    weatherDescription = trWeatherList.get(2)
                }
                if (weatherDescription == engWeatherList.get(3)){
                    weatherDescription = trWeatherList.get(3)
                }
                if (weatherDescription == engWeatherList.get(4)){
                    weatherDescription = trWeatherList.get(4)
                }
                if (weatherDescription == engWeatherList.get(5)){
                    weatherDescription = trWeatherList.get(5)
                }
                if (weatherDescription == engWeatherList.get(6)){
                    weatherDescription = trWeatherList.get(6)
                }
                if (weatherDescription == engWeatherList.get(7)){
                    weatherDescription = trWeatherList.get(7)
                }
                if (weatherDescription == engWeatherList.get(8)){
                    weatherDescription = trWeatherList.get(8)
                }
                if (weatherDescription == engWeatherList.get(9)){
                    weatherDescription = trWeatherList.get(9)
                }

                val address = jsonObj.getString("name")+", "+sys.getString("country")


                findViewById<TextView>(R.id.address).text = address
                findViewById<TextView>(R.id.updated_at).text =  updatedAtText
                findViewById<TextView>(R.id.status).text = weatherDescription.capitalize(Locale.getDefault())
                findViewById<TextView>(R.id.temp).text = temp
                findViewById<TextView>(R.id.temp_min).text = tempMin
                findViewById<TextView>(R.id.temp_max).text = tempMax
                findViewById<TextView>(R.id.sunrise).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise*1000))
                findViewById<TextView>(R.id.sunset).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset*1000))
                findViewById<TextView>(R.id.wind).text = windSpeed
                findViewById<TextView>(R.id.pressure).text = pressure
                findViewById<TextView>(R.id.humidity).text = humidity


                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.VISIBLE

            } catch (e: Exception) {
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<TextView>(R.id.errorText).visibility = View.VISIBLE
            }

        }
    }


}