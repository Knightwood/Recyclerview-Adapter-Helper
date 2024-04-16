# ListAdapter

* ListAdapter 需要一个DiffUtil.ItemCallback，用来比对数据集  
* 如果更新数据，需要一个新的List，不可以用原List

示例：

```kotlin
val d: MutableList<String> = mutableListOf()
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
    addItemView(R.layout.item_1){
        onBind { holder, data, position ->
            holder.getView<TextView>(R.id.tv1)?.text = data.toString()
        }
    }
}
//完成配置
listAdapterConfig.done(rv)
//开启拖拽排序和策划删除
listAdapterConfig.drag(rv, true)
//刷新数据
listAdapterConfig.submitList(d)
```