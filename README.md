# @noesis/intent-shim-capacitor

Capacitor port of com-darryncampbell-cordova-plugin-intent for OutSystems dual-stack. Android-only implementation of sendBroadcast, registerBroadcastReceiver, and unregisterBroadcastReceiver; iOS is a no-op bridge.

## API

<docgen-index>

* [`sendBroadcast(...)`](#sendbroadcast)
* [`registerBroadcastReceiver(...)`](#registerbroadcastreceiver)
* [`unregisterBroadcastReceiver()`](#unregisterbroadcastreceiver)
* [`addListener('broadcastReceived', ...)`](#addlistenerbroadcastreceived-)
* [`removeAllListeners()`](#removealllisteners)
* [Interfaces](#interfaces)
* [Type Aliases](#type-aliases)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

IntentShim — Capacitor port of com-darryncampbell-cordova-plugin-intent.

Scope: the three methods used by the OutSystems Zebra DataWedge library —
sendBroadcast, registerBroadcastReceiver, unregisterBroadcastReceiver.
Android-only behavior; iOS methods are no-ops that resolve successfully.

Cordova observable behavior is the source of truth. Where Cordova asymmetries
exist, they are preserved deliberately (see method-level notes).

### sendBroadcast(...)

```typescript
sendBroadcast(options: SendBroadcastOptions) => Promise<void>
```

Sends an Android broadcast intent with the given action and optional extras.

Cordova parity: all `extras` values are coerced to String before being
attached to the Intent (via `String.valueOf`). Consumers that need to pass
structured data should stringify it themselves (e.g. `JSON.stringify(obj)`)
— this matches how the OutSystems DataWedge connector already behaves.

iOS: no-op.

| Param         | Type                                                                  |
| ------------- | --------------------------------------------------------------------- |
| **`options`** | <code><a href="#sendbroadcastoptions">SendBroadcastOptions</a></code> |

--------------------


### registerBroadcastReceiver(...)

```typescript
registerBroadcastReceiver(options: RegisterBroadcastReceiverOptions) => Promise<void>
```

Registers a dynamic BroadcastReceiver for the given filter actions
(and optionally categories). Any receiver previously registered by this
plugin is unregistered first, mirroring the Cordova implementation.

Received intents are delivered via the `'broadcastReceived'` event —
subscribe using `addListener('broadcastReceived', cb)`.

Android 13+ (API 33+): the receiver is registered with
`Context.RECEIVER_EXPORTED`, required on API 34+ for broadcasts originating
from other apps (e.g. the Zebra DataWedge service).

iOS: no-op. No events will ever fire on iOS.

| Param         | Type                                                                                          |
| ------------- | --------------------------------------------------------------------------------------------- |
| **`options`** | <code><a href="#registerbroadcastreceiveroptions">RegisterBroadcastReceiverOptions</a></code> |

--------------------


### unregisterBroadcastReceiver()

```typescript
unregisterBroadcastReceiver() => Promise<void>
```

Unregisters the currently registered BroadcastReceiver, if any.
Safe to call when no receiver is registered (swallowed internally).

iOS: no-op.

--------------------


### addListener('broadcastReceived', ...)

```typescript
addListener(eventName: 'broadcastReceived', listenerFunc: (intent: BroadcastIntent) => void) => Promise<PluginListenerHandle>
```

Subscribes to broadcast events from the currently registered receiver.
Emits a {@link <a href="#broadcastintent">BroadcastIntent</a>} payload matching the Cordova
`getIntentJson` shape.

| Param              | Type                                                                             |
| ------------------ | -------------------------------------------------------------------------------- |
| **`eventName`**    | <code>'broadcastReceived'</code>                                                 |
| **`listenerFunc`** | <code>(intent: <a href="#broadcastintent">BroadcastIntent</a>) =&gt; void</code> |

**Returns:** <code>Promise&lt;<a href="#pluginlistenerhandle">PluginListenerHandle</a>&gt;</code>

--------------------


### removeAllListeners()

```typescript
removeAllListeners() => Promise<void>
```

Removes all listeners registered via {@link addListener}.

--------------------