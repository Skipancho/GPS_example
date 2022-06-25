package com.jjsh.gpsexample.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jjsh.gpsexample.R
import com.jjsh.gpsexample.databinding.StationItemBinding
import com.jjsh.gpsexample.datas.SubwayStation

class SubwayStationAdapter(
    private val list : List<SubwayStation>
) : RecyclerView.Adapter<SubwayStationAdapter.SubwayStationDataViewHolder>(){

    class SubwayStationDataViewHolder(
        private val binding : StationItemBinding
    ) : RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(item : SubwayStation){
            binding.nameTv.text = "${item.name} ${item.line}"
            binding.distTv.text = "${item.distance}m"
        }
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