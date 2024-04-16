# 头尾布局

使用的concatAdapter和LoadStateAdapter配合，可以上拉和下拉显示头尾布局

例如concatAdapter示例中连接两个适配器后的的ConcatAdapter
```kotlin
val concat = concatMultiAdapter(adapter1, adapter2) {}
```

## 添加header和footer

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