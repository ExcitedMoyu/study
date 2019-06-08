package com.smasher.widget.struct.communication;

/**
 * 无参数无返回值的通信接口
 *
 * @author matao
 * @date 2019/3/25
 */
public abstract class FunctionNoParamAndResult extends Function {
    public FunctionNoParamAndResult(String functionName) {
        super(functionName);
    }

    /**
     * 通信方法
     */
    public abstract void function();
}
