#
# Build library bilaries for all supported architectures
#

APP_ABI := all
APP_OPTIM := release
APP_STL := c++_static
APP_CPPFLAGS := -fexceptions # -D SOUNDTOUCH_DISABLE_X86_OPTIMIZATIONS
APP_PLATFORM := android-16
APP_ALLOW_MISSING_DEPS=true

