## 水平分隔线

<img src="https://i.loli.net/2021/08/14/IoBfnz6ERXVHlq3.png" width="250" />

创建`drawable`来描述分隔线

```xml
<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <solid android:color="@color/dividerDecoration" />
    <size android:height="5dp" />
</shape>
```

创建列表，添加分割线
```kotlin  hl_lines="14"
val rv = findViewById<RecyclerView>(R.id.rv)

val config = createNormalAdapterConfig<CollapseData> {
    addItemView(R.layout.item_1) {
        onBind { holder, data, position ->
            holder.getView<TextView>(R.id.tv1)?.text = data.s
        }
    }
    // .....
}
config.done(rv, d)//调用show方法完成recycleview的显示

//添加分割线
rv.divider(R.drawable.divider_horizontal)

```

!!! warning "重复调用"
    divider实现是`addItemDecoration`, 重复调用会叠加分隔物

## 垂直分隔线

<img src="https://i.loli.net/2021/08/14/rAeDXkfV6HxJUym.png" width="250"/>

创建`drawable`来描述分隔线
```xml
<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <solid android:color="@color/dividerDecoration" />
    <size android:width="5dp" />
</shape>
```

添加分割线
```kotlin
rv.divider(R.drawable.divider_vertical)
```


- 使用`drawable`资源可复用分隔线, 其宽高就是分隔线的宽高
- 如果水平分隔线, 则`drawable`的宽度值无效(实际宽度值为RecyclerView的宽)
- 如果垂直分隔线, 则`drawable`的高度值无效(实际分隔线高度为RecyclerView高度)


## 边缘分隔线

| 字段 | 描述 |
|-|-|
| [startVisible](api/-b-r-v/com.drake.brv/-default-decoration/index.html#-2091559976%2FProperties%2F-900954490) | 是否显示首部分隔线 |
| [endVisible](api/-b-r-v/com.drake.brv/-default-decoration/index.html#-377591023%2FProperties%2F-900954490) | 是否显示尾部分隔线 |
| [includeVisible](api/-b-r-v/com.drake.brv/-default-decoration/index.html#1716094302%2FProperties%2F-900954490) | 是否显示首尾分隔线 |

<img src="https://i.loli.net/2021/08/14/iL5epWdOQKnwZAc.png" width="250"/>

两个字段控制首尾是否显示分隔线

```kotlin hl_lines="3 4"
rv.divider {
    setDrawable(R.drawable.divider_horizontal)
    startVisible = true
    endVisible = true
}
```

## 四周全包裹

<img src="https://i.loli.net/2021/08/14/lGSOPdg5A8WInoL.png" width="250"/>

使用GridLayoutManager且`spanCount`为1

```kotlin
rv.divider{
    setDrawable(R.drawable.divider_horizontal)
    orientation = DividerOrientation.GRID
    includeVisible = true
}
```

## 分隔线间隔

有两种方式

1. 创建有间隔`内间距`的drawable, 以下为间距16水平分隔线

    ```xml hl_lines="2"
    <inset xmlns:android="http://schemas.android.com/apk/res/android"
        android:insetLeft="16dp"
        android:insetRight="16dp">
        <shape>
            <solid android:color="@color/dividerDecoration" />
            <size android:height="5dp" />
        </shape>
    </inset>
    ```

2. 使用setMargin()

    ```kotlin hl_lines="3"
    rv.divider {
        setDivider(1, true)
        setMargin(16, 0, dp = true)
        setColor(Color.WHITE)
    }
    ```

