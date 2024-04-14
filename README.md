# RecyclerviewNeko

* 支持多种adapter,也可以自己定义新的adapter传入
* 没有侵入和魔改
* 支持状态页回调
* 支持下拉显示header和上拉显示footer,下拉加载，上拉加载等
* 内置动画
* 支持viewbinding

# 使用

版本 [![](https://jitpack.io/v/Knightwood/RecyclerViewNeko.svg)](https://jitpack.io/#Knightwood/RecyclerViewNeko)

```css
dependencies {
       implementation 'com.github.Knightwood:RecyclerViewNeko:Tag'
}
```

## 快速开始

```kotlin
//预定义数据
val d: MutableList<String> = mutableListOf()
d.addAll(listOf("a", "b", "c", "item"))

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
neko1.done(rv)
neko1.normalAdapter.notifyItemChanged(3)//直接获取相应类型的adapter刷新数据

```

除了使用`createNormalAdapterConfig`创建普通的adapter 还可以使用：
* createListAdapterConfig 创建ListAdapter
* createPaging3AdapterConfig 创建Paging3Adapter

例如paging

```kotlin
rv = view.findViewById<RecyclerView>(R.id.rv)
config = createPaging3AdapterConfig<MediaResourceEntity>(object :
            DiffUtil.ItemCallback<MediaResourceEntity>() {
            override fun areItemsTheSame(
                oldItem: MediaResourceEntity,
                newItem: MediaResourceEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: MediaResourceEntity,
                newItem: MediaResourceEntity
            ): Boolean {
                return oldItem == newItem
            }

        }) {
            //与上面实例一样，在这里添加了viewholder
            addItemView(R.layout.item_1,
                isThisView = { data, position ->
                    data.fileType ==2
                }) {
                // 。。。
            }
            //再添加一种新的viewholder
            addItemView(R.layout.item_2,
                isThisView = { data, position ->
                    data.fileType != 2
                }) {
                // 。。。
            }
        }

        config.done(rv)
        //提交数据
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.datas.collect { pagingData ->
                    config.myPaging3Adapter.submitData(pagingData)
                }
            }
        }

```


# 下面都不用看了，需要重写文档

### 对于pagingadapter和listadapter
他们内部维护有数据列表，不用传递mDatas给show方法
例如：
```
rv = view.findViewById<RecyclerView>(R.id.rv)
        requireActivity().run {
            neko = paging3Neko<MediaResourceEntity>(rv,
                object : DiffUtil.ItemCallback<MediaResourceEntity>() {
                    override fun areItemsTheSame(
                        oldItem: MediaResourceEntity,
                        newItem: MediaResourceEntity
                    ): Boolean {
                        return oldItem.id == newItem.id
                    }

                    override fun areContentsTheSame(
                        oldItem: MediaResourceEntity,
                        newItem: MediaResourceEntity
                    ): Boolean {
                        return oldItem == newItem
                    }

                }) {
                addItemView(layoutId = R.layout.sample_item) { holder, data, position ->
                    holder.with<SampleItemBinding> {
                        nameText.setText(data.title)
                        descriptionText.setText(data.content)
                    }
                }

                itemClickListener =
                    ItemClickListener { view, holder, bindingAdapterPosition, position, data ->
                        //todo 点击跳转不同的activity预览文件
                        Log.d(TAG, "onViewCreated: ${data.content}")
                    }
            }
        }
        neko.done()//done方法是NekoPagingAdapterConfig中的，不需要使用show方法
        neko.nekoPagingAdapter.addLoadStateListener {

        }
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
//                viewModel.getPagingData(queryParam.toString())
                viewModel.datas.collect { pagingData ->
                    neko.nekoPagingAdapter.submitData(pagingData)//提交数据
                }
            }
        }

```

## 多种viewtype

```kotlin
//预定义数据
val d: MutableList<String> = mutableListOf()
d.addAll(listOf("a", "b", "c", "item1"))

//泛型指定了此recyclerview显示什么类型的数据
val neko = neko<String>(rv) {
    //内置默认LinearLayoutManager,可以在这里修改
    layoutManager=GridLayoutManager(this@MainActivity,2) //替换默认的布局管理器

    //添加单种类的"viewholder"，以及数据绑定到viewholder
    addSingleItemView(R.layout.item_1) { holder, data, position ->
        //数据绑定到viewholder
        holder.getView<TextView>(R.id.tv1)?.text = data.toString()
        //或者使用viewbinding
        holder.with<Item1Binding> {
            //使用viewbinding绑定数据
        }
    }
    
    //对于多种viewtype
 	//一种方式是根据数据类型返回不同的viewtype
    //当不指定这个解析器时，就得重写ItemViewDelegate中的isForViewType方法来判断viewtype
    viewTypeParser = ViewTypeParser<String> { data, pos ->
         if (data == "item1") 1 else 2
    }    
    //并使用addItemView添加ItemViewDelegate
    addItemView(R.layout.item_1, 1) { holder, data, position ->
                //数据绑定到viewholder
                holder.with<Item1Binding> {
                    //使用viewbinding
                }
            }
    
   //或者不写viewtype和 viewTypeParser
    addItemView(R.layout.item_1,isThisView = { data: String, pos: Int ->
        //像上面那样，判断此数据是否有该ItemViewDelegate显示
        return@addItemView data == "item1"
    }) { holder, data, position ->
        //数据绑定到viewholder
        holder.with<Item1Binding> {
            //使用viewbinding
        }
   }   
    
    
    //设置动画，对于concat连接的adapter,可以分别设置不同的动画。
    configAnim {
        itemAnimation = SlideInLeftAnimation()
    }
    // .....
    //给整个itemview设置点击事件
    itemClickListener = ItemClickListener { view, holder, position ->
        Toast.makeText(applicationContext, mDatas[position], Toast.LENGTH_LONG).show()
    }
    //设置长按事件
    itemLongClickListener = ItemLongClickListener { view, holder, position ->
        Toast.makeText(applicationContext, mDatas[position], Toast.LENGTH_LONG).show()
        true//事件被消费
    }

}.show(d)//调用show方法完成recycleview的显示，也就是setAdapter()

//直接获取相应类型的adapter刷新数据
neko.nekoAdapter.notifyItemChanged(3)
```

# 文档

## 关于viewholder

使用此库不用再继承viewholder，viewholder的创建和绑定部分被拿出来了，交给了一个叫做ItemViewDelegate的类去描述viewholder。
在adapter创建viewholder时，会向ItemViewDelegateManager查询ItemViewDelegate信息，然后创建viewholder和绑定数据。

例如：这是一个ItemViewDelegate的实现
对于区分不同的viewType，一个是实现ItemViewDelegate类中的isForViewType方法，另一种是在配置rv方法的dsl块中指定viewTypeParser

```kotlin
class Delegate2 : ItemViewDelegate<String>(R.layout.item_2) {
    //在有多种viewholder时，
    //此方法的作用是判断此viewholder是否应该显示某数据类型的数据
    //如果此viewholder应该显示此数据类型数据，就让他返回true
    //如果指定viewTypeParser，则这里不起作用,因此指定了viewTypeParser可以不重写此方法
    override fun isForViewType(data: String, position: Int): Boolean {
        //此类型的viewholder显示字符串是"item"的
        return data == "item"
    }

    //这里是数据绑定
    override fun convert(holder: BaseViewHolder, data: String, position: Int) {
        holder.getView<TextView>(R.id.tv2)?.text = data.toString()
    }

}
```

## 其他介绍

调用context.neko()方法构造一个用于显示rv的配置，block块中用于配置rv需要显示的viewholder类型，还可以配置layoutmanager，动画等。

类似的还有`listNeko`，`paging3Neko`构造ListAdapter，PagingDataAdapter

对于添加viewholder：

* 在上面的block块中调用addSingleItemView添加单一的viewholder
* 在上面的block块中调用addItemViews方法添加多种类型的viewholder

对于多种viewtype：
dsl块中的addItemView方法其实就是在实现ItemViewDelegate

* 在block块中指定ViewTypeParser区分不同的viewtype
* 或者实现ItemViewDelegate时重新isForViewType方法

动画

* 在block块中通过调用configAnim配置动画

最终调用show方法将adapter设置给RecyclerView

数据更新：每一个neko方法组装得到的config都包含着一个原始的recyclerView.adapter和相应的adapter

例如：

```kotlin
context.neko方法得到的config中有最基本的iNekoAdapter: RecyclerView.Adapter<out RecyclerView.ViewHolder> 和一个对应的nekoAdapter : NekoAdapter类型

        context.listNeko方法得到的config中有最基本的iNekoAdapter: RecyclerView.Adapter<out RecyclerView.ViewHolder> 和一个对应的nekoListAdapter : NekoListAdapter < T >
对于后面添加了header和footer后还可以得到head和footer相应的adapter 
```

因此对于数据更新，是从neko方法返回的config中得到adapter，更新数据

## 连接不同的adapter

例如我们有两个adapter

```
		//预定义数据
        val d1: MutableList<String> = mutableListOf()
        d1.addAll(listOf("a", "b", "c", "item", "d", "e", "r", "ee", "a", "b"))
        //预定义数据
        val d2: MutableList<String> = mutableListOf()
        d2.addAll(listOf("a", "b", "c", "item"))

        val neko1 = neko<String>(rv) {
            mDatas = d1.toMutableList()//指定adapter的数据
            //仅有一种viewHolder
            setSingleItemView(R.layout.item_1) { holder, data, position ->
                holder.getView<TextView>(R.id.tv1)?.text = data.toString()
            }
            //设置动画，对于concat连接的adapter,可以分别设置不同的动画。
            configAnim {
                itemAnimation = SlideInLeftAnimation()
            }
        }
        val neko2 = neko<String>(rv) {
            mDatas = d2.toMutableList()//指定adapter的数据
            //仅有一种viewHolder
            setSingleItemView(R.layout.item_2) { holder, data, position ->
                holder.getView<TextView>(R.id.tv2)?.text = data.toString()
            }
        }
```

我们现在拥有neko1和neko2，他两个持有了adapter和rv等一系列的配置，现在调用concat方法将他们连接起来，还可以在concat的block块中统一给其他adapter设置动画

```
//将两个adapter合并，此方法可以传入更多的adapter进行合并
        val concat = concatNeko(neko1, neko2) {
            // 自定义配置
            //统一给所有adapter设置动画
            setAnim(SlideInLeftAnimation())
        }
```

## header和footer

现在我们对adapter添加header和footer，可以对RecyclerView.Adapter，ListAdapter，PagingDataAdapter,ConcatAdapter使用。

以上一节连接adapter得到的ConcatAdapter为例：：

```
//给rv添加上header和footer，
val loadStateWrapper = concat.withLoadStatus {
    //配置footer
    withFooter {
        autoLoading = true
        //footer配置成一个跟loadstate没关联的itemview
        setItemDelegate(com.kiylx.libx.R.layout.footer_item) { header, loadstate ->
            //itemview绑定
            val m = header.getView<Button>(com.kiylx.libx.R.id.retry_button)!!
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
```

## 状态页

以上一节添加了header和footer的adapter为例，添加状态页：

```
//先设置header和footer，之后才可以用状态页包装
val statePage: IWrapper = loadStateWrapper.withPageState {
    //例如设置空布局
    setEmpty(R.layout.empty) {
        it.setOnClickListener {
            Log.d(tag, "被点击")
        }
    }

}.showStatePage()//立即显示状态页

//显示被包装起来的content
statePage.showContent()
```

## 拖拽和侧滑

侧滑和拖拽对recyclerview生效

```
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
