package com.example.myproj.uI

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.myproj.R
import com.example.myproj.databinding.ListItemBinding
import com.example.myproj.loadDataFromInternet.ApiResult

class RvAdapter(
    private val context : Context,
    private val result : List<ApiResult>
) : RecyclerView.Adapter<RvAdapter.ViewHolder>(){
    inner class ViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun getItemCount(): Int {
        return result.size
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = result[position]
        holder.binding.apply {
            bindItems(currentItem)
        }
    }
    private fun ListItemBinding.bindItems(currentItem: ApiResult) {
        tvTitle.text = currentItem.webTitle
        tvSection.text = currentItem.sectionName
        tvText.text = currentItem.fields.trailText.replace("<.*?>".toRegex(), "")
        val date = currentItem.webPublicationDate.substring(0, 10)
        val time = currentItem.webPublicationDate.substring(11, 16)
        tvDate.text = "$date  $time"
        imageView.load(currentItem.fields.thumbnail) {
            placeholder(R.drawable.loadingicon)
            error(R.drawable.erroricon)
        }
        button.setOnClickListener {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                currentItem.webTitle + "\n" + currentItem.webUrl
            )
            shareIntent.type = "text/plain"
            context.startActivity(Intent.createChooser(shareIntent, "Share Via"))
        }
        tvTitle.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(currentItem.webUrl)
                context.startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(
                    context, "No application can handle this request."
                            + " Please install a web browser", Toast.LENGTH_LONG
                ).show()
                e.printStackTrace()
            }
        }
    }
}