cmake_minimum_required(VERSION 3.22)
project(zcjni)

option(WITH_COMMANDER "Include Commander Library"           OFF)
option(WITH_ZCFS      "Include ZCFS Executable"             OFF)
option(WITH_WORKER    "Include Worker Library"              ON)
option(WITH_ZCWORKERD "Include ZCWorker Daemon Executable"  OFF)
add_subdirectory(ZControl)

add_library(zcjni SHARED
	zcjni.cpp
)
target_include_directories(zcjni PUBLIC ${CMAKE_CURRENT_SOURCE_DIR})
target_link_libraries(zcjni PRIVATE android log base common worker)
