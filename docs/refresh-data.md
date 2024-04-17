# 数据刷新

```kotlin
sealed class IConfig() {
    lateinit var iRecyclerViewAdapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>
}
sealed class BaseConfig<T : Any> : IConfig() {}
```

一系列的`createXXXAdapterConfig`方法，返回的都是继承自IConfig的配置类，
都可以从中获取到RecyclerView.Adapter<out RecyclerView.ViewHolder>类型适配器：iRecyclerViewAdapter

而不同的`createXXXAdapterConfig`方法，返回的配置类，除了iRecyclerViewAdapter，都有相应的适配器，
刷新数据可以直接获取到相应的adapter，直接调用熟悉的notifyItemXXX系列方法。

## 示例：

### createListAdapterConfig

```kotlin
val listAdapterConfig: ListAdapterConfig<String> = createListAdapterConfig<String>()

// 而相应的配置类：
class ListAdapterConfig<T : Any> : BaseConfig<T>() {
    lateinit var myListAdapter: MyListAdapter<T>
}
```

可以获取到继承自ListAdapter的myListAdapter
除了获取myListAdapter调用熟悉的notifyItemXXX系列方法，ListAdapterConfig还准备了：

| 方法              | 解释                              |
|-----------------|---------------------------------|
| moveData        | 将某位置数据换到另一位置                    |
| removeData      | 移除数据                            |
| refreshData     | notifyDataSetChanged刷新          |


### NormalAdapterConfig

```kotlin
val config: NormalAdapterConfig<String> = createNormalAdapterConfig<String>()

// 而相应的配置类：
class NormalAdapterConfig<T : Any> : BaseConfig<T>() {
    lateinit var normalAdapter: NormalAdapter<T>
    var mDatas: MutableList<T> = mutableListOf()
}
```

可以获取到继承自Adapter的normalAdapter  
除了获取normalAdapter调用熟悉的notifyItemXXX系列方法，NormalAdapterConfig还准备了：

| 方法              | 解释                              |
|-----------------|---------------------------------|
| removeRangeData | 移除某范围数据                         |
| moveData        | 将某位置数据换到另一位置                    |
| removeData      | 移除数据                            |
| refreshData     | notifyDataSetChanged刷新          |
| changeDatas     | 修改NormalAdapterConfig中mDatas数据集 |

### concatAdapter

```kotlin
val concat: ConcatConfig<String, BaseConfig<String>> = concatMultiAdapter(neko1, neko2)

// 而相应的配置类：
class ConcatConfig<T : Any, N : BaseConfig<T>>(val configList: Array<out N>, ) : IConfig() {
    lateinit var concatAdapter: ConcatAdapter
}

```

可以获取到ConcatAdapter

### MyPaging3Adapter

```kotlin
val config: Paging3AdapterConfig<String> = createPaging3AdapterConfig()

// 而相应的配置类：
class Paging3AdapterConfig<T : Any> : BaseConfig<T>() {
    lateinit var myPaging3Adapter: MyPaging3Adapter<T>
}


```
可以获取到myPaging3Adapter  
例如提交数据  
```kotlin
viewLifecycleOwner.lifecycleScope.launch {
    lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
        viewModel.datas.collect { pagingData ->
            config.myPaging3Adapter.submitData(pagingData)
        }
    }
}
```
