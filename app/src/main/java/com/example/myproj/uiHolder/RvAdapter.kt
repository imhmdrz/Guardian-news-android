package com.example.myproj.uiHolder

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.paging.LoadState
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.myproj.R
import com.example.myproj.databinding.ListItemBinding
import com.example.myproj.model.ApiResult
import androidx.paging.PagingDataAdapter



class RvPagingAdapter(private val context : Context,
                      private val section: String? = null
) : PagingDataAdapter<ApiResult, RvPagingAdapter.ViewHolder>(diffCallback) {
    inner class ViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(item: ApiResult) {
            binding.apply {
                tvTitle.text = item.webTitle
                tvSection.text = section
                tvText.text = item.fields.trailText.replace("<.*?>".toRegex(), "")
                val date = item.webPublicationDate.substring(0, 10)
                val time = item.webPublicationDate.substring(11, 16)
                tvDate.text = "$date  $time"
                imageView.load(item.fields.thumbnail) {
                    placeholder(R.drawable.loadingicon)
                    error(R.drawable.erroricon)
                }
                bindButton(item)
            }
        }

        private fun ListItemBinding.bindButton(item: ApiResult) {
            button.setOnClickListener {
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    item.webTitle + "\n" + item.webUrl
                )
                shareIntent.type = "text/plain"
                context.startActivity(Intent.createChooser(shareIntent, "Share Via"))
            }
            tvTitle.setOnClickListener {
                try {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(item.webUrl)
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
    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<ApiResult>() {
            override fun areItemsTheSame(oldItem: ApiResult, newItem: ApiResult): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: ApiResult, newItem: ApiResult): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(it)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }
}
