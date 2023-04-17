package com.kiylx.recyclerviewneko.nekoadapter.config

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.kiylx.recyclerviewneko.viewholder.BaseViewHolder
import com.kiylx.recyclerviewneko.wrapper.WrapperTypes
import java.util.*


open class WrapperConfig {
    val wrapperMap: EnumMap<WrapperTypes, Adapter<BaseViewHolder>> =
        EnumMap(WrapperTypes::class.java)
    var recyclerView: RecyclerView? = null

    /**
     * 第一个被包装起来的adapter
     */
    lateinit var firstWrappedAdapter: Adapter<BaseViewHolder>

    private val stack: Stack<Adapter<BaseViewHolder>> = Stack()

    /**
     * 上一个被包装的adapter
     */
    val lastWrappedAdapter: Adapter<BaseViewHolder>
        get() = stack.peek()


    fun add(wrapperTypes: WrapperTypes, adapter: Adapter<BaseViewHolder>) {
        wrapperMap[wrapperTypes] = adapter
        stack.push(adapter)
    }

    companion object {
        operator fun <T : Any> invoke(config: BaseConfig<T>): WrapperConfig {
            return WrapperConfig().apply {
                firstWrappedAdapter = config.iNekoAdapter
            }
        }

        operator fun <T : Any, N : BaseConfig<T>> invoke(config: ConcatConfig<T, N>): WrapperConfig {
            return WrapperConfig().apply {
                firstWrappedAdapter = config.concatAdapter as Adapter<BaseViewHolder>
            }
        }
    }

    operator fun get(wrapperTypes: WrapperTypes): Adapter<BaseViewHolder>? {
        return wrapperMap[wrapperTypes]
    }
}