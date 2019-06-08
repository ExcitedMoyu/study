package com.smasher.widget.struct.communication;

/**
 * 接口抽象类
 *
 * @author matao
 * @date 2019/3/25
 */
public abstract class FunctionWithResultOnly<Result> extends Function {


    public FunctionWithResultOnly(String functionName) {
        super(functionName);
    }


    /**
     * 通信方法
     *
     * @return Result
     */
    public abstract Result function();
}
