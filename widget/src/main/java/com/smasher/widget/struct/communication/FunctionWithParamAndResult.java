package com.smasher.widget.struct.communication;

/**
 * 有参数有返回值的通信接口
 *
 * @author matao
 * @date 2019/3/25
 */
public abstract class FunctionWithParamAndResult<Param, Result> extends Function {
    public FunctionWithParamAndResult(String functionName) {
        super(functionName);
    }

    /**
     * 通信方法
     *
     * @param param param参数
     * @return Result返回值
     */
    public abstract Result function(Param param);
}
