@file:Suppress(
    "unused",
    "SpellCheckingInspection",
    "UNUSED_ANONYMOUS_PARAMETER",
    "UNUSED_VARIABLE",
)

package com.kiylx.simplerecyclerview

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.LoadState
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kiylx.recyclerviewneko.*
import com.kiylx.recyclerviewneko.myadapter.Lm.linear
import com.kiylx.recyclerviewneko.myadapter.config.ListAdapterConfig
import com.kiylx.recyclerviewneko.utils.drag
import com.kiylx.recyclerviewneko.viewholder.ItemViewDelegate
import com.kiylx.recyclerviewneko.wrapper.anim.SlideInLeftAnimation
import com.kiylx.recyclerviewneko.wrapper.pagestate.config.IWrapper
import com.kiylx.simplerecyclerview.databinding.Item1Binding
import com.kiylx.simplerecyclerview.databinding.Item2Binding


class MainActivity2 : AppCompatActivity() {
    lateinit var handler: Handler
    lateinit var rv: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handler = Handler(Looper.getMainLooper())
        rv = findViewById<View>(R.id.rv) as RecyclerView
    }

    override fun onStart() {
        super.onStart()
//        handler.postDelayed(Runnable {
        //这里延迟是为了测试时避免出问题时闪退太快收集不到日志
//            nekoTest()
//            concatTest()
//            wrapperTest()
//            loadStateTest()
//            rvSingleViewHolderTest()
//            listNekoTest()
        rvSingleCollapseViewHolderTest()
//        }, 1500)

    }

    //    /** 代替viewholder */
    val delegate1 = ItemViewDelegate<String>(R.layout.item_1).onBind { holder, data, pos ->
        holder.getView<TextView>(R.id.tv1)?.text = data
    }
        .onClick(R.id.tv1) { v, h ->
            Toast.makeText(applicationContext, "pos:${h.layoutPosition}", Toast.LENGTH_SHORT).show()
        }
//
//    inner class Delegate2 : ItemViewDelegate<String>(R.layout.item_2) {
//        //在有多种viewholder时，
//        //此方法的作用是判断此viewholder是否应该显示某数据类型的数据
//        //如果此viewholder应该显示此数据类型数据，就让他返回true
//        //如果指定viewTypeParser，则这里不起作用,因此指定了viewTypeParser可以不重写此方法
//        override fun isForViewType(data: String, position: Int): Boolean {
//            //此类型的viewholder显示字符串是"item"的
//            return data == "item"
//        }
//
//        override fun convert(holder: BaseViewHolder, data: String, position: Int) {
//            holder.getView<TextView>(R.id.tv2)?.text = data
//        }
//
//    }
//
    /** 仅有一种viewHolder的示例 */
    fun rvSingleViewHolderTest() {
        //预定义数据
        val d: MutableList<String> = mutableListOf()
        for (i in 0..100) {
            d.add(RandomUtil.generateByRandom(9))
        }

        //泛型指定了此recyclerview显示什么类型的数据
        val config = createNormalAdapterConfig<String> {
            layoutManager = LinearLayoutManager(this@MainActivity2)
            // .....
            //仅有一种viewHolder,
            //不需要指定viewTypeParser
            //不需要重写ItemViewDelegate中的isForViewType方法来判断ViewType
            //仅添加"viewholder"
            addItemView(R.layout.item_1) {
                onBind { holder, data, position ->
                    holder.getView<TextView>(R.id.tv1)?.text = data.toString()
                }
                onClick(R.id.tv1) { v1, h ->
                    val v = h.getView<LinearLayout>(R.id.collapse_layout)!!
                    if (v.visibility == View.VISIBLE) {
                        v.visibility = View.GONE
                        h.setIsRecyclable(true)
                    } else {
                        v.visibility = View.VISIBLE
                        h.setIsRecyclable(false)
                    }
                }
            }
            // .....
        }.done(rv, d)//调用show方法完成recycleview的显示

        config.drag(rv)
    }

    class CollapseData(var collapse: Boolean = true, var s: String)

    /** 仅有一种viewHolder的示例 可折叠 */
    fun rvSingleCollapseViewHolderTest() {
        //预定义数据
        val d: MutableList<CollapseData> = mutableListOf()
        for (i in 0..100) {
            d.add(CollapseData(s = RandomUtil.generateByRandom(9)))
        }

        //泛型指定了此recyclerview显示什么类型的数据
        val config = createNormalAdapterConfig<CollapseData> {
            // .....
            //仅有一种viewHolder,
            //不需要指定viewTypeParser
            //不需要重写ItemViewDelegate中的isForViewType方法来判断ViewType
            //仅添加"viewholder"
            addItemView(R.layout.item_1) {
                onBind { holder, data, position ->
                    holder.getView<TextView>(R.id.tv1)?.text = data.s
                    val v = holder.getView<LinearLayout>(R.id.collapse_layout)!!
                    if (data.collapse) {
                        v.visibility = View.GONE
                    } else {
                        v.visibility = View.VISIBLE
                    }
                }
                onClick(R.id.tv1) { v1, h ->
                    //点击时显示和隐藏折叠菜单
                    val v = h.getView<LinearLayout>(R.id.collapse_layout)!!
                    if (v.visibility == View.VISIBLE) {
                        v.visibility = View.GONE
                        h.getBindData<CollapseData>().collapse = true
                    } else {
                        v.visibility = View.VISIBLE
                        h.getBindData<CollapseData>().collapse = false
                    }
                }
            }
            // .....
        }
            .done(rv, d)//调用show方法完成recycleview的显示
        config.drag(rv)
    }
//
//    /** 普通的adapter，多种viewtype */
//    fun nekoTest() {
//        //预定义数据
//        val d: MutableList<String> = mutableListOf()
//        d.addAll(listOf("a", "b", "c", "item"))
//
//        //两个viewholder类型
//        val item1 = Delegate1()
//        val item2 = Delegate2()
//
//        //泛型指定了此recyclerview显示什么类型的数据
//        val neko = createNormalAdapterConfig<String> {
//            //内置默认LinearLayoutManager,可以在这里修改
//            //layoutManager=GridLayoutManager(this@MainActivity2,2) //替换默认的布局管理器
//            layoutManager.apply {
//                //修改布局管理器的配置
//            }
//
//            mDatas = d.toMutableList()//指定adapter的数据。也可以现在不指定数据，在后面的show方法中传入数据
//
////使用ViewTypeParser
//            //在有多种viewholder时，根据数据类型返回不同的viewtype
//            //当不指定这个解析器时，就得重写ItemViewDelegate中的isForViewType方法来判断viewtype
//            viewTypeParser = ViewTypeParser<String> { data, pos ->
//                if (data == "item") 1 else 2
//            }
//
//            //多种viewtype可以使用[addItemViews]将多种viewholder添加进去
//            addItemViews(
//                1 pack item1,
//                2 pack item2
//            )
//
//            //向上面批量添加或者像这样一个个添加
////            addItemView(R.layout.item_1, 1) { holder, data, position ->
////                //数据绑定到viewholder
////                holder.with<Item1Binding> {
////                    //使用viewbinding
////                }
////
////            }
//
////不使用ViewTypeParser
////            addItemViews(
////                item1, item2
////            )
//
//            //向上面批量添加或者像这样一个个添加
////            addItemView(R.layout.item_1, isThisView = { data: String, pos: Int ->
////                //像上面那样有viewTypeParser的话，isThisView参数可以不写
////                return@addItemView true
////            }) { holder, data, position ->
////                //数据绑定到viewholder
////                holder.with<Item1Binding> {
////                    //使用viewbinding
////                }
////
////            }
//
//            //给整个itemview设置点击事件
//            setItemClick { view, holder ->
//                Toast.makeText(applicationContext, holder.getBindData<String>(), Toast.LENGTH_LONG)
//                    .show()
//            }
//            //设置长按事件
//            setItemLongClick { view, holder ->
//                Toast.makeText(applicationContext, holder.getBindData<String>(), Toast.LENGTH_LONG)
//                    .show()
//                true
//            }
//
//        }.done(rv)//调用show方法完成recycleview的显示
//
//        neko.mDatas[1] = "eee"
//        //刷新数据
//        neko.normalAdapter.notifyItemChanged(1)
//        //刷新数据
//        neko.refreshData(d)
//        //刷新数据
//        neko.normalAdapter.notifyItemChanged(3)
//    }
//
    /** listadapter，多种viewtype */
    fun listNekoTest() {
        val d: MutableList<String> = mutableListOf<String>()
        d.add("0000")
        d.add("1111")
        d.add("2222")
        d.add("3333")
        val item1 = delegate1
        val myDiffCallback = object : DiffUtil.ItemCallback<String>() {
            //指定diffCallback
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }

        val listAdapterConfig: ListAdapterConfig<String> = createListAdapterConfig<String>(
            // 若没有指定[asyncConfig]，则用[diffCallback]参数创建NekoListAdapter
            // 若指定了[asyncConfig]，则[diffCallback]参数不起作用
            //diffCallback和asyncConfig是listAdapter的构造函数参数，不明白去看下ListAdapter
            diffCallback = myDiffCallback,
            asyncConfig = AsyncDifferConfig.Builder<String>(myDiffCallback).build()
        ) {
            //这部分代码跟上面一样
            addItemView(delegate1)
        }.done(rv)
        listAdapterConfig.drag(rv, true)

        //添加StateHeader
//        neko.withLoadStatus(rv) {
//            withHeader {
//                setItemDelegate(R.layout.item_2) { holder, ls ->
//
//                }
//            }
//
//        }.done()

//        d[0] = "eee"
        //刷新数据
        listAdapterConfig.submitList(d)
        //刷新数据
//        neko.myListAdapter.submitList(null)
    }
//
//    /** pagingDataAdapter */
//    fun pagingTest() {
//        val pagingConfig = createPaging3AdapterConfig(object : DiffUtil.ItemCallback<String>() {
//            //这里指定了数据是否相同的判断。不明白的再去学一遍pagingDataAdapter。
//            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
//                TODO("Not yet implemented")
//            }
//
//            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
//                TODO("Not yet implemented")
//            }
//        }) {
//            //这里跟其他的几种情况一模一样，不再重复写
//
//            //给pagingAdapter添加header和footer
//
//            withHeader {
//                addItemDelegate(LoadState.Loading, R.layout.item_1) { holder, ls ->
//
//                }
//            }
//            withFooter {
//                addItemDelegate(LoadState.Loading, R.layout.item_2) { holder, ls ->
//
//                }
//            }
//        }.done(rv)
////        pagingConfig.myPaging3Adapter.submitData()//提交数据
//        //刷新数据
//        pagingConfig.myPaging3Adapter.notifyItemChanged(3)
//    }
//
//    /** concatAdapter */
//    fun concatTest() {
//
//        //预定义数据
//        val d1: MutableList<String> = mutableListOf()
//        d1.addAll(listOf("a", "b", "c", "item"))
//
//        //预定义数据
//        val d2: MutableList<String> = mutableListOf()
//        d2.addAll(listOf("a", "b", "c", "item"))
//
//        val neko1 = createNormalAdapterConfig<String> {
//            mDatas = d1.toMutableList()//指定adapter的数据
//            //仅有一种viewHolder
//            setSingleItemView(R.layout.item_1) { holder, data, position ->
//                holder.getView<TextView>(R.id.tv1)?.text = data.toString()
//            }
//        }
//        val neko2 = createNormalAdapterConfig<String> {
//            mDatas = d2.toMutableList()//指定adapter的数据
//            //仅有一种viewHolder
//            setSingleItemView(R.layout.item_2) { holder, data, position ->
//                holder.getView<TextView>(R.id.tv2)?.text = data.toString()
//            }
//        }
//
//        //将两个adapter合并，此方法可以传入更多的adapter进行合并
//        val concat = concatMultiAdapter(neko1, neko2) {
//            //自定义配置
//            this.concatAdapter.apply {
//
//            }
//        }.done(rv, linear())
//
//        neko1.mDatas[2] = "ertt"
//        //刷新数据
//        neko1.normalAdapter.notifyItemChanged(2)
//    }
//
//    /** concatAdapter */
//    fun wrapperTest() {
//        //预定义数据
//        val d1: MutableList<String> = mutableListOf()
//        d1.addAll(listOf("a", "b", "c", "item"))
//
//        //预定义数据
//        val d2: MutableList<String> = mutableListOf()
//        d2.addAll(listOf("a", "b", "c", "item"))
//
//        val neko1 = createNormalAdapterConfig<String> {
//            mDatas = d1.toMutableList()//指定adapter的数据
//            //仅有一种viewHolder
//            setSingleItemView(R.layout.item_1) { holder, data, position ->
//                holder.getView<TextView>(R.id.tv1)?.text = data.toString()
//            }
//        }
//        val neko2 = createNormalAdapterConfig<String> {
//            mDatas = d2.toMutableList()//指定adapter的数据
//            //仅有一种viewHolder
//            setSingleItemView(R.layout.item_2) { holder, data, position ->
//                holder.getView<TextView>(R.id.tv2)?.text = data.toString()
//            }
//        }
//
//        //将两个adapter合并，此方法可以传入更多的adapter进行合并
//        val concat = concatMultiAdapter(neko1, neko2) {
//            // todo 自定义配置
//        }
//
//        val w = concat.withPageState(rv) {
//            //例如设置空布局
//            setEmpty(R.layout.empty) {
//                it.setOnClickListener {
//                    Log.d(tag, "被点击")
//                }
//            }
//        }.showStatePage()
//
//        handler.postDelayed(Runnable {
//            w.showEmpty()
//        }, 1000)
//
//        handler.postDelayed(Runnable {
//            w.showContent()
//        }, 2000)
//
//        neko1.mDatas[1] = "olskdfbsf"
//        neko1.normalAdapter.notifyItemChanged(1)
//
//    }

    /** header和footer */
    fun loadStateTest() {
        //预定义数据
        val d1: MutableList<String> = mutableListOf()
        d1.addAll(listOf("a", "b", "c", "item", "d", "e", "r", "ee", "a", "b", "c", "dd"))
        //预定义数据
        val d2: MutableList<String> = mutableListOf()
        d2.addAll(listOf("a", "b", "c", "item"))
        val neko1 = createNormalAdapterConfig<String> {
            mDatas = d1.toMutableList()//指定adapter的数据
            //仅有一种viewHolder
            addItemView(R.layout.item_1) {
                onCreate { holder ->
                    //干预创建过程
                    //例如itemview是一个recyclerview，希望在oncreateviewholder时，初始化recyclerview
                    //如果不需要，可以不调用此函数
                }
                onBind { holder, data, position ->
                    //使用with方法得到viewbinding实例
                    holder.withBinding<Item1Binding> {
                        tv1.text = data
                    }
                    //或者不使用viewbinding
                    //                holder.getView<TextView>(R.id.tv1)?.text = data.toString()
                    //不要在绑定中使用此方法，因为每绑定一次数据就会调用添加一次点击事件
                    //                holder.setOnClickListener(R.id.tv1) {
                    //                    //对某个view设置点击事件
                    //                }
                }
                onClick(R.id.tv1, R.id.tv0) { v, holder ->
                    Toast.makeText(application, holder.getBindData<String>(), Toast.LENGTH_SHORT)
                        .show()
                }
            }

            //设置动画，对于concat连接的adapter,可以分别设置不同的动画。
            configAnim {
                itemAnimation = SlideInLeftAnimation()
            }
        }
        val neko2 = createNormalAdapterConfig<String> {
            mDatas = d2.toMutableList()//指定adapter的数据
            //仅有一种viewHolder
            addItemView(layoutId = R.layout.item_2) {
                onBind { holder, data, position ->
                    holder.withBinding<Item2Binding> {
                        tv2.text = data.toString()
                    }
                }
            }

        }
        //将两个adapter合并，此方法可以传入更多的adapter进行合并
        val concat = concatMultiAdapter(neko1, neko2) {
            // 自定义配置
            //统一给所有adapter设置动画
            setAnim(SlideInLeftAnimation())
        }

        //给rv添加上header和footer，
        val loadStateWrapper = concat.withLoadStatus(rv) {
            //配置footer
            withFooter {
                autoLoading = true
                //footer配置成一个跟loadstate没关联的itemview
                setItemDelegate(com.kiylx.recyclerviewneko.R.layout.footer_item) { header, loadstate ->
                    //itemview绑定
                    val m = header.getView<Button>(com.kiylx.recyclerviewneko.R.id.retry_button)!!
                    Log.d(tag, "可见性：${m.visibility}")
                }
            }
            //当滑动到底部时，如果不配置footer，这里不会被调用
            whenScrollToEnd {
                handler.postDelayed(Runnable {
                    footerState(LoadState.NotLoading(false))
                }, 3000)
            }
            //当数据不够一屏，滑动时
            whenNotFull {

            }
            //当滑动到顶时,如果不配置header，这里不会被调用
            whenScrollToTop {

            }

        }
        //先设置header和footer，之后才可以用状态页包装
        val statePage: IWrapper = loadStateWrapper.withPageState {
            //例如设置空布局
            setEmpty(R.layout.empty) {
                it.setOnClickListener {
                    Log.d(tag, "被点击")
                }
            }

        }.showStatePage(linear())//显示状态页

        //显示被包装起来的content
        statePage.showContent()
        handler.postDelayed(Runnable {
            statePage.showEmpty()

            handler.postDelayed(Runnable {
                statePage.showContent()
            }, 1000)

        }, 2000)


        neko1.mDatas[1] = "olskdfbsf"
        neko1.normalAdapter.notifyItemChanged(1)

    }

    companion object {
        const val tag = "MainActivity2"
    }
}