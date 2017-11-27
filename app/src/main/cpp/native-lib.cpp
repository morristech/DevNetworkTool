#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring

JNICALL
Java_app_deadmc_devnetworktool_helpers_SystemHelper_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
