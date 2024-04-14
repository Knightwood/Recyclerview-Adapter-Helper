package com.kiylx.simplerecyclerview

import android.app.Application
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.kiylx.recyclerviewneko.createNormalAdapterConfig
import com.kiylx.recyclerviewneko.myadapter.config.ViewTypeParser
import com.kiylx.recyclerviewneko.viewholder.BaseViewHolder
import com.kiylx.recyclerviewneko.viewholder.ItemViewDelegate
import com.kiylx.recyclerviewneko.viewholder.pack
import com.kiylx.simplerecyclerview.databinding.Item1Binding

/** 使用最普通的适配器为例； 1，ItemViewDelegate代理了各种不同的ViewHolder实现 2，快速构建一个适配器 */
class NormalAdapterExample(val application: Application) {
}