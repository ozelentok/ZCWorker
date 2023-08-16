# ZCWorker
Android worker for [ZControl](https://github.com/ozelentok/ZControl)

- Access an Android device's filesystem remotely on a Linux machine using FUSE
- Only Android 11+ (API 30) is supported as MANAGE_EXTERNAL_STORAGE is required to freely access the internal storage and SD card

## Building
- Compile using Android Studio

## Running
- After installing the APK, configure the remote host and port to connect to
- Make sure the ZCFS FUSE module on the host is mounted
- Press "Start" to connect
- A WakeLock is activate while the worker is connected to ensure fast speeds
- The device will be available at
```bash
ZCFS_MOUNT_POINT/DEVICE_IP
```
- Unfortunately the root of the device is un-indexable as normal apps do not have permission
- To access the internal storage / SD cards, enter a specific path
```bash
# Internal Storage
ZCFS_MOUNT_POINT/DEVICE_IP/storage/emulated/0

# SD Card (Replace the placeholder with your SD storage ID)
ZCFS_MOUNT_POINT/DEVICE_IP/storage/AAAA-BBBB
```
