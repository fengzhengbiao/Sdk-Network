package com.leopard.network.entity

/**
 * Created by codeest on 2016/8/4.
 */

class ApiException : RuntimeException {

    var code: Int = 0

    constructor(msg: String) : super(msg) {}

    constructor(msg: String, codee: Int) : super(msg) {
        code = codee
    }
}
