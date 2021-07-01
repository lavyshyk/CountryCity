package com.lavyshyk.countrycity.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.lavyshyk.countrycity.R
import com.lavyshyk.countrycity.data.CountryData
import com.lavyshyk.countrycity.ui.CountryAdapter.ViewHolder

class CountryAdapter(dataCountry: MutableList<CountryData>): RecyclerView.Adapter<ViewHolder>() {
//    lateinit var fragmentListBinding: FragmentListBinding
//    lateinit var fragmentItemBinding: FragmentItemBinding
//    var binding: FragmentItemBinding = fragmentItemBinding

   //private val data = listOf<String>("Англия","Беларусь","Венгрия","Дания")
    private var data = dataCountry




    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val  tvCountry: AppCompatTextView = view.findViewById(R.id.textViewCountry)
        val  tvCapital: AppCompatTextView = view.findViewById(R.id.textViewCapital)
        val  tvLanguages: AppCompatTextView = view.findViewById(R.id.textViewLang)
        //val  tv3alpha: AppCompatTextView = view.findViewById(R.id.text_3alpha)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //fragmentItemBinding = FragmentItemBinding.bind(parent)
        //return ViewHolder(fragmentItemBinding.root)
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_item,parent,false)
       return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = data[position]
        holder.tvCountry.text = "${list.name}"
      //  holder.tv3alpha.text = list.alpha3Code
        holder.tvCapital.text = "capital is ${list.capital}"
        holder.tvLanguages.text = "languages : ${list.languages
            .let { 
                it.map { it.name }
                    .joinToString(", ")
            }
        }"

    }

    override fun getItemCount(): Int {
//        return try {
//            data.size
//        }catch (e:Exception){
//            0
//        }
        return data.size
    }
}