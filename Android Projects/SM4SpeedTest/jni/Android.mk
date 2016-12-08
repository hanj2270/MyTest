LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := SM4byC
LOCAL_SRC_FILES := SM4byC.cpp

include $(BUILD_SHARED_LIBRARY)
