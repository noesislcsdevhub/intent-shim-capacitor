# @noesis/intent-shim-capacitor

Capacitor port of com-darryncampbell-cordova-plugin-intent for OutSystems dual-stack. Android-only implementation of sendBroadcast, registerBroadcastReceiver, and unregisterBroadcastReceiver; iOS is a no-op bridge.

## Install

To use npm

```bash
npm install @noesis/intent-shim-capacitor
````

To use yarn

```bash
yarn add @noesis/intent-shim-capacitor
```

Sync native files

```bash
npx cap sync
```

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


### Interfaces


#### SendBroadcastOptions

| Prop         | Type                                                                                 | Description                                                                                                                                   |
| ------------ | ------------------------------------------------------------------------------------ | --------------------------------------------------------------------------------------------------------------------------------------------- |
| **`action`** | <code>string</code>                                                                  | Intent action string, e.g. "com.symbol.datawedge.api.ACTION".                                                                                 |
| **`extras`** | <code><a href="#record">Record</a>&lt;string, string \| number \| boolean&gt;</code> | Flat key/value map of extras. Values are coerced to String to match the Cordova implementation — structured payloads must be pre-stringified. |


#### RegisterBroadcastReceiverOptions

| Prop                   | Type                  | Description                                             |
| ---------------------- | --------------------- | ------------------------------------------------------- |
| **`filterActions`**    | <code>string[]</code> | Intent filter actions. At least one action is required. |
| **`filterCategories`** | <code>string[]</code> | Optional intent filter categories.                      |


#### PluginListenerHandle

| Prop         | Type                                      |
| ------------ | ----------------------------------------- |
| **`remove`** | <code>() =&gt; Promise&lt;void&gt;</code> |


#### BroadcastIntent

Shape of a received broadcast intent. Mirrors Cordova's `getIntentJson`
output so dual-stack consumer code does not need platform-specific parsing.

| Prop             | Type                                                                                               | Description                                              |
| ---------------- | -------------------------------------------------------------------------------------------------- | -------------------------------------------------------- |
| **`action`**     | <code>string \| null</code>                                                                        |                                                          |
| **`type`**       | <code>string \| null</code>                                                                        |                                                          |
| **`extras`**     | <code><a href="#record">Record</a>&lt;string, unknown&gt; \| null</code>                           |                                                          |
| **`categories`** | <code>string[] \| null</code>                                                                      |                                                          |
| **`flags`**      | <code>number</code>                                                                                |                                                          |
| **`component`**  | <code>string \| null</code>                                                                        |                                                          |
| **`data`**       | <code>string \| null</code>                                                                        |                                                          |
| **`package`**    | <code>string \| null</code>                                                                        |                                                          |
| **`clipItems`**  | <code><a href="#array">Array</a>&lt;<a href="#record">Record</a>&lt;string, unknown&gt;&gt;</code> | Present only when the intent carries ClipData (KitKat+). |


#### Array

| Prop         | Type                | Description                                                                                            |
| ------------ | ------------------- | ------------------------------------------------------------------------------------------------------ |
| **`length`** | <code>number</code> | Gets or sets the length of the array. This is a number one higher than the highest index in the array. |

| Method             | Signature                                                                                                                     | Description                                                                                                                                                                                                                                 |
| ------------------ | ----------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **toString**       | () =&gt; string                                                                                                               | Returns a string representation of an array.                                                                                                                                                                                                |
| **toLocaleString** | () =&gt; string                                                                                                               | Returns a string representation of an array. The elements are converted to string using their toLocalString methods.                                                                                                                        |
| **pop**            | () =&gt; T \| undefined                                                                                                       | Removes the last element from an array and returns it. If the array is empty, undefined is returned and the array is not modified.                                                                                                          |
| **push**           | (...items: T[]) =&gt; number                                                                                                  | Appends new elements to the end of an array, and returns the new length of the array.                                                                                                                                                       |
| **concat**         | (...items: <a href="#concatarray">ConcatArray</a>&lt;T&gt;[]) =&gt; T[]                                                       | Combines two or more arrays. This method returns a new array without modifying any existing arrays.                                                                                                                                         |
| **concat**         | (...items: (T \| <a href="#concatarray">ConcatArray</a>&lt;T&gt;)[]) =&gt; T[]                                                | Combines two or more arrays. This method returns a new array without modifying any existing arrays.                                                                                                                                         |
| **join**           | (separator?: string \| undefined) =&gt; string                                                                                | Adds all the elements of an array into a string, separated by the specified separator string.                                                                                                                                               |
| **reverse**        | () =&gt; T[]                                                                                                                  | Reverses the elements in an array in place. This method mutates the array and returns a reference to the same array.                                                                                                                        |
| **shift**          | () =&gt; T \| undefined                                                                                                       | Removes the first element from an array and returns it. If the array is empty, undefined is returned and the array is not modified.                                                                                                         |
| **slice**          | (start?: number \| undefined, end?: number \| undefined) =&gt; T[]                                                            | Returns a copy of a section of an array. For both start and end, a negative index can be used to indicate an offset from the end of the array. For example, -2 refers to the second to last element of the array.                           |
| **sort**           | (compareFn?: ((a: T, b: T) =&gt; number) \| undefined) =&gt; this                                                             | Sorts an array in place. This method mutates the array and returns a reference to the same array.                                                                                                                                           |
| **splice**         | (start: number, deleteCount?: number \| undefined) =&gt; T[]                                                                  | Removes elements from an array and, if necessary, inserts new elements in their place, returning the deleted elements.                                                                                                                      |
| **splice**         | (start: number, deleteCount: number, ...items: T[]) =&gt; T[]                                                                 | Removes elements from an array and, if necessary, inserts new elements in their place, returning the deleted elements.                                                                                                                      |
| **unshift**        | (...items: T[]) =&gt; number                                                                                                  | Inserts new elements at the start of an array, and returns the new length of the array.                                                                                                                                                     |
| **indexOf**        | (searchElement: T, fromIndex?: number \| undefined) =&gt; number                                                              | Returns the index of the first occurrence of a value in an array, or -1 if it is not present.                                                                                                                                               |
| **lastIndexOf**    | (searchElement: T, fromIndex?: number \| undefined) =&gt; number                                                              | Returns the index of the last occurrence of a specified value in an array, or -1 if it is not present.                                                                                                                                      |
| **every**          | &lt;S extends T&gt;(predicate: (value: T, index: number, array: T[]) =&gt; value is S, thisArg?: any) =&gt; this is S[]       | Determines whether all the members of an array satisfy the specified test.                                                                                                                                                                  |
| **every**          | (predicate: (value: T, index: number, array: T[]) =&gt; unknown, thisArg?: any) =&gt; boolean                                 | Determines whether all the members of an array satisfy the specified test.                                                                                                                                                                  |
| **some**           | (predicate: (value: T, index: number, array: T[]) =&gt; unknown, thisArg?: any) =&gt; boolean                                 | Determines whether the specified callback function returns true for any element of an array.                                                                                                                                                |
| **forEach**        | (callbackfn: (value: T, index: number, array: T[]) =&gt; void, thisArg?: any) =&gt; void                                      | Performs the specified action for each element in an array.                                                                                                                                                                                 |
| **map**            | &lt;U&gt;(callbackfn: (value: T, index: number, array: T[]) =&gt; U, thisArg?: any) =&gt; U[]                                 | Calls a defined callback function on each element of an array, and returns an array that contains the results.                                                                                                                              |
| **filter**         | &lt;S extends T&gt;(predicate: (value: T, index: number, array: T[]) =&gt; value is S, thisArg?: any) =&gt; S[]               | Returns the elements of an array that meet the condition specified in a callback function.                                                                                                                                                  |
| **filter**         | (predicate: (value: T, index: number, array: T[]) =&gt; unknown, thisArg?: any) =&gt; T[]                                     | Returns the elements of an array that meet the condition specified in a callback function.                                                                                                                                                  |
| **reduce**         | (callbackfn: (previousValue: T, currentValue: T, currentIndex: number, array: T[]) =&gt; T) =&gt; T                           | Calls the specified callback function for all the elements in an array. The return value of the callback function is the accumulated result, and is provided as an argument in the next call to the callback function.                      |
| **reduce**         | (callbackfn: (previousValue: T, currentValue: T, currentIndex: number, array: T[]) =&gt; T, initialValue: T) =&gt; T          |                                                                                                                                                                                                                                             |
| **reduce**         | &lt;U&gt;(callbackfn: (previousValue: U, currentValue: T, currentIndex: number, array: T[]) =&gt; U, initialValue: U) =&gt; U | Calls the specified callback function for all the elements in an array. The return value of the callback function is the accumulated result, and is provided as an argument in the next call to the callback function.                      |
| **reduceRight**    | (callbackfn: (previousValue: T, currentValue: T, currentIndex: number, array: T[]) =&gt; T) =&gt; T                           | Calls the specified callback function for all the elements in an array, in descending order. The return value of the callback function is the accumulated result, and is provided as an argument in the next call to the callback function. |
| **reduceRight**    | (callbackfn: (previousValue: T, currentValue: T, currentIndex: number, array: T[]) =&gt; T, initialValue: T) =&gt; T          |                                                                                                                                                                                                                                             |
| **reduceRight**    | &lt;U&gt;(callbackfn: (previousValue: U, currentValue: T, currentIndex: number, array: T[]) =&gt; U, initialValue: U) =&gt; U | Calls the specified callback function for all the elements in an array, in descending order. The return value of the callback function is the accumulated result, and is provided as an argument in the next call to the callback function. |


#### ConcatArray

| Prop         | Type                |
| ------------ | ------------------- |
| **`length`** | <code>number</code> |

| Method    | Signature                                                          |
| --------- | ------------------------------------------------------------------ |
| **join**  | (separator?: string \| undefined) =&gt; string                     |
| **slice** | (start?: number \| undefined, end?: number \| undefined) =&gt; T[] |


### Type Aliases


#### Record

Construct a type with a set of properties K of type T

<code>{ [P in K]: T; }</code>

</docgen-api>
