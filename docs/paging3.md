# Paging3

示例：

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
