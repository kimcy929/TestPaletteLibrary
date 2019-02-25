package com.example.testpalettelibrary.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.testpalettelibrary.R

class PaletteAdapter(private val onClickListener: RecyclerViewOnClickItem)
    : RecyclerView.Adapter<PaletteAdapter.ViewHolder>() {

    private var selectedCurrPosition = -1
    private var selectedBeforePosition = -1

    private var colorList = emptyList<Int>()

    fun addData(colors: List<Int>) {
        colorList = colors
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.circle_color_item, parent, false))
    }

    override fun getItemCount() = colorList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val layerDrawable = ContextCompat.getDrawable(itemView.context, R.drawable.circle_background) as LayerDrawable
        private val borderWidth = itemView.resources.getDimensionPixelSize(R.dimen.circle_border_width)
        private val solidLayerOffset = borderWidth * 3

        init {
            itemView.setOnClickListener {
                selectedCurrPosition = adapterPosition
                if (selectedBeforePosition != selectedCurrPosition) {
                    if (selectedBeforePosition != -1) { // don't need check validate position
                        notifyItemChanged(selectedBeforePosition)
                    }
                    selectedBeforePosition = selectedCurrPosition
                } else {
                    selectedBeforePosition = -1
                    selectedCurrPosition = -1
                    notifyItemChanged(adapterPosition)
                }

                if (selectedCurrPosition != -1) {
                    notifyItemChanged(selectedCurrPosition)
                }

                onClickListener.onClick(colorList[adapterPosition])
            }
        }

        fun bindTo(position: Int) {
            val color = colorList[position]

            layerDrawable.getLayer(R.id.solidLayer).setColor(color)

            layerDrawable.getLayer(R.id.strokeLayer).apply {
                if (selectedCurrPosition == position) {
                    setStroke(borderWidth, color)
                } else {
                    setStroke(borderWidth, Color.TRANSPARENT)
                }
            }

            solidLayerOffset.run {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    layerDrawable.setLayerInsetRelative(1, this, this, this, this)
                } else {
                    layerDrawable.setLayerInset(1, this, this, this, this)
                }
            }

            (itemView as AppCompatImageView).setImageDrawable(layerDrawable)
        }

        private fun LayerDrawable.getLayer(@IdRes id: Int) = findDrawableByLayerId(id) as GradientDrawable
    }

    interface RecyclerViewOnClickItem {
        fun onClick(color: Int)
    }
}