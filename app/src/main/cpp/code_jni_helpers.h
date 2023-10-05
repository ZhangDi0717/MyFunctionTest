//
// Created by zhangdi45 on 2023/9/11.
//

#ifndef MYFUNCTIONTEST_CODE_JNI_HELPERS_H
#define MYFUNCTIONTEST_CODE_JNI_HELPERS_H
#include <jni.h>
#include "log.h"
//#include <android_runtime/AndroidRuntime.h>
static inline jclass FindClassOrDie(JNIEnv* env, const char* class_name) {
    jclass clazz = env->FindClass(class_name);
    if(CONDITION(clazz == NULL)){
        LOGE( "Unable to find class %s", class_name);
    }
    return clazz;
}

static inline jfieldID GetFieldIDOrDie(JNIEnv* env, jclass clazz, const char* field_name,
                                       const char* field_signature) {
    jfieldID res = env->GetFieldID(clazz, field_name, field_signature);
    if(CONDITION(res == NULL)){
        LOGE( "Unable to find class %s", res);
    }
    return res;
}

static inline jmethodID GetMethodIDOrDie(JNIEnv* env, jclass clazz, const char* method_name,
                                         const char* method_signature) {
    jmethodID res = env->GetMethodID(clazz, method_name, method_signature);
    if(CONDITION(res == NULL)){
        LOGE( "Unable to find class %s", res);
    }
    return res;
}
static inline jfieldID GetStaticFieldIDOrDie(JNIEnv* env, jclass clazz, const char* field_name,
                                             const char* field_signature) {
    jfieldID res = env->GetStaticFieldID(clazz, field_name, field_signature);
    if(CONDITION(res == NULL)){
        LOGE( "Unable to find class %s", res);
    }
    return res;
}

static inline jmethodID GetStaticMethodIDOrDie(JNIEnv* env, jclass clazz, const char* method_name,
                                               const char* method_signature) {
    jmethodID res = env->GetStaticMethodID(clazz, method_name, method_signature);
    if(CONDITION(res == NULL)){
        LOGE( "Unable to find class %s", res);
    }
    return res;
}

template <typename T>
static inline T MakeGlobalRefOrDie(JNIEnv* env, T in) {
    jobject res = env->NewGlobalRef(in);
    if(CONDITION(res == NULL)){
        LOGE( "Unable to find class %s", res);
    }
    return static_cast<T>(res);
}

//static inline int RegisterMethodsOrDie(JNIEnv* env, const char* className,
//                                       const JNINativeMethod* gMethods, int numMethods) {
//    int res = AndroidRuntime::registerNativeMethods(env, className, gMethods, numMethods);
//    LOG_ALWAYS_FATAL_IF(res < 0, "Unable to register native methods.");
//    return res;
//}
//
///**
// * Read the specified field from jobject, and convert to std::string.
// * If the field cannot be obtained, return defaultValue.
// */
//static inline std::string getStringField(JNIEnv* env, jobject obj, jfieldID fieldId,
//                                         const char* defaultValue) {
//    ScopedLocalRef<jstring> strObj(env, jstring(env->GetObjectField(obj, fieldId)));
//    if (strObj != nullptr) {
//        ScopedUtfChars chars(env, strObj.get());
//        return std::string(chars.c_str());
//    }
//    return std::string(defaultValue);
//}


#endif //MYFUNCTIONTEST_CODE_JNI_HELPERS_H
