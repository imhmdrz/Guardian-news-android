package com.example.myproj.uI

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.myproj.R
import com.example.myproj.databinding.ListItemBinding
import com.example.myproj.loadDataFromInternet.ApiResult

class RvAdapter(
    private val resualt : List<ApiResult>
) : RecyclerView.Adapter<RvAdapter.ViewHolder>(){
    inner class ViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return resualt.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = resualt[position]
        holder.binding.apply {
            tvTitle.text = currentItem.webTitle
            tvSection.text = currentItem.sectionName
            val date = currentItem.webPublicationDate.substring(0,10)
            val time = currentItem.webPublicationDate.substring(11,16)
            tvDate.text = "$date  $time"
            imageView.load(currentItem.fields.thumbnail) {
                placeholder(R.drawable.loadingicon)
                error(R.drawable.erroricon)
            }
        }
    }
}