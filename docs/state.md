# 状态页

可以给任意适配器添加状态页    
状态有四种，内容，Empty, Loading, Error,

全局配置

```kotlin
    GlobalWrapperConfig.configStateView {
    //配置空页面
    this[PageStateTypes.Empty] = PageStateWrapperView(R.layout.empty) {v:View->

    }
    //配置错误页面
    this[PageStateTypes.Error] = PageStateWrapperView(R.layour.error) {v:View->

    }
}
```

## 以concatAdapter为例

例如concatAdapter示例中连接两两个适配器后的的ConcatAdapter

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

}.showStatePage(linear())//立即显示此状态页

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