package com.smasher.widget.struct.communication;

/**
 * 通信接口抽象类
 *
 * @author matao
 * @date 2019/3/25
 */
public abstract class Function {

    public String mFunctionName;

    public Function(String functionName) {
        mFunctionName = functionName;
    }
}
