package com.leopard.network.view

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialog
import com.leopard.network.R

class LoadingDialog : AppCompatDialog {

    constructor(context: Context?) : this(context, R.style.LoadingDialog)

    constructor(context: Context?, theme: Int) : super(context, theme)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sdk_network_loading_dialog)
    }


}