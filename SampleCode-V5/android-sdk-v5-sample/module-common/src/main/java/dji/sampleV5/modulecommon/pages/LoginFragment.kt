package dji.sampleV5.modulecommon.pages

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dji.sampleV5.modulecommon.R
import dji.sampleV5.modulecommon.models.LoginVM
import dji.sampleV5.modulecommon.util.ToastHelper.showToast
import dji.sampleV5.modulecommon.util.ToastUtils
import dji.v5.utils.common.LogUtils
import kotlinx.android.synthetic.main.frag_login_account_page.*

/**
 * Description :
 *
 * @author: Byte.Cai
 *  date : 2022/4/24
 *
 * Copyright (c) 2022, DJI All Rights Reserved.
 */
class LoginFragment : DJIFragment() {
    private val loginVM: LoginVM by viewModels()
    private val TAG = LogUtils.getTag("LoginFragment")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_login_account_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }

    private fun initListener() {
        loginVM.addLoginStateChangeListener {
            activity?.runOnUiThread {
                it?.run {
                    tv_login_account_info.text = if (TextUtils.isEmpty(account)) {
                        "UNKNOWN"
                    } else {
                        account
                    }
                    tv_login_state_info.text = loginState.name
                }
            }

        }
        btn_login.setOnClickListener {
            loginVM.loginAccount(requireActivity())
        }
        btn_logout.setOnClickListener {
            loginVM.logoutAccount()
        }
        btn_get_login_info.setOnClickListener {
            //将获取到的结果刷新到UI
            val loginInfo = loginVM.getLoginInfo()
            tv_login_account_info.text = if (TextUtils.isEmpty(loginInfo.account)) {
                "UNKNOWN"
            } else {
                loginInfo.account
            }
            tv_login_state_info.text = loginInfo.loginState.name
            //同时将获取到的结果Toast出来
            ToastUtils.showToast(tv_login_account_info.text.toString() + "-" + loginInfo.loginState.name)

        }
    }

    private fun initView() {
        loginVM.loginLD.observe(viewLifecycleOwner,
            { t ->
                t?.run {
                    isSuccess.showToast("Login Success!", msg, tv_login_error_info,true)

                }
            })

        loginVM.logoutLD.observe(viewLifecycleOwner, {
            it.isSuccess.showToast("Logout Success！", it.msg, tv_login_error_info,true)
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        loginVM.clearAllLoginStateChangeListener()
    }
}