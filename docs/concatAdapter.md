# 连接多个Adapter
使用ConcatAdapter连接多个适配器  
如果你需要在一个rv上添加头部布局或尾部布局，你可以在使用viewtype进行区分，当作不同的ViewHolder放在同一个adapter中  
也可以选择将头尾布局当作两种adapter， 使用ConcatAdapter将其与中间的内容部分连接起来

示例：  
例如连接如下两个适配器

```kotlin
//预定义数据
val d1: MutableList<String> = mutableListOf()
d1.addAll(listOf("a", "b", "c", "item", "d", "e", "r", "ee", "a", "b", "c", "dd"))
//预定义数据
val d2: MutableList<String> = mutableListOf()
d2.addAll(listOf("a", "b", "c", "item"))

val adapter1 = createNormalAdapterConfig<String> {
    addItemView(R.layout.item_1) {
        onBind { holder, data, position ->
            //使用with方法得到viewbinding实例
            holder.withBinding<Item1Binding> {
                tv1.text = data
            }
        }
    }
}
val adapter2 = createNormalAdapterConfig<String> {
    
    mDatas = d2.toMutableList()//可以预先指定adapter的数据
    
    addItemView(layoutId = R.layout.item_2) {
        onBind { holder, data, position ->
            holder.withBinding<Item2Binding> {
                tv2.text = data.toString()
            }
        }
    }
}
```

## 连接
```kotlin

//将两个adapter合并，此方法可以传入更多的adapter进行合并
val concat = concatMultiAdapter(adapter1, adapter2) {
    // 自定义配置
    //统一给所有adapter设置动画
    setAnim(SlideInLeftAnimation())
}

concat.done(rv, LinearLayoutManager(ctx))
```

## 数据更新
```kotlin

//给adapter刷新数据
adapter1.refreshData(d1)
adapter2.refreshData(d2)
//或者修改数据，然后通知adapter
adapter1.mDatas[0]="pppppp"
adapter1.normalAdapter.notifyItemXXX()
```