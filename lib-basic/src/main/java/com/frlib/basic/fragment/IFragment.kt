package com.frlib.basic.fragment

import android.os.Bundle
import android.view.View
import com.frlib.basic.app.IAppComponent
import com.frlib.basic.defaultpages.DefaultPagesContainer

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 14/02/2021 10:29
 * @desc fragment 接口
 */
interface IFragment {

    /**
     * 提供 AppComponent (提供所有的单例对象) 给实现类, 进行 Component 依赖
     *
     * @param appComponent
     */
    fun setupActivityComponent(appComponent: IAppComponent)

    /**
     * 初始化 View
     *
     * @param savedInstanceState
     * @return
     */
    fun initView(savedInstanceState: Bundle?)

    /**
     * 初始化数据
     */
    fun initData()

    /**
     * 设置点击事件
     */
    fun setClickListen()

    /**
     * 初始化Observable
     */
    fun initObservables()

    /**
     * 通过此方法可以使 Fragment 能够与外界做一些交互和通信, 比如说外部的 Activity 想让自己持有的某个 Fragment 对象执行一些方法,
     * 建议在有多个需要与外界交互的方法时, 统一传 [Message], 通过 what 字段来区分不同的方法, 在 [.setData]
     * 方法中就可以 `switch` 做不同的操作, 这样就可以用统一的入口方法做多个不同的操作, 可以起到分发的作用
     *
     * Example usage:
     * <pre>
     * public void setData(@Nullable Object data) {
     * if (data != null && data instanceof Message) {
     * switch (((Message) data).what) {
     * case 0:
     * loadData(((Message) data).arg1);
     * break;
     * case 1:
     * refreshUI();
     * break;
     * default:
     * //do something
     * break;
     * }
     * }
     * }
     *
     * // call setData(Object):
     * Message data = new Message();
     * data.what = 0;
     * data.arg1 = 1;
     * fragment.setData(data);
    </pre> *
     *
     *
     * [.setData] 框架是不会调用的, 自己去调用的, 让 [Activity] 或者其他类可以和 [Fragment] 通信,
     * 并且因为 [.setData] 是 [IFragment] 的方法, 所以你可以通过多态, 持有父类,
     * 不持有具体子类的方式就可以和子类 [Fragment] 通信, 这样如果需要替换子类, 就不会影响到其他地方,
     * 并且 [.setData] 可以通过传入 [Message] 作为参数, 使外部统一调用 [.setData],
     * 方法内部再通过 `switch(message.what)` 的方式, 从而在外部调用方式不变的情况下, 却可以扩展更多的方法,
     * 让方法扩展更多的参数, 这样不管 [Fragment] 子类怎么变, 它内部的方法以及方法的参数怎么变, 却不会影响到外部调用的任何一行代码
     *
     * @param data 当不需要参数时 `data` 可以为 `null`
     */
    fun setData(data: Any?)

    /**
     * 是否使用 EventBus
     * 请在项目中自行依赖对应的 EventBus 确保依赖后, 将此方法返回 true, Arms 会自动检测您依赖的 EventBus,
     * 并自动注册
     *
     * @return 返回 {@code true}, Arms 会自动注册 EventBus
     */
    fun useEventBus(): Boolean

    /**
     * 是否使用缺省页
     *
     * 统一管理缺省页
     */
    fun useDefaultPages(): Boolean

    /**
     * 缺省页容器
     */
    fun defaultPages(): DefaultPagesContainer
}