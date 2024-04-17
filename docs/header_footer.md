# 下拉header与上拉footer

!!! note "前言"
    实验特性，不太完善

可以给任意adapter 添加header和footer（但pagingDataAdapter例外），并在显示时回调事件，例如刷新数据或加载更多等
原理是通过ConcatAdapter将header、内容、footer三部分连接起来，ConcatAdapter只能连接adapter，
所以，header和footer也必须是adapter，而paging3里有LoadStateAdapter<T>，正好可以拿来当作header、footer使用

## 添加header和footer

使用withLoadStatus配置header、footer

### withLoadStatus

| 函数                     | 描述                      |
|------------------------|-------------------------|
| IConfig.withLoadStatus | 对adapter连接header和footer |

withLoadStatus方法生成一个MyAdapterLoadStatusWrapperUtil，可以进行header和footer的配置。

### MyAdapterLoadStatusWrapperUtil中的方法：

| 函数                       | 描述                                   |
|--------------------------|--------------------------------------|
| 配置header、footer          |                                      |
| withHeader               | 配置header                             |
| withFooter               | 配置footer                             |
| 事件监听                     |                                      |
| whenScrollToEnd          | 设置当滑动到底部时的回调监听                       |
| whenScrollToTop          | 设置当滑动到顶部时的回调监听                       |
| whenNotFull              | 当数据不满一屏，手指滑动时触发                      |
| 完成配置                     |                                      |
| completeConfig           | 完成header和footer的包装                   |
| done                     | 为rv设置adapter和layoutManager，调用此方法完成显示 |
| 修改header、footer状态，转换显示效果 |
| headerState              | 通过此方法改变header的itemview状态             |
| footerState              | 通过此方法改变footer的itemview状态             |
| finish                   | 设置header或footer adapter加载状态          |

### header、footer

header、footer也具有不同状态，例如上拉时可以显示“正在加载”，无法获取到数据时，可以将其转换到“加载失败”布局，并提供点击按钮重试。

其实例是LoadStateAdapter<T> 通过配置不同状态下的itemview完成切换布局  
其状态有:

| 方法         | 解释   |
|------------|------|
| NotLoading | 非加载中 |
| Loading    | 正在加载 |
| Error      | 加载失败 |


header、footer可配置属性或方法有：

| 方法、属性           | 解释                                                                                                             |
|-----------------|----------------------------------------------------------------------------------------------------------------|
| 属性              | ---------------------------------------------------------                                                      |
| autoClose       | 如果值大于0.则在触发[LoadState.Loading]状态后，延迟[autoClose]毫秒后触发[LoadState.NotLoading]                                     |
| autoLoading     | true时自动触发[LoadState.Loading]状态 false时需手动切换[LoadState]状态                                                        |
| useType         | 是否以loadState区分不同的viewHolder 若是使用[addItemDelegate]添加viewholder，则此处为true，[setItemDelegate]设置的单个viewholder则不修改此状态 |
| 方法              | ---------------------------------------------------------                                                      |
| addItemDelegate | 为header、footer配置多种加载状态，类似于配置多ViewHolder                                                                        |
| setItemDelegate | 为header、footer配置一种加载状态，类似于配置单种类型ViewHolder                                                                     |

## 示例：

例如：为concatAdapter示例中连接两个适配器后的的ConcatAdapter，添加header、footer

```kotlin
val concat = concatMultiAdapter(adapter1, adapter2) {}
```

1. 使用`withLoadStatus`方法将适配器包裹上header和footer
2. 在block块中使用`withFooter`、`withHeader`方法自定义header或footer内容
3. 使用`whenXX`系列方法作事件监听

示例：

```kotlin
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
    withHeader {
        //配置header，方法与withFooter一样使用
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