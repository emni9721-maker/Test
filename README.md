# ZERO - Encrypted Social & Cloud Vault (Android)

ZERO is a native Android application built with Kotlin and Jetpack Compose, implementing zero-knowledge encrypted cloud storage and privacy-focused social networking.

## Features

- **Encrypted Social Feed**: Interactive updates, posts, comments, likes, and stories viewer.
- **Media & Photos Vault**: High-resolution and RAW photo/video storage with instant AES-256 GCM encryption.
- **PIN-Protected Folders**: Secure album locks requiring 4-digit PIN verification to decrypt and view sensitive files.
- **Direct Encrypted Messaging**: Peer-to-peer style conversational messaging interface.
- **Dual Display Theming**: Native support for **Midnight** (deep navy charcoal with emerald accents) and **AMOLED** (pure black with vibrant neon accents).
- **Storage & Backup Management**: Real-time quota tracking, Wi-Fi only background synchronization, and zero compression mode.

## Architecture

- **UI & Layout**: 100% Jetpack Compose using Material Design 3 (M3) components, adaptive layouts, and custom theme tokens.
- **State Management**: Reactive MVVM architecture with Kotlin Coroutines and `StateFlow`.
- **Image Loading**: Asynchronous image caching and rendering powered by Coil Compose.
- **Target SDK**: Android 35 (minSdk 26).
