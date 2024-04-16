# ViewBinding

* 不支持DataBinding

## 使用ViewBinding

```kotlin
val config = createNormalAdapterConfig<String> {
    //添加ViewHolder
    addItemView(R.layout.item_1) {
        //绑定数据到ViewHolder
        onBind { holder, data, position ->
            //调用withBinding方法，传入与addItemView对应的布局id的Binding类即可
            holder.withBinding<Item1Binding> {
                tv2.text = data.toString()
            }
        }
    }
    // .....
}

config.done(rv, d)

```
