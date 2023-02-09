package com.app.flix.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.ViewDataBinding

open abstract class BaseBindingActivity<B : ViewDataBinding> : BaseActivity() {

    private var mBinding: B? = null

    abstract fun inflateBindingLayout(inflater: LayoutInflater): B


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = setupBinding(layoutInflater)
        setContentView(mBinding!!.root)
    }


    open fun getBinding(): B? {
        return mBinding
    }


     private fun setupBinding(inflater: LayoutInflater): B {
        return inflateBindingLayout(inflater)
    }

}