package com.z.zjetpack.lifecycle

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.OnLifecycleEvent

/*
可以和Lifecycle一起工作的类我们称之为有生命周期意识的控件。
我们建议那些提供了需要和Android生命周期打交道的类的库最好提供有生命周期意识的控件，
这样它们的用户就能轻松的集成这些类，而不是在客户端手动管理生命周期。

LiveData 就是一个有生命周期意识的控件的例子。使用LiveData 和 ViewModel 可以让数据部署到UI更简单。


Lifecycle最佳实践
尽可能保持UI控制器（Activity和Fragment）的简洁。它们不应该去获取数据，而是使用ViewModel 来做这个工作，然后观察LiveData 把变化反应给view。

尝试写数据驱动的UI，UI controller的职责是在数据改变的时候更新view，或者把用户的操作通知给ViewModel。

把数据逻辑放在ViewModel 类中。ViewModel的角色是UI控制器与app其余部分的桥梁。不过要注意，ViewModel的职责并不是获取数据（比如，从网络）。相反 ViewModel应该调用合适的控件去做这件事情，然后把结果提供给UI控制器。

使用 Data Binding来让view和UI控制器之间的接口保持干净。这可以让你的view更加声明式同时最小化Activity和Fragment中的代码。如果你更喜欢用Java代码做这件事情，使用 Butter Knife来避免繁琐的代码。

如果你的UI非常复杂，考虑创建一个Presenter类来处理UI的变动。通常这会有点多余，但可能会让UI的测试更加简单。

绝对不要在 ViewModel中引用View 或者 Activity 的context。如果ViewModel活的比Activity更长，Activity可能会泄漏，无法正常回收。


Lifecycle已经纳入新版本的AppCompatActivity和Fragment中了，并且Lifecycle还是Android Jetpack中其他两个组件LiveData和ViewModel的基础


*/
class MyLifecycle : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun myOnCreate() {
        Log.v("tag", "myOnCreate")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun myOnResume() {
        Log.v("tag", "myOnResume")

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun myOnPause() {
        Log.v("tag", "myOnPause")
    }
}