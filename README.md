# Recyclerview-Adapter-Helper

* 支持多种adapter,也可以自己定义新的adapter传入
* 没有侵入和魔改
* 支持状态页回调
* 支持下拉显示header和上拉显示footer,下拉加载，上拉加载等
* 内置动画
* 支持viewbinding

# 使用

# [文档](https://knightwood.github.io/Recyclerview-Adapter-Helper/)

版本 [![](https://jitpack.io/v/Knightwood/Recyclerview-Adapter-Helper.svg)](https://jitpack.io/#Knightwood/Recyclerview-Adapter-Helper)

```css
dependencies {
       implementation 'com.github.Knightwood:Recyclerview-Adapter-Helper:Tag'
}
```

## 快速开始

```kotlin
//预定义数据
val d: MutableList<String> = mutableListOf()
d.addAll(listOf("a", "b", "c", "item"))

val neko1 = createNormalAdapterConfig<String> {
    mDatas = d1.toMutableList()//指定adapter的数据
    //添加一种viewHolder
    addItemView(R.layout.item_1) {
        isThisType{data, position ->
            //如果添加多种viewholder,可以使用此方法判断是否显示该viewholder
            //当此viewholder显示该data时，返回true
            //还有另一种判断viewholder的方式，调用addItemView时传入type,并在调用addItemView之前指定viewtypeparser
        }
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

## 判断viewtype：
有两种方式判断viewtype：
* 使用ViewTypeParser判断viewtype
* 使用isThisType判断viewtype

### 只有一种viewholder
    直接调用addItemView即可，不需要判断viewtype

```kotlin
val neko1 = createNormalAdapterConfig<String> {
    mDatas = d1.toMutableList()//指定adapter的数据
    //添加一种viewHolder
    addItemView(R.layout.item_1) {
        //不需要判断viewtype的处理
    }

}
```

### 使用ViewTypeParser判断viewtype
```kotlin
val neko1 = createNormalAdapterConfig<String> {
    mDatas = d1.toMutableList()//指定adapter的数据
    viewTypeParser=object :ViewTypeParser<String>{
        override fun parse(data: String, pos: Int): Int {
            if (pos == 1){
                return 1
            }else{
                return 2
            }
        }
    }
    
    //添加一种viewHolder
    addItemView(R.layout.item_1,type=1) { //这里传入了viewholder的type

    }

}
```

### 使用isThisType判断viewtype
```kotlin
val neko1 = createNormalAdapterConfig<String> {
    mDatas = d1.toMutableList()//指定adapter的数据
    //添加一种viewHolder
    addItemView(R.layout.item_1) {//使用isThisType方法判断viewtype时，不需要指定ViewTypeParser
        isThisType{data, position ->
            //如果添加多种viewholder,可以使用此方法判断是否显示该viewholder
            //当此viewholder显示该data时，返回true
            //还有另一种判断viewholder的方式，调用addItemView时传入type,并在调用addItemView之前指定viewtypeparser
        }
    }

}
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
            addItemView(R.layout.item_1){
                isThisType{data, position ->
                    data.fileType ==2
                }
                // 。。。
            }
            //再添加一种新的viewholder
            addItemView(R.layout.item_2){
                isThisType{data, position ->
                    data.fileType != 2
                }
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
