#include "zcjni.hpp"
#include "SysLog.hpp"
#include "Worker.hpp"
#include <thread>

static std::shared_ptr<Worker> worker_ptr;

void throw_java_exception(JNIEnv *env, const std::string &what) {
  jclass java_exception_class = env->FindClass("java/lang/Exception");
  env->ThrowNew(java_exception_class, what.c_str());
}

extern "C" JNIEXPORT void JNICALL Java_ozelentok_zcworker_MainActivity_connectWorker(JNIEnv *env, jobject, jstring host, jint port) {
  try {
    const char *host_c = env->GetStringUTFChars(host, NULL);
    std::string host_str = std::string(host_c);
    env->ReleaseStringUTFChars(host, host_c);
    worker_ptr = std::make_shared<Worker>(host_str, port);
  } catch (const std::exception &e) {
    throw_java_exception(env, std::string("Error on connecting worker: ") + e.what());
  } catch (...) {
    throw_java_exception(env, "Unknown Error on connecting worker");
  }
}

extern "C" JNIEXPORT void JNICALL Java_ozelentok_zcworker_MainActivity_work(JNIEnv *env, jobject) {
  if (worker_ptr) {
    worker_ptr->work();
  }
}

extern "C" JNIEXPORT void JNICALL Java_ozelentok_zcworker_MainActivity_stopWorker(JNIEnv *env, jobject) {
  try {
    if (worker_ptr) {
      worker_ptr->stop();
      worker_ptr = nullptr;
    }
  } catch (const std::exception &e) {
    throw_java_exception(env, std::string("Error on stopping worker: ") + e.what());
  } catch (...) {
    throw_java_exception(env, "Unknown on stopping worker");
  }
}
