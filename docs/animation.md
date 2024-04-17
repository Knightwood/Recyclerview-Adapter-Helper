# Item出现时的动画

## 可用动画

| 动画类                    | 描述    |
|------------------------|-------|
| AlphaInAnimation       | 透明度动画 |
| ScaleInAnimation       | 缩放动画  |
| SlideInBottomAnimation | 从底部滑入 |
| SlideInLeftAnimation   | 从左侧滑入 |
| SlideInRightAnimation  | 从右侧滑入 |

## 使用

```kotlin
val config = createNormalAdapterConfig<CollapseData> {
    //...其余无关代码
    addItemView(R.layout.item_1) {}
    //设置动画
    setAnim(SlideInLeftAnimation())
    // .....
}
config.done(rv, d)//调用show方法完成recycleview的显示
```
## 配置
```kotlin
val config = createNormalAdapterConfig<CollapseData> {
    //...其余无关代码
    addItemView(R.layout.item_1) {}
    configAnim {
        //更详细的配置动画
        startAnimBlock = { anim: Animator, holder: RecyclerView.ViewHolder ->

        }
        //作用同 setAnim(SlideInLeftAnimation())
        itemAnimation = SlideInLeftAnimation()
        // 是否打开动画
        animationEnable = false
        // 动画是否仅第一次执行
        isAnimationFirstOnly = false
    }
    // .....
}
config.done(rv, d)//调用show方法完成recycleview的显示
```
## 自定义动画
参考示例实现动画类，然后如上面那样 调用 setAnim() 设置动画

```kotlin
class SlideInLeftAnimation @JvmOverloads constructor(
    private val duration: Long = 400L,
) : ItemAnimator {

    private val interpolator = DecelerateInterpolator(1.8f)

    override fun animator(view: View): Animator {
        val animator = ObjectAnimator.ofFloat(view, "translationX", -view.rootView.width.toFloat(), 0f)
        animator.duration = duration
        animator.interpolator = interpolator
        return animator
    }
}
```