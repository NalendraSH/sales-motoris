package com.salesmotoris.mvp

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment

/**
 * Created by andrewkhristyan on 10/2/16.
 */
abstract class BaseMvpFragment<in V : BaseMvpView, T : BaseMvpPresenter<V>>
    : Fragment(), BaseMvpView {

    var ctx: Context? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mPresenter.attachView(this as V)
        ctx=context
    }
    override fun getContext(): Context{
        return ctx!!
    }

    protected abstract var mPresenter: T

    override fun showError(error: String?) {
        Toast.makeText(ctx, error, Toast.LENGTH_LONG).show()
    }

    override fun showError(stringResId: Int) {
        Toast.makeText(ctx, stringResId, Toast.LENGTH_LONG).show()
    }

    override fun showMessage(srtResId: Int) {
        Toast.makeText(ctx, srtResId, Toast.LENGTH_LONG).show()
    }

    override fun showMessage(message: String) {
        Toast.makeText(ctx, message, Toast.LENGTH_LONG).show()
    }

    override fun onDetach() {
        super.onDetach()
        mPresenter.detachView()
        ctx=null
    }
}