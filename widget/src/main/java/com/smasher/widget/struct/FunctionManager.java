package com.smasher.widget.struct;

import android.text.TextUtils;


import com.smasher.widget.struct.communication.FunctionNoParamAndResult;
import com.smasher.widget.struct.communication.FunctionWithParamAndResult;
import com.smasher.widget.struct.communication.FunctionWithParamOnly;
import com.smasher.widget.struct.communication.FunctionWithResultOnly;

import java.util.HashMap;

/**
 * fragment与activity通信接口管理类
 *
 * @author matao
 */
public class FunctionManager {

    private HashMap<String, FunctionNoParamAndResult> mFunctionNoParamAndResults;
    private HashMap<String, FunctionWithParamAndResult> mFunctionWithParamAndResult;
    private HashMap<String, FunctionWithParamOnly> mFunctionWithParamOnlys;
    private HashMap<String, FunctionWithResultOnly> mFunctionWithResultOnlys;

    private FunctionManager() {
        mFunctionNoParamAndResults = new HashMap<>();
        mFunctionWithParamAndResult = new HashMap<>();
        mFunctionWithParamOnlys = new HashMap<>();
        mFunctionWithResultOnlys = new HashMap<>();
    }

    private static class SingletonInstance {
        private static final FunctionManager INSTANCE = new FunctionManager();
    }

    public static FunctionManager getInstance() {
        return SingletonInstance.INSTANCE;
    }


    /**
     * 无参无返回值通信接口添加
     *
     * @param function function通信接口{@link FunctionNoParamAndResult}
     * @return #FunctionManager
     */
    public FunctionManager add(FunctionNoParamAndResult function) {
        mFunctionNoParamAndResults.put(function.mFunctionName, function);
        return this;
    }

    /**
     * 无参数无返回值接口invoke
     *
     * @param functionName functionName接口名称
     * @throws FunctionException 通信接口异常
     */
    public void invokeFunction(String functionName) throws FunctionException {
        if (TextUtils.isEmpty(functionName)) {
            return;
        }

        FunctionNoParamAndResult function = null;
        if (mFunctionNoParamAndResults != null) {
            function = mFunctionNoParamAndResults.get(functionName);
            if (function != null) {
                function.function();
            }
        }

        if (function == null) {
            throw new FunctionException("no this function: " + functionName);
        }
    }

    /**
     * 有参数有返回值通信接口添加
     *
     * @param function function通信接口{@link FunctionWithParamAndResult}
     * @return #FunctionManager
     */
    public FunctionManager add(FunctionWithParamAndResult function) {
        mFunctionWithParamAndResult.put(function.mFunctionName, function);
        return this;
    }


    /**
     * 有参数有返回值接口invoke
     *
     * @param functionName 通信接口名称
     * @param c            返回值class
     * @param param        参数
     * @param <Param>      参数泛型
     * @param <Result>     返回值泛型
     * @return Result
     * @throws FunctionException 通信接口异常
     */
    public <Param, Result> Result invokeFunction(String functionName, Class<Result> c, Param param) throws FunctionException {
        if (TextUtils.isEmpty(functionName)) {
            return null;
        }

        FunctionWithParamAndResult function = null;
        if (mFunctionWithParamAndResult != null) {
            function = mFunctionWithParamAndResult.get(functionName);
            if (function != null) {
                if (c != null) {
                    return c.cast(function.function(param));
                } else {
                    return (Result) function.function(param);
                }

            }
        }
        throw new FunctionException("no this function: " + functionName);
    }


    /**
     * 只有参数的通信接口添加
     *
     * @param function function通信接口{@link FunctionWithParamOnly}
     * @return #FunctionManager
     */
    public FunctionManager add(FunctionWithParamOnly function) {
        mFunctionWithParamOnlys.put(function.mFunctionName, function);
        return this;
    }


    /**
     * 有参数无返回值接口invoke
     *
     * @param functionName 通信接口名称
     * @param param        参数
     * @param <Param>      参数泛型
     * @throws FunctionException 通信接口异常
     */
    public <Param> void invokeFunction(String functionName, Param param) throws FunctionException {
        if (TextUtils.isEmpty(functionName)) {
            return;
        }
        FunctionWithParamOnly function = null;
        if (mFunctionWithParamOnlys != null) {
            function = mFunctionWithParamOnlys.get(functionName);
            if (function != null) {
                function.function(param);
            }
        }

        if (function == null) {
            throw new FunctionException("no this function: " + functionName);
        }
    }


    /**
     * 只有返回值的通信接口添加
     *
     * @return #FunctionManager
     */
    public FunctionManager add(FunctionWithResultOnly function) {
        mFunctionWithResultOnlys.put(function.mFunctionName, function);
        return this;
    }


    /**
     * 无参数有返回值接口invoke
     *
     * @param functionName 通信接口名称
     * @param c            返回值class
     * @param <Result>     返回值泛型
     * @return Result
     * @throws FunctionException 通信接口异常
     */
    public <Result> Result invokeFunction(String functionName, Class<Result> c) throws FunctionException {
        if (TextUtils.isEmpty(functionName)) {
            return null;
        }
        FunctionWithResultOnly function = null;
        if (mFunctionWithResultOnlys != null) {
            function = mFunctionWithResultOnlys.get(functionName);
            if (function != null) {
                if (c != null) {
                    return c.cast(function.function());
                } else {
                    return (Result) function.function();
                }

            }
        }
        throw new FunctionException("no this function: " + functionName);
    }

}