//
// Created by moyu on 2019/6/5.
//

#ifndef STUDY_NATIVE_LIB_H
#define STUDY_NATIVE_LIB_H


#include <string>
#include <jni.h>
#include <unistd.h>
#include <pthread.h>
#include <signal.h>
#include <string.h>
#include <stdlib.h>
#include <errno.h>
#include <sys/select.h>
#include <sys/wait.h>
#include <sys/types.h>
#include <linux/signal.h>
#include <sys/socket.h>
#include <sys/un.h>
#include <android/log.h>

#define  LOG_TAG "tuch"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)


void child_do_work();

int child_create_channel();

void child_listen_msg();

#endif //STUDY_NATIVE_LIB_H
