package ru.practicum.android.diploma.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class UniqueEventLiveData<T> : MutableLiveData<T>() {

    private val isPending = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner) { data ->
            if (isPending.getAndSet(false)) {
                observer.onChanged(data)
            }
        }
    }

    override fun setValue(value: T?) {
        isPending.set(true)
        super.setValue(value)
    }
}
