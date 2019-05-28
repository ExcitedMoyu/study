//
// Created by matao on 2019/5/28.
//
#include "com_smasher_ndk_Java2CJNI.h"

JNIEXPORT jstring JNICALL
Java_com_smasher_ndk_Java2CJNI_java2C(JNIEnv *env, jobject instance)
{
    return (*env)->NewStringUTF(env,"I am From Native C.");
}