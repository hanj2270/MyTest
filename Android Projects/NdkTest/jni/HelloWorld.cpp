#include <jni.h>
#include <string.h>
extern "C" {
JNIEXPORT jstring JNICALL Java_com_example_ndktest_MainActivity_HelloFromJNI(JNIEnv * env, jobject obj){
	return env->NewStringUTF("Hello World!This is JNI.");
}
}
