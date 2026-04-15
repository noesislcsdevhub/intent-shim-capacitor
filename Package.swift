// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "NoesisIntentShimCapacitor",
    platforms: [.iOS(.v15)],
    products: [
        .library(
            name: "NoesisIntentShimCapacitor",
            targets: ["IntentShimPlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", from: "8.0.0")
    ],
    targets: [
        .target(
            name: "IntentShimPlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/IntentShimPlugin"),
        .testTarget(
            name: "IntentShimPluginTests",
            dependencies: ["IntentShimPlugin"],
            path: "ios/Tests/IntentShimPluginTests")
    ]
)