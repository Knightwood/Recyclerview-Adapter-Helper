package com.kiylx.recyclerviewneko.viewholder

abstract class ItemViewDelegate<T>(var type:Int,var layoutId: Int) {
    /**
     * 例如某类型数据里有个type字段，有四个取值:1，2，3，4.代表四种viewholder
     * 那么，可以通过 type==1 这样的判断，确定此ItemViewDelegate是否显示此数据，
     * 如果返回true，即此ItemViewDelegate(也就是此viewholder)将绑定此条数据显示内容
     * @param data
     * @param position
     * @return 对item(也就是dateList[position])判断，如果是显示这个类型的itemview,就返回true,否则返回false
     */
    open fun isForViewType(data: T, position: Int): Boolean = true

    /**
     * 将数据绑定到viewholder
     */
    abstract fun convert(holder: BaseViewHolder, data: T, position: Int)
}

