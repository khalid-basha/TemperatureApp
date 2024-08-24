#include <jni.h>
#include <cstdlib>
#include <ctime>

extern "C" JNIEXPORT jfloat JNICALL
Java_com_example_temperatureapp_SensorViewModel_getTemperature(JNIEnv* env, jobject /* this */) {
    std::srand(std::time(nullptr));
    float temp = 20.0f + static_cast<float>(std::rand()) / RAND_MAX * 20.0f;

    return temp;
}
