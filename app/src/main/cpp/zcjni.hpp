#pragma once
#include <jni.h>
#ifdef __cplusplus
extern "C" {
#endif
JNIEXPORT void JNICALL Java_ozelentok_zcworker_MainActivity_connectWorker(JNIEnv *, jobject, jstring host, jint port);
JNIEXPORT void JNICALL Java_ozelentok_zcworker_MainActivity_waitOnWorker(JNIEnv *, jobject);
JNIEXPORT void JNICALL Java_ozelentok_zcworker_MainActivity_stopWorker(JNIEnv *, jobject);
#ifdef __cplusplus
}
#endif
