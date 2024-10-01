## 网格分隔线

<img src="https://i.loli.net/2021/08/14/gx8mLuCNOFzWfIj.png" width="250"/>


```kotlin
val rv = findViewById<RecyclerView>(R.id.rv)

val config = createNormalAdapterConfig<DividerModel> {
    addItemView(R.layout.item_1) {
        onBind { holder, data, position ->
            // 模拟动态高度
            val layoutParams = holder.getConvertView().layoutParams
            layoutParams.height =holder.getBindData<DividerModel>().height
            itemView.layoutParams = layoutParams
        }
    }
    // .....
}
val g=GridLayoutManager(this,3)
config.done(rv, d,g)//调用show方法完成recycleview的显示
rv.divider(R.drawable.divider_horizontal)
```


## 边缘分隔线

通过两个字段可以控制边缘分隔线是否显示

| 字段 | 描述 |
|-|-|
| [startVisible](api/-b-r-v/com.drake.brv/-default-decoration/index.html#-2091559976%2FProperties%2F-900954490) | 是否显示上下边缘分隔线 |
| [endVisible](api/-b-r-v/com.drake.brv/-default-decoration/index.html#-377591023%2FProperties%2F-900954490) | 是否显示左右边缘分隔线 |
| [includeVisible](api/-b-r-v/com.drake.brv/-default-decoration/index.html#1716094302%2FProperties%2F-900954490) | 是否显示周围分隔线 |