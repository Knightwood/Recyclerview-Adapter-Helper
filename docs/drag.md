# 拖拽与侧滑

| 函数                          | 描述                  |
|-----------------------------|---------------------|
| ListAdapterConfig<T>.drag   | ListAdapter的拖拽与侧滑删除 |
| NormalAdapterConfig<T>.drag | 普通Adapter的拖拽与侧滑删除   |

* NormalAdapterConfig<T>.drag
  定义：

```kotlin
fun <T : Any> NormalAdapterConfig<T>.drag(
    recyclerView: RecyclerView, 
    canSlideDelete: Boolean = false,//是否开启侧滑删除
    direction: Int = ItemTouchHelper.UP or ItemTouchHelper.DOWN,//可滑动的方向
)
```

* ListAdapterConfig<T>.drag
  定义：

```kotlin
fun <T : Any> ListAdapterConfig<T>.drag(
    recyclerView: RecyclerView,
    canSlideDelete: Boolean = false,
    direction: Int = ItemTouchHelper.UP or ItemTouchHelper.DOWN,
) 
```
## 使用：

```kotlin
val config = createNormalAdapterConfig<CollapseData> {
    addItemView(R.layout.item_1) {
        onBind { holder, data, position ->
            holder.getView<TextView>(R.id.tv1)?.text = data.s
        }
    }
    // .....
}
config.done(rv, d)//调用show方法完成recycleview的显示

config.drag(rv)//开启拖拽排序
```
