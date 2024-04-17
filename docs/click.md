# 点击事件

| 函数          | 描述         |
|-------------|------------|
| onClick     | 添加指定Id点击事件 |
| onLongClick | 添加指定Id长按事件 |

点击事件和长按事件都被局限于ItemViewDelegate，因此，即使两个ViewHolder中的view id一致，也不会被覆盖

!!! warning
    尽量避免在onBind方法中设置点击事件，这回因为rv反复绑定数据造成不必要的开销

## 点击事件

```kotlin
val config = createNormalAdapterConfig<String> {
    //添加ViewHolder
    addItemView(R.layout.item_1) {
        //...其余代码省略
        //配置ViewHolder的点击事件，可传入多个视图的id设置点击事件
        onClick(R.id.tv1, R.id.tv2) { v: View, holder: BaseViewHolder ->
            //点击事件处理
        }
    }
    // .....
}
```

## 长按事件

```kotlin
val config = createNormalAdapterConfig<String> {
    //添加ViewHolder
    addItemView(R.layout.item_1) {
        //...其余代码省略
        //配置ViewHolder的长按事件，可传入多个视图的id设置事件
        onLongClick(R.id.tv1, R.id.tv2) { v: View, holder: BaseViewHolder ->
            //事件处理
        }
    }
    // .....
}
```

## 直接传递ItemViewDelegate类型的addItemView 点击事件和长按事件

```kotlin
val delegate1 = ItemViewDelegate<String>(R.layout.item_1)
    .onBind { holder, data, pos ->
        holder.getView<TextView>(R.id.tv1)?.text = data
    }
    //定义点击事件
    .onClick(R.id.tv1) { v, h ->
        Toast.makeText(applicationContext, "pos:${h.layoutPosition}", Toast.LENGTH_SHORT).show()
    }

val config = createNormalAdapterConfig<String> {
    //添加ViewHolder
    addItemView(delegate1)//添加上面的ItemViewDelegate
    // .....
}
```

## 多个ItemViewDelegate共享一个事件处理

这种方式下，不同ViewHolder中，如果View id一致，你需要从ViewHolder种获取data来判断该事件来自哪种ViewType
```kotlin
val config = createNormalAdapterConfig<String> {
    //将事件独立出来
    val click = ItemClickListener { view, holder ->
        val data = holder.getBindData<String>()//从ViewHolder种获取绑定的数据
    }

    //添加ViewHolder
    addItemView(layoutId = R.layout.test_item) {
        //...省略其他代码
        onClick(R.id.tv_1, R.id.tv_2, clickEvent = click)
    }
    //添加ViewHolder
    addItemView(layoutId = R.layout.sample_item) {
        //...省略其他代码
        onClick(R.id.tv_3, R.id.btn_1, clickEvent = click)
    }
    // .....
}
```