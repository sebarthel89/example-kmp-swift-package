# Kotlin multiplatform project example with swift package manager deployment

## Build targets

```
./gradlew clean build
```

Afterwards, IOS frameworks are located in:

- `./build/bin/`

Build a swift package:

```
./gradlew createXCFramework
```
