# 多种ViewHolder

* 通过多次调用addItemView()方法添加ViewHolder
* 配置类型解析器或重写isThisType方法来区分类型

ps：
ItemViewDelegate类包含了描述ViewHolder的内容，例如布局id，点击事件，绑定ViewHolder的方法等。  
ItemViewDelegate和不同种类的ViewHolder一一对应，因此，在文档中ItemViewDelegate类可以视为ViewHolder来称呼。

## addItemView方法

| 函数                                                                         | 描述 添加ViewHolder      |
|:---------------------------------------------------------------------------|----------------------|
| 使用 isThisType 判断类型的方法                                                      |                      |
| addItemView(layoutId: Int, block: ItemViewDelegate<T>.() -> Unit)          | 指定布局id               |
| addItemView(v: View, block: ItemViewDelegate<T>.() -> Unit)                | 指定布局View             |
| 使用 ViewTypeParser 判断类型的方法                                                  |                      |
| addItemView(layoutId: Int,type: Int,block: ItemViewDelegate<T>.() -> Unit) | 指定布局id               |
| addItemView(v: View,type: Int,block: ItemViewDelegate<T>.() -> Unit)       | 指定布局View             |
| 直接传入itemViewDelegates 的方法                                                  |                      |
| addItemView(vararg itemViewDelegates: ItemViewDelegate<T>)                 | 传递多个ItemViewDelegate |
| addItemView(vararg itemViewDelegates: DelegatePair<T>)                     | 传递多个DelegatePair     |


## 区分类型 

### 方式1：重写isThisType方法

使用isThisType方法判断viewtype时，不需要指定类型解析器ViewTypeParser  
在isThisType方法中，判断数据是否由此Viewholder显示，如果是，返回true即可

```kotlin
val config = createNormalAdapterConfig<SampleData> {
    //添加一种viewHolder
    addItemView(R.layout.item_1) {
        isThisType { data, position ->
            //该ViewHolder显示data.type为2 的数据，因此当data.type == 2时返回true
            return data.type == 2
        }
        onBind {}
    }
    //添加另一种viewHolder
    addItemView(R.layout.item_2) {
        isThisType { data, position ->
            //该ViewHolder显示data.type为3 的数据，因此当data.type == 3时返回true
            return data.type == 3
        }
        onBind {}
    }

}
```

### 方式2：类型解析器

1. 指定viewTypeParser，并根据数据返回不同viewType
2. 在addItemView方法中，指定该ViewHolder的viewType

```kotlin
val neko1 = createNormalAdapterConfig<String> {
    viewTypeParser = object : ViewTypeParser<String> {
        override fun parse(data: String, pos: Int): Int {
            if (pos == 1) {
                return 1
            } else {
                return 2
            }
        }
    }

    //添加一种viewHolder
    addItemView(R.layout.item_1, type = 1) { //这里传入了viewholder的type
        onBind {}
    }
    //添加另一种viewHolder
    addItemView(R.layout.item_2, type = 2) {
        onBind {}
    }

}
```

### 如果只有一种ViewType

不需要指定viewTypeParser，也不需要isThisType方法，直接调用addItemView添加ViewHolder即可

```kotlin
val config = createNormalAdapterConfig<SampleData> {
    //添加一种viewHolder
    addItemView(R.layout.item_1) {
        onBind {}
    }
}
```

## ItemViewDelegate

我们在前面说，调用addItemView方法添加ViewHolder，实际上，addItemView方法添加的是一个名为ItemViewDelegate的实例。

1. 调用addItemView方法后，ItemViewDelegate会被添加到管理器。
2. 在adpater创建ViewHolder时，会根据类型viewType找到相应ItemViewDelegate。
3. 然后将绑定数据，创建或是点击事件，交给ItemViewDelegate处理。

### 直接传递ItemViewDelegate类型的addItemView 添加ViewHolder

```kotlin
val delegate1 = ItemViewDelegate<String>(R.layout.item_1)
    .isThisType { data, position ->
        true 
    //如上面的例子，可以在这里判断类型，或使用类型解析器
    //如果使用类型解析器，这里将失去作用
    //如果仅有一种ViewType，此方法可以不写    
    }
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