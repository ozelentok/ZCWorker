cmake_minimum_required(VERSION 3.22)
project(zcjni)
set(CMAKE_CXX_STANDARD 23)
set(CMAKE_INTERPROCEDURAL_OPTIMIZATION TRUE)

# Required for std::jthread support in LLVM libc++
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -fexperimental-library")

option(WITH_COMMANDER "Include Commander Library"           OFF)
option(WITH_ZCFS      "Include ZCFS Executable"             OFF)
option(WITH_WORKER    "Include Worker Library"              ON)
option(WITH_ZCWORKERD "Include ZCWorker Daemon Executable"  OFF)
add_subdirectory(ZControl)

add_library(zcjni SHARED
	zcjni.cpp
)
target_include_directories(zcjni PUBLIC ${CMAKE_CURRENT_SOURCE_DIR})
target_link_libraries(zcjni PRIVATE worker)
