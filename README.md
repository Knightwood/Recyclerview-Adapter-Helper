# RecyclerviewNeko

* 支持多种adapter,也可以自己定义新的adapter传入
* 没有侵入和魔改

# 使用

* 版本 [![](TyporaRaw/README.assets/RecyclerViewNeko.svg+xml)](https://jitpack.io/#Knightwood/RecyclerViewNeko)

  ```css
  dependencies {
  	        implementation 'com.github.Knightwood:RecyclerViewNeko:Tag'
  	}
  ```
  
* 简单示例方式

```

    inner class Delegate1 : ItemViewDelegate<String>(1, R.layout.item_1) {
        /**
         * 绑定数据到viewholder
         */
        override fun convert(holder: BaseViewHolder, data: String, position: Int) {
            holder.getView<TextView>(R.id.tv1)?.text = data.toString()
        }
    }

    inner class Delegate2 : ItemViewDelegate<String>(2, R.layout.item_2) {
        override fun convert(holder: BaseViewHolder, data: String, position: Int) {
            holder.getView<TextView>(R.id.tv2)?.text = data.toString()
		 }
    }
    
    fun initRv(){
     //预定义数据
        val d: MutableList<String> = mutableListOf()
        d.addAll(listOf("a", "b", "c", "item"))

        //两个viewholder类型
        val item1 = Delegate1()
        val item2 = Delegate2()
        //泛型指定了此recyclerview显示什么类型的数据
        val neko = neko<String>(rv) {
            //在有多种viewholder时，根据数据类型返回不同的viewtype
            viewTypeParser = ViewTypeParser<String> { data, pos ->
                if (data == "item") 1 else 2
            }

            //1. 多种viewtype可以使用[addItemViews]将多种viewholder添加进去
            addItemViews(item1, item2)

            //给整个itemview设置点击事件
            itemClickListener = ItemClickListener { view, holder, position ->
                Toast.makeText(applicationContext, mDatas[position], Toast.LENGTH_LONG).show()
            }


        }.show(d)//调用show方法完成recycleview的显示,并传入数据显示

        //刷新数据
        neko.refreshData(d)
    }
    }
```




0. 调用neko方法，传入相应设置就可以显示列表

```
例如
1. 定义itemviewDelegate
class Delegate1 : ItemViewDelegate<String>(viewtype, 布局id) {
        override fun convert(holder: BaseViewHolder, data: String, position: Int) {
            //数据绑定到viewholder
        }
    }
2.组装recyclerview并显示
val rvNeko=xxxNeko<数据类型>(rv){
		//配置
		addItemViews()//添加itemviewDelegate
		addItemView()//添加itemviewDelegate,但不能和上面的混用
		
}.show(datas) //就此完成recyclerview显示


3.数据刷新,不同的XXXNeko将会给予不同的config实例，通过此实例获取相应的adapter用于数据刷新等
rvNeko.相应adapter.数据刷新()

4. 对于多种viewholder
有两种方式指定：
一是重写ItemViewDelegate中的isForViewType方法
二是在xxxNeko配置recyclerview时传入ViewTypeParser解析viewtype

注：两种方式不会同时生效，优先ViewTypeParser生效，但调用addItemView方法添加itemviewDelegate会另isForViewType方法优先
xxxNeko<数据类型>(rv){
			//配置
	//指定viewtype解析器
 viewTypeParser = ViewTypeParser<String> { data, pos ->
                if (data == "item") 1 else 2
            }
}
5.两种添加itemviewDelegate方法：
		addItemViews()//添加itemviewDelegate
		addItemView()//添加itemviewDelegate,但不能和上面的混用
两种都可以做到单一/多种类的viewholder
```



## 使用ItemViewDelegate取代viewholder, 在ItemViewDelegate中绑定数据

```

    /**
     * 代替viewholder
     */
    inner class Delegate1 : ItemViewDelegate<String>(1, R.layout.item_1) {
        /**
         * 绑定数据到viewholder
         */
        override fun convert(holder: BaseViewHolder, data: String, position: Int) {
            holder.getView<TextView>(R.id.tv1)?.text = data.toString()
        }
    }
	/**
     * 代替viewholder
     */
    inner class Delegate2 : ItemViewDelegate<String>(2, R.layout.item_2) {
        //在有多种viewholder时，
        //此方法的作用是判断此viewholder是否应该显示某数据类型的数据
        //如果此viewholder应该显示此数据类型数据，就让他返回true
        //如果指定viewTypeParser，则这里不起作用,因此指定了viewTypeParser可以不重写此方法
        override fun isForViewType(data: String, position: Int): Boolean {
            //此类型的viewholder显示字符串是"item"的
            return data == "item"
        }

        override fun convert(holder: BaseViewHolder, data: String, position: Int) {
            holder.getView<TextView>(R.id.tv2)?.text = data.toString()
        }

    }
```

## 多种adapter用于recyclerview显示

1. 使用普通的adapter显示列表


```
    /**
     * 普通的adapter
     */
    fun nekoTest() {
        //预定义数据
        val d: MutableList<String> = mutableListOf()
        d.addAll(listOf("a", "b", "c", "item"))

        //两个viewholder类型
        val item1 = Delegate1()
        val item2 = Delegate2()
        //泛型指定了此recyclerview显示什么类型的数据
        val neko = neko<String>(rv) {
            //在有多种viewholder时，根据数据类型返回不同的viewtype
            //当不指定这个解析器时，就得重写ItemViewDelegate中的isForViewType方法来判断viewtype
            viewTypeParser = ViewTypeParser<String> { data, pos ->
                if (data == "item") 1 else 2
            }

            mDatas = d.toMutableList()//指定adapter的数据。也可以现在不指定数据，在后面的show方法中传入数据

            //1. 多种viewtype可以使用[addItemViews]将多种viewholder添加进去
            addItemViews(item1, item2)

            //2. 单一viewtype，使用[addItemView]方法添加viewholder
            //这种单一viewholder, 用不上viewTypeParser
            //注意： 不要与[addItemViews]混用
//            addItemView(R.layout.item_3) { holder, t, position ->  //数据绑定
//                holder.getView<TextView>(R.id.tv2)?.text = t.toString()
//            }

            //给整个itemview设置点击事件
            itemClickListener = ItemClickListener { view, holder, position ->
                Toast.makeText(applicationContext, mDatas[position], Toast.LENGTH_LONG).show()
            }
            itemLongClickListener = ItemLongClickListener { view, holder, position ->
                Toast.makeText(applicationContext, mDatas[position], Toast.LENGTH_LONG).show()
                true
            }

        }.show()//调用show方法完成recycleview的显示

        neko.mDatas[1] = "eee"
        //刷新数据
        (neko.iNekoAdapter as NekoAdapter).notifyItemChanged(1)
        //刷新数据
        neko.refreshData(d)
    }
```

2. 使用listadapter显示列表


```
    /**
     * listadapter
     */
    fun listNekoTest() {

        val d: MutableList<String> = mutableListOf<String>()
        val item1 = Delegate1()
        val item2 = Delegate2()
        val neko = listNeko<String>(
            recyclerView = rv,
            diffCallback = object : DiffUtil.ItemCallback<String>() {            //指定diffCallback
                override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                    TODO("Not yet implemented")
                }

                override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                    TODO("Not yet implemented")
                }
            }) {
            //根据数据类型返回不同的viewtype
            viewTypeParser = ViewTypeParser<String> { data, pos ->
                if (data == "item")
                    1
                else
                    2
            }
            mDatas = d.toMutableList()//指定adapter的数据
            addItemViews(item1, item2)
        }.show()
        neko.mDatas[1] = "eee"
        //刷新数据
        neko.submitList(d)
        //刷新数据
        neko.nekoListAdapter.submitList(null)
    }
```

3. 使用pagingDataAdapter显示列表

```
    /**
     * pagingDataAdapter
     */
    fun pagingTest() {
        paging3Neko(rv, object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                TODO("Not yet implemented")
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                TODO("Not yet implemented")
            }
        }) {

        }
    }
```

4. 使用concatAdapter显示列表


```
    /**
     * concatAdapter
     */
    fun concatTest() {

        //预定义数据
        val d1: MutableList<String> = mutableListOf()
        d1.addAll(listOf("a", "b", "c", "item"))

        //预定义数据
        val d2: MutableList<String> = mutableListOf()
        d2.addAll(listOf("a", "b", "c", "item"))

        val neko1 = neko<String>(rv) {
            mDatas = d1.toMutableList()//指定adapter的数据
            addItemView(R.layout.item_1) { holder, data, position ->
                holder.getView<TextView>(R.id.tv1)?.text = data.toString()
            }
        }
        val neko2 = neko<String>(rv) {
            mDatas = d2.toMutableList()//指定adapter的数据
            addItemView(R.layout.item_2) { holder, data, position ->
                holder.getView<TextView>(R.id.tv2)?.text = data.toString()
            }
        }
        concat(neko1, neko2) {
            // todo 自定义配置
        }
    }
```

