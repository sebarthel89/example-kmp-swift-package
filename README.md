# Kotlin multiplatform project example with swift package manager deployment

## Build targets

```
./gradlew clean build
```

Afterwards, IOS frameworks are located in:

- `./build/bin/iosArm64`
- `./build/bin/iosX64`

To build a fat framework you can:
```
universalFrameworkRelease
```

Afterwards, the fat (simulator + ios) framework is located in:

- `./build/bin/universal`


Build a swift package:

```
./gradlew createXCFramework
```
