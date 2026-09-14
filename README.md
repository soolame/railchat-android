# RailChat

**Offline chat for train passengers.** No internet, no SIM signal, no accounts, no signup. Open the app on a train and talk to other passengers in your coach or anywhere on the train — messages hop phone to phone over Bluetooth mesh, because there is no server.

Working name, still early — built for and tested during development, not yet field-tested on a real train.

## This is a fork of bitchat

RailChat is a fork of [permissionlesstech/bitchat-android](https://github.com/permissionlesstech/bitchat-android), and it runs on bitchat's engine largely unmodified. Everything about how messages actually get from one phone to another — Bluetooth mesh discovery, multi-hop relay, Noise Protocol end-to-end encryption, the binary packet format, peer identity — is bitchat's code, untouched.

What RailChat adds on top:

- An onboarding step to optionally record your train number and coach
- Two auto-generated chat rooms derived from that (a whole-train room and a coach-only room), using bitchat's existing channel system
- A distinct name, launcher icon, and color theme, so it's visually its own thing
- New GATT UUIDs and application ID, so installs don't join the live public bitchat mesh during testing

Everything else — private messaging, manually-created group channels, Wi-Fi Aware transport, Nostr/geohash location channels, Tor support, emergency wipe — is inherited as-is from bitchat. See the [upstream README](https://github.com/permissionlesstech/bitchat-android#readme) and [`AGENTS.md`](AGENTS.md)/[`docs/`](docs/) for how the underlying mesh and protocol actually work; this fork hasn't changed any of that.

## Get a build

Pre-built debug APKs are published automatically on every push, no build tools required:

**[Latest debug build →](https://github.com/soolame/railchat-android/releases/tag/latest-debug)**

Not signed for release and not on an app store — you'll need to allow "install from unknown sources" to install it. Pick the APK matching your phone (`arm64-v8a` covers almost all modern Android phones), or grab `app-universal-debug.apk` if unsure.

## See it in action

<table>
  <tr>
    <th>Offline mesh conversation</th>
    <th>Geohash globe picker</th>
  </tr>
  <tr>
    <td><img src="docs/screenshots/readme-mesh-chat.png" alt="Active four-peer mesh conversation with an image, voice messages, and text messages" width="360"/></td>
    <td><img src="docs/screenshots/readme-geohash-globe.png" alt="Geohash location picker showing the whole Earth and geohash grid" width="360"/></td>
  </tr>
</table>

These screenshots are inherited from upstream bitchat and predate RailChat's rebrand — the color theme and launcher icon shown are not current. New screenshots are pending.

## License

GNU General Public License v3.0, inherited from upstream. See [LICENSE.md](LICENSE.md).

## Features

Inherited from bitchat, unmodified:

- **Decentralized Mesh Network**: Automatic peer discovery and multi-hop relay over Bluetooth LE (max 7 hops)
- **End-to-End Encryption**: [Noise Protocol](https://noiseprotocol.org) (XX pattern, X25519 + ChaCha20-Poly1305) for private messages over the mesh
- **Channel Chats**: Topic-based group messaging with optional password protection (Argon2id + AES-256-GCM)
- **IRC-Style Commands**: Familiar `/join`, `/msg`, `/who` style interface
- **Wi-Fi Aware Transport**: Higher-bandwidth local mesh on supported devices
- **Location-Based Channels**: Geographic chat rooms using geohash coordinates over Nostr relays (needs internet)
- **Intelligent Message Routing**: Automatically chooses the best transport, with queuing and retry when a peer is unreachable
- **Tor Support**: Built-in Tor (Arti) for private internet connectivity
- **Emergency Wipe**: Triple-tap to instantly clear all data

Added by RailChat:

- **Train & coach rooms**: optionally record your train number and coach during onboarding; a `t/<train>` room and a `t/<train>/<coach>` room appear automatically for anyone who entered the same values, using bitchat's existing channel mechanism — no manual `/join` needed
- **Isolated mesh**: distinct GATT service/characteristic UUIDs and application ID, so test installs don't join the live public bitchat network

## Technical Architecture

### Bluetooth Mesh Network (Offline)

- Direct peer-to-peer within Bluetooth range, multi-hop relay through nearby devices
- Noise Protocol sessions with forward secrecy; peer identities derived from static keys
- Compact binary packet format with fragmentation, TTL routing, and deduplication
- Adaptive duty cycling and connection limits for battery efficiency
- Foreground service keeps the mesh alive within Android background execution limits

### Nostr Protocol (Internet, optional)

- Global reach via public relays, geohash-based location channels
- Private messages fall back to Nostr for mutual favorites when the mesh is unavailable
- Ephemeral keys per geohash area

### Android Stack

- Kotlin, Jetpack Compose (Material 3), MVVM
- Coroutines and Flow for all networking and state
- Core components: `MeshForegroundService` (persistent connectivity), `BluetoothMeshService` / `WifiAwareMeshService` (transports), `UnifiedMeshService` (transport selection), `NoiseSessionManager` (encryption sessions), `MessageRouter` (mesh/Nostr routing with outbox retry)

## Building from source

Requires Android Studio and the Android SDK (API 26+).

```bash
git clone git@github.com:soolame/railchat-android.git
cd railchat-android
./gradlew assembleDebug
```

Install on a connected device:

```bash
adb install -r app/build/outputs/apk/debug/app-universal-debug.apk
```

The app requests Bluetooth, location (required for BLE scanning), and notification permissions at runtime.

## Testing

```bash
# Unit tests
./gradlew test

# Lint
./gradlew lint

# Instrumented tests (requires a device or emulator)
./gradlew connectedAndroidTest
```

Note that BLE mesh behavior is difficult to emulate; protocol and session logic is covered by unit tests, while radio-level behavior needs real devices (or, in a pinch, two emulators — modern Android Emulator builds include a working virtual Bluetooth stack that can discover and message between instances on the same machine).
