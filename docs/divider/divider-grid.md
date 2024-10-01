## 水平分隔线

<img src="https://i.loli.net/2021/08/14/oyjdg42zDUbkFtu.png" width="250"/>

```kotlin
val rv = findViewById<RecyclerView>(R.id.rv)

val config = createNormalAdapterConfig<CollapseData> {
    addItemView(R.layout.item_1) {
        onBind { holder, data, position ->
            holder.getView<TextView>(R.id.tv1)?.text = data.s
        }
    }
    // .....
}
val g=GridLayoutManager(this,3)
config.done(rv, d,g)//调用show方法完成recycleview的显示

rv.divider(R.drawable.divider_horizontal)
```


## 垂直分隔线

<img src="https://i.loli.net/2021/08/14/ChG9ZnNiJyasWFr.png" width="250"/>

```kotlin

rv.divider(R.drawable.divider_vertical, DividerOrientation.VERTICAL)
```

## 网格分隔线

<img src="https://i.loli.net/2021/08/14/NLAPphzIU6yvVnt.png" width="250"/>

```kotlin
rv.divider {
    setDrawable(R.drawable.divider_horizontal)
    orientation = DividerOrientation.GRID
}
```

## 边缘分隔线

通过两个字段可以控制边缘分隔线是否显示

| 字段 | 描述 |
|-|-|
| [startVisible](api/-b-r-v/com.drake.brv/-default-decoration/index.html#-2091559976%2FProperties%2F-900954490) | 是否显示上下边缘分隔线 |
| [endVisible](api/-b-r-v/com.drake.brv/-default-decoration/index.html#-377591023%2FProperties%2F-900954490) | 是否显示左右边缘分隔线 |
| [includeVisible](api/-b-r-v/com.drake.brv/-default-decoration/index.html#1716094302%2FProperties%2F-900954490) | 是否显示周围分隔线 |

### 上下

<img src="https://i.loli.net/2021/08/14/JBjETuMoaORFWHK.png" width="250"/>

```kotlin hl_lines="4"
rv.divider {
    setDrawable(R.drawable.divider_horizontal)
    orientation = DividerOrientation.GRID
    startVisible = true
}
```


### 左右

<img src="https://i.loli.net/2021/08/14/IcxHsWafFQXh4Eg.png" width="250"/>

```kotlin hl_lines="4"
rv.divider {
    setDrawable(R.drawable.divider_horizontal)
    orientation = DividerOrientation.GRID
    endVisible = true
}
```

### 四周

<img src="https://i.loli.net/2021/08/14/UmhH5BgFA3a1W2Q.png" width="250"/>

```kotlin hl_lines="4 5"
rv.divider {
    setDrawable(R.drawable.divider_horizontal)
    orientation = DividerOrientation.GRID
    startVisible = true
    endVisible = true
}
```

## 分隔线间隔

分隔线默认情况下是基于rv设置间隔

<img src="https://cdn.jsdelivr.net/gh/JBFiveHub/picture-storage@master/uPic/Clipboard - 2023-01-17 16.16.01.jpg" width="250"/>

```kotlin
rv.divider {
    orientation = DividerOrientation.GRID
    setDivider(1, true)
    setMargin(16, 16, dp = true)
    setColor(Color.WHITE)
}
```

<br>
使用`baseItemStart/baseItemEnd`参数以item为基准设置间隔

<img src="https://cdn.jsdelivr.net/gh/JBFiveHub/picture-storage@master/uPic/Clipboard - 2023-01-17 16.30.04.jpg" width="250"/>

<img src="https://cdn.jsdelivr.net/gh/JBFiveHub/picture-storage@master/uPic/Clipboard - 2023-01-17 16.33.04.jpg" width="250"/>

```kotlin
rv.divider {
    orientation = DividerOrientation.GRID
    setDivider(1, true)
    setMargin(16, 16, dp = true, baseItemStart = true)
    setColor(Color.WHITE)
}
```
