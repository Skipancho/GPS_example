package com.jjsh.gpsexample.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jjsh.gpsexample.App
import com.jjsh.gpsexample.R
import com.jjsh.gpsexample.databinding.StationItemBinding
import com.jjsh.gpsexample.datas.SubwayStation

class SubwayStationAdapter: RecyclerView.Adapter<SubwayStationAdapter.SubwayStationDataViewHolder>(){

    private val list  = mutableListOf<SubwayStation>()
    var onClick : () -> Unit = {}

    inner class SubwayStationDataViewHolder(
        private val binding : StationItemBinding
    ) : RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(item : SubwayStation){
            binding.nameTv.text = "${item.name} ${item.line}"
            binding.distTv.text = if (item.distance > 1000.0){
                val dist = (item.distance/100).toInt() / 10.0
                "${dist}km"
            }else{
                "${(item.distance*100).toInt()/100.0}m"
            }
            binding.root.setOnClickListener {
                App.selectedPosition = doubleArrayOf(item.latitude,item.longitude)
                onClick()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(items : List<SubwayStation>){
        list.clear()
        list.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubwayStationDataViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.station_item,parent,false)
        val binding = StationItemBinding.bind(view)
        return SubwayStationDataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubwayStationDataViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = list.size
}