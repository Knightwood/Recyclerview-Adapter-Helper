# Paging3

## 示例：

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
    addItemView(R.layout.item_1) {
        isThisType { data, position ->
            data.fileType == 2
        }
        // 。。。
    }
    //再添加一种新的viewholder
    addItemView(R.layout.item_2) {
        isThisType { data, position ->
            data.fileType != 2
        }
        // 。。。
    }
}
//完成配置
config.done(rv)

//提交、更新数据
viewLifecycleOwner.lifecycleScope.launch {
    lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
        viewModel.datas.collect { pagingData ->
            config.myPaging3Adapter.submitData(pagingData)
        }
    }
}

```

## header、footer

配置header、footer
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

```kotlin
config = createPaging3AdapterConfig<MediaResourceEntity>() {
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

    }
}

```