package com.hanna.zava.comicslist.extensions

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


/***
 * This Extensions enables a safe viewBinding when used on fragments, using a single line,
 * causing no need to handle when destroyed and viewCreated
 * @see <a href="https://medium.com/@Zhuinden/simple-one-liner-viewbinding-in-fragments-and-activities-with-kotlin-961430c6c07c">Medium source</a>
 */
class FragmentViewBindingDelegate<T : ViewBinding>(
    val fragment: Fragment,
    val viewBindingFactory: (View) -> T
) : ReadOnlyProperty<Fragment, T> {
    private var binding: T? = null

    init {
        fragment.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                fragment.viewLifecycleOwnerLiveData.observe(fragment, { viewLifecycleOwner ->
                    viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                        override fun onDestroy(owner: LifecycleOwner) {
                            binding = null
                        }
                    })
                })
            }
        })
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        val binding = binding
        if (binding != null) {
            return binding
        }
        val lifecycle = fragment.viewLifecycleOwner.lifecycle
        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            throw IllegalStateException("Should not attempt to get bindings when Fragment views are destroyed.")
        }
        return viewBindingFactory(thisRef.requireView()).also { this.binding = it }
    }
}

fun <T : ViewBinding> Fragment.viewBinding(viewBindingFactory: (View) -> T) =
    FragmentViewBindingDelegate(this, viewBindingFactory)