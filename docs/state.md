# 状态页

!!! note "前言"
    可以给任意适配器添加状态页       
    状态有四种：内容，Empty, Loading, Error,

全局配置  
为rv配置全局的状态页

```kotlin
    GlobalWrapperConfig.configStateView {
    //配置空页面
    this[PageStateTypes.Empty] = PageStateWrapperView(R.layout.empty) { v: View ->

    }
    //配置错误页面
    this[PageStateTypes.Error] = PageStateWrapperView(R.layour.error) { v: View ->

    }
}
```

使用`withPageState`方法给rv包装状态页。状态页的转换原理是：动态切换rv关联的适配器。
`withPageState`将返回`StateWrapperConfig`实例，此实例实现了状态页的配置，状态转换。

## StateWrapperConfig

| 状态            | 解释                |
|---------------|-------------------|
| setEmpty      | 设置空布局             |
| setLoading    | 设置加载中布局           |
| setError      | 加载错误布局            |
| showLoading   | 显示加载中布局           |
| showEmpty     | 显示空布局             |
| showContent   | 显示内容布局            |
| showError     | 显示错误布局            |
| showStatePage | 完成状态页配置，并立即显示内容布局 |

## 以concatAdapter为例

例如：为concatAdapter示例中连接两两个适配器后的的ConcatAdapter，设置状态页

```kotlin
val concat = concatMultiAdapter(adapter1, adapter2) {}
```

配置状态页

```kotlin
//先设置header和footer，之后才可以用状态页包装
val statePage: IWrapper = concat.withPageState {
    //例如设置空布局
    setEmpty(R.layout.empty) {
        it.setOnClickListener {
            Log.d(tag, "被点击")
        }
    }

}.showStatePage(linear())//完成配置。

//显示被包装起来的content
statePage.showContent()

//用一个延迟模拟网络请求
handler.postDelayed(Runnable {
    //显示空布局
    statePage.showEmpty()
    handler.postDelayed(Runnable {//模拟耗时逻辑之后，显示内容
        //显示内容
        statePage.showContent()
    }, 1000)

}, 2000)

```

## 以header_footer中得到的包装器为例

header_footer

```kotlin
val loadStateWrapper = concat.withLoadStatus(rv) {}
```

配置状态页

```kotlin
//先设置header和footer，之后才可以用状态页包装
val statePage: IWrapper = concat.withPageState {
    //... 同上
}

```