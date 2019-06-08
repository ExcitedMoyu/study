package com.smasher.widget.struct.communication;

/**
 * 只有参数的通信接口
 *
 * @author matao
 * @date 2019/3/25
 */
public abstract class FunctionWithParamOnly<Param> extends Function {

    public FunctionWithParamOnly(String functionName) {
        super(functionName);
    }


    /**
     * 通信方法
     *
     * @param param param参数
     */
    public abstract void function(Param param);
}
