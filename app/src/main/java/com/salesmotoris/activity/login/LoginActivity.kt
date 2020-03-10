package com.salesmotoris.activity.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.salesmotoris.R
import com.salesmotoris.activity.home.HomeActivity
import com.salesmotoris.model.Login
import com.salesmotoris.mvp.BaseMvpActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.toast

class LoginActivity : BaseMvpActivity<LoginContract.View, LoginContract.Presenter>(), LoginContract.View {

    override var mPresenter: LoginContract.Presenter = LoginPresenter()

    override fun showResponseMessage(response: Login.LoginResponse) {
        progress_login.visibility = View.GONE
        button_login.visibility = View.VISIBLE

        if (response.meta.code == 200){
            toast(response.meta.message)

            val data: MutableMap<String, String> = mutableMapOf()
            data["id"] = response.data.id.toString()
            data["username"] = response.data.username
            data["email"] = response.data.email
            mPresenter.saveUserData(response.data.api_token, data)
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }

    override fun hideProgress() {
        progress_login.visibility = View.GONE
        button_login.visibility = View.VISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Picasso.get().load(R.drawable.img_logo_white).into(imageview_login_logo)
        button_login.setOnClickListener {
            when {
                TextUtils.isEmpty(edittext_login_username.text.toString()) -> {
                    edittext_login_username.error = "Username must not be empty"
                }
                TextUtils.isEmpty(edittext_login_password.text.toString()) -> {
                    edittext_login_password.error = "Password must not be empty"
                }
                else -> {
                    edittext_login_username.error = null
                    edittext_login_password.error = null

                    progress_login.visibility = View.VISIBLE
                    button_login.visibility = View.GONE

                    mPresenter.submitLoginData(edittext_login_username.text.toString(), edittext_login_password.text.toString())
                }
            }
        }
    }
}
