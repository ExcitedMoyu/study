
#include "native_lib.h"

const char *PATH = "/data/data/com.smasher.rejuvenation/my.sock";
const char *userId;
int m_child;


//创建服务端socket，并实现监听
extern "C"
JNIEXPORT void JNICALL
Java_com_smasher_ndk_Watcher_createWatcher(JNIEnv *env, jobject instance, jstring userId_) {

    userId = env->GetStringUTFChars(userId_, 0);
    // 开双进程
    pid_t pid = fork();
    if (pid < 0) {
        LOGE("fork 进程失败");
    } else if (pid == 0) {
        //子进程
        child_do_work();
    } else {
        //父进程
        //donothing
    }

    env->ReleaseStringUTFChars(userId_, userId);
}


extern "C"
JNIEXPORT void JNICALL
Java_com_smasher_ndk_Watcher_connectMonitor(JNIEnv *env, jobject instance) {
    int socked;

    struct sockaddr_un addr;
    // 主进程
    while (1) {
        LOGI("客户端链接了。。。");
        socked = socket(AF_LOCAL, SOCK_STREAM, 0);
        if (socked < 0) {
            LOGE("链接失败");
            return;
        }

        //addr
        memset(&addr, 0, sizeof(sockaddr_un));
        addr.sun_family = AF_LOCAL;
        strcpy(addr.sun_path, PATH);

        int connect_result = connect(socked, (const sockaddr *) &addr, sizeof(sockaddr_un));
        if (connect_result < 0) {
            close(socked);
            sleep(1);
            //再来下一次尝试链接
            continue;
        }
        LOGI("链接成功");
        break;
    }
}


extern "C"
JNIEXPORT jstring JNICALL
Java_com_smasher_ndk_Watcher_stringFromJNI(JNIEnv *env, jobject instance) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}


void child_do_work() {
    //开启socket
    if (child_create_channel()) {
        //读取数据
        child_listen_msg();
    }
}


/**
 * 创建服务端读取信息
 * @return
 */
int child_create_channel() {
    //IP端口=文件
    int listenfd = socket(AF_LOCAL, SOCK_STREAM, 0);

    int connfd = 0;

    unlink(PATH);
    //addr
    struct sockaddr_un addr;
    memset(&addr, 0, sizeof(sockaddr_un));
    addr.sun_family = AF_LOCAL;
    //addr.sun_data = PATH; 不能够直接赋值,所以使用内存拷贝的方式赋值
    strcpy(addr.sun_path, PATH);

    //绑定
    int result = bind(listenfd, (const sockaddr *) (&addr), sizeof(sockaddr_un));

    if (result < 0) {
        LOGE("绑定错误");
        return 0;
    }

    listen(listenfd, 5);

    //保证宿主进程链接成功
    while (1) {
        //返回客户单的地址 阻塞式函数
        connfd = accept(listenfd, NULL, NULL);
        if (connfd < 0) {
            if (errno == EINTR) {
                continue;
            } else {
                LOGE("读取错误");
                return 0;
            }
        }
        m_child = connfd;
        LOGI("链接成功");
        break;
    }
    return 1;
}


/**
 * 创建服务端的socket
 */
void child_listen_msg() {

    fd_set fdSet;
    struct timeval timeout = {3, 0};
    while (1) {
        //清空内容
        FD_ZERO(&fdSet);
        //重新設置
        FD_SET(m_child, &fdSet);
        //选择监听 监视文件数  一般+1
        int r = select(m_child + 1, &fdSet, NULL, NULL, &timeout);


        if (r > 0) {
            //缓冲区
            char pkg[256] = {0};

            //保证所读信息为指定客户端
            if (FD_ISSET(m_child, &fdSet)) {

                LOGI("读取消息前 %d", r);

                //阻塞式函数 读取nothing
                int result = read(m_child, pkg, sizeof(pkg));

                LOGE("重新開啓服務");
                //开启服务
                execlp("am", "am", "startservice", "--user", userId,
                       "com.smasher.ndk/com.smasher.ndk.PrimaryService", (char *) NULL);

                break;
            }
        }
    }
}



