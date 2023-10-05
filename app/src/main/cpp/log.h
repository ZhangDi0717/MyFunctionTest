//
// Created by zhangdi45 on 2023/9/11.
//

#ifndef MYFUNCTIONTEST_LOG_H
#define MYFUNCTIONTEST_LOG_H

#include <android/log.h>

#define CONDITION(cond)     (__builtin_expect((cond)!=0, 0))

#ifndef LOG_TAG
#define LOG_TAG "RasterMill"
#endif


#define LOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG, __VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)



#ifndef LOG_ALWAYS_FATAL_IF
#define LOG_ALWAYS_FATAL_IF(cond, ...) \
    ( (CONDITION(cond)) \
    ? ((void)LOGV(#cond, ## __VA_ARGS__)) \
    : (void)0 )
#endif

#endif //MYFUNCTIONTEST_LOG_H
