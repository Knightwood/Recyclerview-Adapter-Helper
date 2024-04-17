# ItemViewDelegate

ItemViewDelegate 是关于ViewHolder的描述，代理。   
调用addItemView方法时，就是在添加ItemViewDelegate

## 方法
ItemViewDelegate内部包含有：

| 函数          | 描述                   |
|-------------|----------------------|
| onClick     | 添加点击事件               |
| onLongClick | 添加长按事件               |
| isThisType  | 判断ViewType           |
| onBind      | 绑定数据到ViewHolder      |
| onCreate    | 创建ViewHolder时，干预创建过程 |

### onCreate
干预ViewHolder的创建过程   
例如 
你的ViewHolder是一个RecyclerView，你希望在创建ViewHolder时加载rv布局，配置相关内容，
而在onBindViewHolder时仅作数据集的刷新。
#### 示例：
有一个ViewPager2，现实的两个页面都是RecyclerView。    
而ViewPager2也是个RecyclerView。 
因此：
!!! note
    我们需要三个适配器：  
        两个作为ItemView显示数据。      
        一个作为ViewPager2的适配器，用于显示那两个ItemView。  
        两个ItemView的数据分别刷新。  


这是两个ItemView，且都是rv
```kotlin
//这个ViewHolder显示rv
val config1 = createNormalAdapterConfig<String> {
    //添加ViewHolder
    addItemView{
        onBind{}
    }
    // .....
}
val d1 = ItemViewDelegate<Int>(R.layout.rv_1)
        .isThisType { data, position ->
            position ==1
        }
        .onCreate { vh: BaseViewHolder ->
            val rv = vh.getView<RecyclerView>(R.id.rv)!!
            config1.done(rv, datas = deptData.devices)
        }
        .onBind { holder: BaseViewHolder, data: Int, position: Int ->
            config1.refresh(emptyList())
        }

//这个ViewHolder显示另一个rv
val config2 = createNormalAdapterConfig<String> {
    //添加ViewHolder
    addItemView{
        onBind{}
    }
    // .....
}
val d2 = ItemViewDelegate<Int>(R.layout.rv_2)
        .isThisType { data, position ->
            position !=1
        }
        .onCreate { vh: BaseViewHolder ->
            val rv = vh.getView<RecyclerView>(R.id.rv)!!
            config2.done(rv, datas = deptData.devices)
        }
        .onBind { holder: BaseViewHolder, data: Int, position: Int ->
            config2.refresh(emptyList())
        }

```
ViewPager2的适配器，显示两个ItemView
 
```kotlin
//数据集，rv只有两个ViewHolder，且每个ViewHolder都是rv
//所以数据无所谓，两个即可，
val d3 = mutableListOf<Int>()
d3.add(1)
d3.add(2)

val config = createNormalAdapterConfig<String> {
    //添加ViewHolder
    addItemView(d1)
    addItemView(d2)
    // .....
}
config.done(root_rv,d3)

//刷新某个ItemView的数据集
config1.normalAdapterrefreshData()
```

### onBind
将数据绑定到ViewHolder