

## 一个只具有单个viewholder种类的rv，各种用法的示例

```kotlin
/**
 * 仅有一种viewHolder的示例
 */
fun nekoSingleTest() {
    //预定义数据
    val d: MutableList<String> = mutableListOf()
    d.addAll(listOf("a", "b", "c", "item"))

    //泛型指定了此recyclerview显示什么类型的数据
    val neko = neko<String>(rv) {
        //内置默认LinearLayoutManager,可以在这里修改
        //layoutManager=GridLayoutManager(this@MainActivity2,2) //替换默认的布局管理器
        layoutManager.apply {
            //修改布局管理器的配置
        }
    mDatas = d.toMutableList()//指定adapter的数据。也可以现在不指定数据，在后面的show方法中传入数据
        //仅有一种viewHolder,
        //不需要指定viewTypeParser
        //不需要重写ItemViewDelegate中的isForViewType方法来判断ViewType
        setSingleItemView(R.layout.item_1) { holder, data, position -> //数据的绑定
            //直接使用getview绑定
            holder.getView<TextView>(R.id.tv1)?.text = data
            //或者使用viewbinding
            holder.with<Item1Binding> {
                tv1.text = data
            }
            //itemview的点击事件等
            holder.setOnClickListener(R.id.tv1){}
            //。。。其他的内置viewholder方法
        }
        //内部的配置：点击事件，长按事件等，这将作用在itemview上
        //给整个itemview设置点击事件
        itemClickListener = ItemClickListener { view, holder, position ->
            Toast.makeText(applicationContext, mDatas[position], Toast.LENGTH_LONG).show()
        }
        //设置整个itemview长按事件
        itemLongClickListener = ItemLongClickListener { view, holder, position ->
            Toast.makeText(applicationContext, mDatas[position], Toast.LENGTH_LONG).show()
            true
        }
        //动画配置
        configAnim {}
        //或者不配置，仅指定动画类型
        itemAnimation =SlideInLeftAnimation()
    }.show(d)//调用show方法完成recycleview的显示，内部最基本的逻辑就是setAdapter去显示数据
    //neko持有rv，adapter，layoutmanager等东西，所以可以使用adapter的方法更新数据
    neko.nekoAdapter.notifyDataSetChanged()
    neko.mDatas[1] = "eee"
    //刷新数据
    (neko.iNekoAdapter as NekoAdapter).notifyItemChanged(1)
    //刷新数据
    neko.refreshData(d)
    //刷新数据
    neko.nekoAdapter.notifyItemChanged(3)
}
```

## 多种viewholder的例子（将基于最开始的例子讲解，省略一些配置过程）

### 情况一：使用viewTypeParser区分viewtype

```kotlin
    /**
     * 代替viewholder的ItemViewDelegate。
     * 使用：在neko方法的块中调用addItemViews方法将类型和ItemViewDelegate传入
     * 
     * 也可以不实现此ItemViewDelegate，
     * 需要在neko方法的块中，使用addItemView方法配置传入ItemViewDelegate内容
     */
    inner class Delegate1 : ItemViewDelegate<String>(R.layout.item_1) {
        // 绑定数据到viewholder
        override fun convert(holder: BaseViewHolder, data: String, position: Int) {
            holder.with<Item1Binding> {
                tv1.text = data
            }
        }
    }

    inner class Delegate2 : ItemViewDelegate<String>(R.layout.item_2) {
        override fun convert(holder: BaseViewHolder, data: String, position: Int) {
            holder.getView<TextView>(R.id.tv2)?.text = data
        }
    }
    
    /**
     * 普通的adapter，多种viewtype，
     */
    fun nekoTest() {
        //预定义数据
        val d: MutableList<String> = mutableListOf()
        d.addAll(listOf("a", "b", "c", "item1", "item"))

        //两个viewholder类型
        val item1 = Delegate1()
        val item2 = Delegate2()

        //泛型指定了此recyclerview显示什么类型的数据
        val neko = neko<String>(rv) {
            //在有多种viewholder时，根据数据类型返回不同的viewtype
            //当不指定这个解析器时，就得重写ItemViewDelegate中的isForViewType方法来判断viewtype
            viewTypeParser = ViewTypeParser<String> { data, pos ->
                if (data == "item1") 1 else 2
            }
            //多种viewtype可以使用[addItemViews]将多种viewholder添加进去
            addItemViews(
                1 pack item1,
                2 pack item2
            )

            //除了addItemViews方法批量添加还可以像这样一个个添加，这样就不需要手动实现ItemViewDelegate
            addItemView(R.layout.item_1, 1) { holder, data, position ->
                //数据绑定到viewholder
                holder.with<Item1Binding> {
                    //使用viewbinding
                }
            }
        }.show()//调用show方法完成recycleview的显示

    }
```

### 情况二：使用ItemViewDelegate中的isForViewType方法来判断viewtype
```kotlin
    inner class Delegate1 : ItemViewDelegate<String>(R.layout.item_1) {
        // 绑定数据到viewholder
        override fun convert(holder: BaseViewHolder, data: String, position: Int) {
            holder.with<Item1Binding> {
                tv1.text = data
            }
        }
        	//在有多种viewholder时，
            //此方法的作用是判断此viewholder是否应该显示某数据类型的数据
            //如果此viewholder应该显示此数据类型数据，就让他返回true
            //如果指定viewTypeParser，则这里不起作用,因此指定了viewTypeParser可以不重写此方法
            override fun isForViewType(data: String, position: Int): Boolean {
                //此类型的viewholder显示字符串是"item1"的，所以在data是"item1"返回true
                return data == "item1"
            }
    }
    
    inner class Delegate2 : ItemViewDelegate<String>(R.layout.item_2) {
        override fun convert(holder: BaseViewHolder, data: String, position: Int) {
            holder.getView<TextView>(R.id.tv2)?.text = data
        }
            override fun isForViewType(data: String, position: Int): Boolean {
                //此类型的viewholder不显示item1的字符串
                return data != "item1"
            }
    }
    
    /**
     * 普通的adapter，多种viewtype，
     */
    fun nekoTest() {
        //预定义数据
        val d: MutableList<String> = mutableListOf()
        d.addAll(listOf("a", "b", "c", "item1", "item"))
    
        //两个viewholder类型
        val item1 = Delegate1()
        val item2 = Delegate2()
    
        //泛型指定了此recyclerview显示什么类型的数据
        val neko = neko<String>(rv) {
          
            //多种viewtype可以使用[addItemViews]将多种viewholder添加进去
            addItemViews(item1,item2)
    
        //向上面批量添加或者像这样一个个添加
        addItemView(R.layout.item_1,isThisView = { data: String, pos: Int ->
            //像上面那样有viewTypeParser的话，isThisView参数可以不写
            return@addItemView data == "item1"
        }) { holder, data, position ->
            //数据绑定到viewholder
            holder.with<Item1Binding> {
                //使用viewbinding
            }
    
        }
        
        }.show()//调用show方法完成recycleview的显示
    
    }
```

## 利用concatAdapter连接多种adapter

```kotlin
/**
 * concatAdapter
 */
fun concatTest() {

    //预定义数据
    val d1: MutableList<String> = mutableListOf()
    d1.addAll(listOf("a", "b", "c", "item"))

    //预定义数据
    val d2: MutableList<String> = mutableListOf()
    d2.addAll(listOf("a", "b", "c", "item"))

    val neko1 = neko<String>(rv) {
        mDatas = d1.toMutableList()//指定adapter的数据
        //仅有一种viewHolder
        setSingleItemView(R.layout.item_1) { holder, data, position ->
            holder.getView<TextView>(R.id.tv1)?.text = data.toString()
        }
    }
    val neko2 = neko<String>(rv) {
        mDatas = d2.toMutableList()//指定adapter的数据
        //仅有一种viewHolder
        setSingleItemView(R.layout.item_2) { holder, data, position ->
            holder.getView<TextView>(R.id.tv2)?.text = data.toString()
        }
    }

    //示例将两个adapter合并，此方法可以传入更多的adapter进行合并
    val concat = concatNeko(neko1, neko2) {
        //自定义配置
        this.concatAdapter.apply { 
            
        }
    }.done()

    neko1.mDatas[2] = "ertt"
    //刷新数据
    neko1.nekoAdapter.notifyItemChanged(2)
}
```



##  状态页
状态页可以用于任意的adapter，例如上面的最普通的adapter，listAdapter，paging3Adapter，以及concatAdapter

```kotlin
1. 状态页的可以全局配置：
 GlobalWrapperConfig.configStateView {
        this[PageStateTypes.Empty] = PageStateWrapperView(android.R.id.edit) {

        }
        this[PageStateTypes.Error] = PageStateWrapperView(android.R.id.edit){
            
        }
    }
    
2. 也可以在adapter中单独配置
  /**
     * concatAdapter
     */
    fun wrapperTest() {
        //预定义数据
        val d1: MutableList<String> = mutableListOf()
        d1.addAll(listOf("a", "b", "c", "item"))

        //预定义数据
        val d2: MutableList<String> = mutableListOf()
        d2.addAll(listOf("a", "b", "c", "item"))

        val neko1 = neko<String>(rv) {
            mDatas = d1.toMutableList()//指定adapter的数据
            //仅有一种viewHolder
            setSingleItemView(R.layout.item_1) { holder, data, position ->
                holder.getView<TextView>(R.id.tv1)?.text = data.toString()
            }
        }
        val neko2 = neko<String>(rv) {
            mDatas = d2.toMutableList()//指定adapter的数据
            //仅有一种viewHolder
            setSingleItemView(R.layout.item_2) { holder, data, position ->
                holder.getView<TextView>(R.id.tv2)?.text = data.toString()
            }
        }

        //将两个adapter合并，此方法可以传入更多的adapter进行合并
        val concat = concatNeko(neko1, neko2) {
            // todo 自定义配置
        }
        //---------------------上面还是一些adapter的配置-----------------------
        
        //单独配置空布局状态页
        val w = concat.withPageState {
            //例如设置空布局
            setEmpty(R.layout.empty) {
                //对布局的控制
                it.setOnClickListener {
                    Log.d(tag, "被点击")
                }
            }
        }.showStatePage()

        //调用方法以展示不同状态页
        handler.postDelayed(Runnable {
            w.showEmpty()
        }, 1000)

        handler.postDelayed(Runnable {
            w.showContent()
        }, 2000)

    }
```



## header和footer

对于Paging3Adapter来说，官方有header和footer的配置，内部是用的concatAdapter。
因此，除了可以包装一下状态适配器，方便在使用paging3Neko方法时配置状态页，
还可以通过concatAdapter将头尾布局和加载状态连接到其余的adapter。

```kotlin

    /**
     * header和footer
     */
    fun loadStateTest() {
        //预定义数据
        val d1: MutableList<String> = mutableListOf()
        d1.addAll(listOf("a", "b", "c", "item", "d", "e", "r", "ee", "a", "b", "c", "dd"))
        //预定义数据
        val d2: MutableList<String> = mutableListOf()
        d2.addAll(listOf("a", "b", "c", "item"))
        val neko1 = neko<String>(rv) {
            mDatas = d1.toMutableList()//指定adapter的数据
            //仅有一种viewHolder
            setSingleItemView(R.layout.item_1) { holder, data, position ->
                //使用with方法得到viewbinding实例
                holder.with<Item1Binding> {
                    tv1.text = data
                }
            }
        }
        val neko2 = neko<String>(rv) {
            mDatas = d2.toMutableList()//指定adapter的数据
            //仅有一种viewHolder
            setSingleItemView(R.layout.item_2) { holder, data, position ->
                holder.with<Item2Binding> {
                    tv2.text = data.toString()
                }
            }
        }
        //将两个adapter合并，此方法可以传入更多的adapter进行合并
        val concat = concatNeko(neko1, neko2) {
            // 自定义配置
            //统一给所有adapter设置动画
            setAnim(SlideInLeftAnimation())
        }
//======================以上面的连接后的持有adapter的config为例，给rv加上header和footer==============
        //给rv添加上header和footer，
        val loadStateWrapper = concat.withLoadStatus {
            //配置footer
            withFooter {
                autoLoading = true //在滑动到底部或顶部时，自动改变状态以展示头尾布局
                //footer配置成一个跟loadstate没关联的itemview
                setItemDelegate(com.kiylx.libx.R.layout.footer_item) { header, loadstate ->
                    //itemview绑定
                    val m = header.getView<Button>(com.kiylx.libx.R.id.retry_button)!!
                    Log.d(tag, "可见性：${m.visibility}")
                }
            }
            //当滑动到底部时，如果不配置footer，这里不会被调用
            whenScrollToEnd {
            
            //1. 如果autoLoading是false //在滑动到底部或顶部时，需要自己手动改变状态以展示头尾布局
            handler.postDelayed(Runnable {
                    footerState(LoadState.Loading)
                }, 3000)
            //2. 当处理结束后，例如请求完网络数据，则可以手动改变状态，以关闭头尾布局的展示
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
        //这里需要注意的是，由于设置状态页包装是获取内容的adapter
        //而这里的loadStateWrapper是将头尾布局通过concatAdapter连接到内容adapter，
        //因此，这里对loadStateWrapper的状态页包装，拿到的内容adapter其实是添加头尾布局的concatAdapter
        //所以需要先设置好header和footer，之后才可以用状态页包装，否则会出现意想不到的问题
        val statePage: IWrapper = loadStateWrapper.withPageState {
            //例如设置空布局
            setEmpty(R.layout.empty) {
                it.setOnClickListener {
                    Log.d(tag, "被点击")
                }
            }

        }.showStatePage()//显示状态页

        //显示被包装起来的content
        statePage.showContent()

        handler.postDelayed(Runnable {
            statePage.showEmpty()

            handler.postDelayed(Runnable {
                statePage.showContent()
            }, 1000)

        }, 2000)


        neko1.mDatas[1] = "olskdfbsf"
        neko1.nekoAdapter.notifyItemChanged(1)

    }
```


## 拖拽和侧滑

侧滑和拖拽对recyclerview生效

```kotlin
rv.attachDragListener {
    //监听移动
    this.dragSwapListener =
        DragPosListener { source: Int, target: Int, sourceAbsolutePosition: Int, targetAbsolutePosition: Int ->
            Log.d(tag, "source1:$source,target1:$target")
        }
    //移动结束
    this.clearViewListener =
        DragPosListener { source: Int, target: Int, sourceAbsolutePosition: Int, targetAbsolutePosition: Int ->
            Log.d(tag, "source2:$source,target2:$target")
        }
    //监听侧滑
    useSlideSwiped = true
    this.slideSwipedListener =
        SlideSwipedPosListener { target: Int, absoluteAdapterPosition: Int ->
            Log.d(tag, "target3:$target")
        }
}
```


## paging3adapter

```kotlin
/**
 * pagingDataAdapter
 */
fun pagingTest() {
    val pagingConfig = paging3Neko(rv, object : DiffUtil.ItemCallback<String>() {
        //这里指定了数据是否相同的判断。
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            TODO("Not yet implemented")
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            TODO("Not yet implemented")
        }
    }) {
        //这里跟其他的几种情况一模一样，不再重复写

        //给paging3Adapter添加header和footer

        withHeader {
            addItemDelegate(LoadState.Loading, R.layout.item_1) { holder, ls ->

            }
        }
        withFooter {
            addItemDelegate(LoadState.Loading, R.layout.item_2) { holder, ls ->

            }
        }
    }.done()
    //刷新数据
    pagingConfig.nekoPagingAdapter.notifyItemChanged(3)
}
```





## listadapter

```kotlin
/**
 * listadapter，多种viewtype
 */
fun listNekoTest() {
    val d: MutableList<String> = mutableListOf<String>()
    val item1 = Delegate1()
    val item2 = Delegate2()
    val myDiffCallback = object : DiffUtil.ItemCallback<String>() {
        //指定diffCallback
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    val neko = listNeko<String>(
        recyclerView = rv,
        // 若没有指定[asyncConfig]，则用[diffCallback]参数创建NekoListAdapter
        // 若指定了[asyncConfig]，则[diffCallback]参数不起作用
        //diffCallback和asyncConfig是listAdapter的构造函数参数，不明白去看下ListAdapter
        diffCallback = myDiffCallback,
        asyncConfig = AsyncDifferConfig.Builder<String>(myDiffCallback).build()
    ) {
        //这部分代码跟上面一样
    }

    //添加StateHeader
    neko.withLoadStatus {
        withHeader {
            setItemDelegate(R.layout.item_2) { holder, ls ->

            }
        }

    }.done()

    neko.mDatas[1] = "eee"
    //刷新数据
    neko.submitList(d)
    //刷新数据
    neko.nekoListAdapter.submitList(null)
}
```