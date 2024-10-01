# 分割线

通过调用recyclerview的`addItemDecoration`方法给recyclerview添加分割线。
例如：
```kotlin
recyclerview.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
```
此时 RecyclerView 便会添加系统默认的灰色的分割线；

## 自定义分割线
继承RecyclerView.ItemDecoration类，重写其中的onDraw或是getItemOffsets方法

```kotlin
//绘制分割线
fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State?)
//获取偏移量
fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?)
```

## note
 [BRV](https://liangjingkanji.github.io/BRV)库里实现了一个通用的分割线类， 
 因此，我将修改后带进了这个库里。