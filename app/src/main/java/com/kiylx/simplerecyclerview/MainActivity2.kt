package com.kiylx.simplerecyclerview

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kiylx.recyclerviewneko.listNeko
import com.kiylx.recyclerviewneko.neko
import com.kiylx.recyclerviewneko.nekoadapter.ItemClickListener
import com.kiylx.recyclerviewneko.nekoadapter.ItemLongClickListener
import com.kiylx.recyclerviewneko.nekoadapter.NekoAdapter
import com.kiylx.recyclerviewneko.nekoadapter.config.ViewTypeParser
import com.kiylx.recyclerviewneko.show
import com.kiylx.recyclerviewneko.viewholder.BaseViewHolder
import com.kiylx.recyclerviewneko.viewholder.ItemViewDelegate


class MainActivity2 : AppCompatActivity() {
    lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handler=Handler(Looper.getMainLooper())
    }

    override fun onStart() {
        super.onStart()
        handler.postDelayed(Runnable {
            nekoTest()
        },5000)
    }

    inner class Delegate1 : ItemViewDelegate<String>(1,R.layout.item_1) {
        override fun convert(holder: BaseViewHolder, data: String, position: Int) {
            holder.getView<TextView>(R.id.tv1)?.text = data.toString()
        }
    }

    inner class Delegate2 : ItemViewDelegate<String>(2,R.layout.item_3) {
        //如果指定viewTypeParser，则这里不起作用
        override fun isForViewType(data: String, position: Int): Boolean {
            val tmp = position % 2
            // 如果position对2取余不为零且 数据字符串是"item"
            return tmp != 0 && data == "item"
        }

        override fun convert(holder: BaseViewHolder, data: String, position: Int) {
            holder.getView<TextView>(R.id.tv2)?.text = data.toString()
        }

    }

    fun nekoTest() {
        //预定义数据，实际项目中可以传入空list初始化
        val d: MutableList<String> = mutableListOf()
        d.addAll(listOf("a", "b", "c","item"))
        val rv = findViewById<View>(R.id.rv) as RecyclerView
        val item1 = Delegate1()
        val item2 = Delegate2()
        val neko = neko<String>(rv) {
            //根据数据类型返回不同的viewtype
            viewTypeParser = ViewTypeParser<String> { data, pos ->
                if (data == "item") 1 else 2
            }
            mDatas = d.toMutableList()//指定adapter的数据

            //1. 多种viewtype可以使用[addItemViews]将多种viewholder添加进去
            addItemViews(item1, item2)

            //2. 单一viewtype，使用[addItemView]方法添加viewholder
            //这种单一viewholder, 用不上viewTypeParser
            //注意： 不要与[addItemViews]混用
//            addItemView(R.layout.item_3) { holder, t, position ->  //数据绑定
//                holder.getView<TextView>(R.id.tv2)?.text = t.toString()
//            }

            //给整个itemview设置点击事件
            itemClickListener= ItemClickListener { view, holder, position ->
                Toast.makeText(applicationContext,mDatas[position],Toast.LENGTH_LONG).show()
            }
            itemLongClickListener= ItemLongClickListener { view, holder, position ->
                    Toast.makeText(applicationContext,mDatas[position],Toast.LENGTH_LONG).show()
                    true
                }

        }.show()

        neko.mDatas[1] = "eee"
        //刷新数据
        (neko.iNekoAdapter as NekoAdapter).notifyItemChanged(1)
        //刷新数据
//        neko.refreshData(d)
    }

    fun listNekoTest() {
        val rv = findViewById<View>(R.id.rv) as RecyclerView
        val d: MutableList<String> = mutableListOf<String>()
        val item1 = Delegate1()
        val item2 = Delegate2()
        val neko = listNeko<String>(
            recyclerView = rv,
            diffCallback = object : DiffUtil.ItemCallback<String>() {            //指定diffCallback
                override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                    TODO("Not yet implemented")
                }

                override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                    TODO("Not yet implemented")
                }
            }) {
            //根据数据类型返回不同的viewtype
            viewTypeParser = ViewTypeParser<String> { data, pos ->
                    if (data == "item")
                        1
                    else
                        2
                }
            mDatas = d.toMutableList()//指定adapter的数据
            addItemViews(item1, item2)
        }.show()
        neko.mDatas[1] = "eee"
        //刷新数据
        neko.submitList(d)
        //刷新数据
        neko.nekoListAdapter.submitList(null)
    }


}