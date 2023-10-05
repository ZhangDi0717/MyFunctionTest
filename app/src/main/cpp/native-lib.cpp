#include <jni.h>
#include <string>
#include "code_jni_helpers.h"
#define TAG "MY_LOG"
#define ALOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, TAG, __VA_ARGS__)
#define ALOGD(...) __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__)
#define ALOGI(...) __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)
#define ALOGW(...) __android_log_print(ANDROID_LOG_WARN, TAG, __VA_ARGS__)
#define ALOGE1(...) __android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__)
struct SensorOffsets
{
    jclass      clazz;
    //fields
    jfieldID    name;
    jfieldID    vendor;
    jfieldID    version;
    jfieldID    handle;
    jfieldID    range;
    jfieldID    resolution;
    jfieldID    power;
    jfieldID    minDelay;
    jfieldID    fifoReservedEventCount;
    jfieldID    fifoMaxEventCount;
    jfieldID    stringType;
    jfieldID    requiredPermission;
    jfieldID    maxDelay;
    jfieldID    flags;
    //methods
    jmethodID   setType;
    jmethodID   setUuid;
    jmethodID   init;
} gSensorOffsets;


extern "C"
JNIEXPORT jstring JNICALL
Java_com_zhangdi_myfunctiontest_jni_JNIActivity_stringFromJNI(JNIEnv *env, jobject thiz) {
    // TODO: implement stringFromJNI()
    //LOG_ALWAYS_FATAL_IF(0, "Unable to find static field %s", "field_name");
    std::string hello = "Hello from C++";
    ALOGD("<Line: %d> jni_init", __LINE__);
    return env->NewStringUTF(hello.c_str());
}
extern "C"
JNIEXPORT void JNICALL
Java_com_zhangdi_myfunctiontest_jni_JNIActivity_mappingClass(JNIEnv *_env, jobject thiz) {
    // TODO: implement mappingClass()
    ALOGD("<Line: %d> %s", __LINE__, __FUNCTION__);
    //android.hardware.Sensor
    SensorOffsets& sensorOffsets = gSensorOffsets;
    jclass sensorClass = (jclass)
            MakeGlobalRefOrDie(_env, FindClassOrDie(_env, "com/zhangdi/myfunctiontest/jni/Bean"));
    sensorOffsets.clazz = sensorClass;
    sensorOffsets.name = GetFieldIDOrDie(_env, sensorClass, "mName", "Ljava/lang/String;");
    sensorOffsets.vendor = GetFieldIDOrDie(_env, sensorClass, "mVendor", "Ljava/lang/String;");
    sensorOffsets.version = GetFieldIDOrDie(_env, sensorClass, "mVersion", "I");
    sensorOffsets.handle = GetFieldIDOrDie(_env, sensorClass, "mHandle", "I");
    sensorOffsets.range = GetFieldIDOrDie(_env, sensorClass, "mMaxRange", "F");
    sensorOffsets.resolution = GetFieldIDOrDie(_env, sensorClass, "mResolution","F");
    sensorOffsets.power = GetFieldIDOrDie(_env, sensorClass, "mPower", "F");
    sensorOffsets.minDelay = GetFieldIDOrDie(_env, sensorClass, "mMinDelay", "I");
    sensorOffsets.fifoReservedEventCount =
            GetFieldIDOrDie(_env,sensorClass, "mFifoReservedEventCount", "I");
    sensorOffsets.fifoMaxEventCount = GetFieldIDOrDie(_env,sensorClass, "mFifoMaxEventCount", "I");
    sensorOffsets.stringType =
            GetFieldIDOrDie(_env,sensorClass, "mStringType", "Ljava/lang/String;");
    sensorOffsets.requiredPermission =
            GetFieldIDOrDie(_env,sensorClass, "mRequiredPermission", "Ljava/lang/String;");
    sensorOffsets.maxDelay = GetFieldIDOrDie(_env,sensorClass, "mMaxDelay", "I");
    sensorOffsets.flags = GetFieldIDOrDie(_env,sensorClass, "mFlags", "I");

}
//
//static jobject
//translateNativeSensorToJavaSensor(JNIEnv *env, jobject sensor, const Sensor& nativeSensor) {
//    const SensorOffsets& sensorOffsets(gSensorOffsets);
//
//    if (sensor == NULL) {
//        // Sensor sensor = new Sensor();
//        sensor = env->NewObject(sensorOffsets.clazz, sensorOffsets.init, "");
//    }
//
//    if (sensor != NULL) {
//        jstring name = getJavaInternedString(env, nativeSensor.getName());
//        jstring vendor = getJavaInternedString(env, nativeSensor.getVendor());
//        jstring requiredPermission =
//                getJavaInternedString(env, nativeSensor.getRequiredPermission());
//
//        env->SetObjectField(sensor, sensorOffsets.name,      name);
//        env->SetObjectField(sensor, sensorOffsets.vendor,    vendor);
//        env->SetIntField(sensor, sensorOffsets.version,      nativeSensor.getVersion());
//        env->SetIntField(sensor, sensorOffsets.handle,       nativeSensor.getHandle());
//        env->SetFloatField(sensor, sensorOffsets.range,      nativeSensor.getMaxValue());
//        env->SetFloatField(sensor, sensorOffsets.resolution, nativeSensor.getResolution());
//        env->SetFloatField(sensor, sensorOffsets.power,      nativeSensor.getPowerUsage());
//        env->SetIntField(sensor, sensorOffsets.minDelay,     nativeSensor.getMinDelay());
//        env->SetIntField(sensor, sensorOffsets.fifoReservedEventCount,
//                         nativeSensor.getFifoReservedEventCount());
//        env->SetIntField(sensor, sensorOffsets.fifoMaxEventCount,
//                         nativeSensor.getFifoMaxEventCount());
//        env->SetObjectField(sensor, sensorOffsets.requiredPermission,
//                            requiredPermission);
//        env->SetIntField(sensor, sensorOffsets.maxDelay, nativeSensor.getMaxDelay());
//        env->SetIntField(sensor, sensorOffsets.flags, nativeSensor.getFlags());
//
//        if (env->CallBooleanMethod(sensor, sensorOffsets.setType, nativeSensor.getType())
//            == JNI_FALSE) {
//            jstring stringType = getJavaInternedString(env, nativeSensor.getStringType());
//            env->SetObjectField(sensor, sensorOffsets.stringType, stringType);
//        }
//
//        // TODO(b/29547335): Rename "setUuid" method to "setId".
//        int64_t id = nativeSensor.getId();
//        env->CallVoidMethod(sensor, sensorOffsets.setUuid, id, 0);
//    }
//    return sensor;
//}


extern "C"
JNIEXPORT void JNICALL
Java_com_zhangdi_myfunctiontest_jni_JNIActivity_logTest(JNIEnv *env, jobject thiz) {
    // TODO: implement logTest()
    ALOGD("Unable to find static field %s", __func__);
}