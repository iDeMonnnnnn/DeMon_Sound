# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.10.2)

#-----------------------------------------
find_library( log-lib
        log )

set(lib_path ${CMAKE_SOURCE_DIR}/libs)
# 添加三方的so库
add_library(libfmod
        SHARED
        IMPORTED )

# 指名第三方库的绝对路径
set_target_properties( libfmod
        PROPERTIES IMPORTED_LOCATION
        ${lib_path}/${ANDROID_ABI}/libfmod.so )

add_library(libfmodL
        SHARED
        IMPORTED )

set_target_properties( libfmodL
        PROPERTIES IMPORTED_LOCATION
        ${lib_path}/${ANDROID_ABI}/libfmodL.so )

#--------------------------------
add_library( # Sets the name of the library.
        FmodSound
        # Sets the library as a shared library.
        SHARED
        # Provides a relative path to your source file(s).
        src/main/cpp/native-lib.cpp)

#---------------------
# 导入路径，为了让编译时能够寻找到这个文件夹
include_directories(src/main/cpp/inc)

# 链接好三个路径
target_link_libraries( FmodSound
        libfmod
        libfmodL
        ${log-lib} )