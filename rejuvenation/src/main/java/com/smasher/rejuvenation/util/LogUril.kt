package com.smasher.rejuvenation.util

import android.util.Log
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * 日志工具类
 * @author sunbufu
 */
class LogUtil {


    private val TOP_LINE = "" +
            "\n^^^^^^^^^^^^^less code,less bug^^^^^^^^^^^^^^\n" +
            "                   _ooOoo_\n" +
            "                  o8888888o\n" +
            "                  88\" . \"88\n" +
            "                  (| -_- |)\n" +
            "                  O\\  =  /O\n" +
            "               ____/`---'\\____\n" +
            "             .'  \\\\|     |//  `.\n" +
            "            /  \\\\|||  :  |||//  \\\n" +
            "           /  _||||| -:- |||||-  \\\n" +
            "           |   | \\\\\\  -  /// |   |\n" +
            "           | \\_|  ''\\---/''  |   |\n" +
            "           \\  .-\\__  `-`  ___/-. /\n" +
            "         ___`. .'  /--.--\\  `. . __\n" +
            "      .\"\" '<  `.___\\_<|>_/___.'  >'\"\".\n" +
            "     | | :  `- \\`.;`\\ _ /`;.`/ - ` : | |\n" +
            "     \\  \\ `-.   \\_ __\\ /__ _/   .-` /  /\n" +
            "======`-.____`-.___\\_____/___.-`____.-'======\n" +
            "                   `=---='\n" +
            "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n" +
            "            佛祖保佑       永无BUG\n" +
            "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n"

    private val TOP_BORDER = "╔══════════════════════════════════════════════════════════════════════════════════════════════════════════"
    private val LEFT_BORDER = "║ "
    private val BOTTOM_BORDER = "╚══════════════════════════════════════════════════════════════════════════════════════════════════════════"
    private var debug: Boolean = true//是否打印log
    private var savesd: Boolean = false//是否存log到sd卡
    private val CHUNK_SIZE = 106 //设置字节数
    private var logDir = ""//设置文件存储目录
    private var logSize = 2 * 1024 * 1024L//设置log文件大小 k
    private val execu: ExecutorService = Executors.newFixedThreadPool(1)


    companion object {

        /**Log的前缀*/
        var tagPrefix: String = "LogUtil"

        /**日志是否打印的标识*/
        val flag: Boolean = true;

        fun d(a: Any) = log("d", a)
        fun i(a: Any) = log("i", a)
        fun w(a: Any) = log("w", a)
        fun e(a: Any) = log("e", a)

        /**
         * @param type   级别
         * @param any   打印的对象
         */
        private fun log(type: String, any: Any) {
            if (!flag) return
            var msg = any.toString()
            var tag = getTag(getCallerStackTraceElement())
            when (type) {
                "d" -> Log.d(tag, msg)
                "i" -> Log.i(tag, msg)
                "w" -> Log.w(tag, msg)
                "e" -> Log.e(tag, msg)
            }
        }

        /**生成TAG*/
        private fun getTag(element: StackTraceElement): String {
            //获取类名（去掉包名）
            var callerClazzName: String = element.className
            callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1)
            //生成TAG
            return "$tagPrefix:$callerClazzName.${element.methodName}(${element.lineNumber})"
        }

        /**获取函数堆栈*/
        private fun getCallerStackTraceElement(): StackTraceElement = Thread.currentThread().getStackTrace()[5]
    }

}