# 基本使用


!!! note "前言"
    支持的Adapter有:  
    - Adapter  
    - ListAdapter  
    - PagingDataAdapter  
    - ConcatAdapter  

示例：

```kotlin
//模拟数据
val d: MutableList<String> = mutableListOf()
for (i in 0..100) {
    d.add(RandomUtil.generateByRandom(9))
}

//生成一个adapter配置
//泛型指定了此recyclerview显示什么类型的数据
val config = createNormalAdapterConfig<String> {
    //（可选）指定layoutManager,如不指定，会默认使用LinearLayoutManager
    layoutManager = LinearLayoutManager(ctx)
    //（可选）预先指定adapter的数据
    mDatas = d2.toMutableList()

    //添加ViewHolder
    addItemView(R.layout.item_1) {
        //绑定数据到ViewHolder
        onBind { holder, data, position ->
            holder.getView<TextView>(R.id.tv1)?.text = data.toString()
        }
        //配置ViewHolder的点击事件，可传入多个视图的id设置点击事件
        onClick(R.id.tv1) { v1, h ->
            //点击事件处理
        }
    }
    // .....
}
//使用done方法完成配置，将rv和adapter组装起来，完成显示
config.done(rv, d)

```

## 数据更新

```kotlin
//给adapter刷新数据
config.refreshData(d1)
//或者修改数据，然后通知adapter
config.mDatas[0] = "pppppp"
config.normalAdapter.notifyItemXXX() //notifyItem系列方法
```