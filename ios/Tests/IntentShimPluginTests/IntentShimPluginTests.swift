import XCTest
@testable import IntentShimPlugin

/// Placeholder test. The iOS IntentShim implementation is a pure no-op bridge,
/// so there is nothing meaningful to unit-test. Real validation happens on
/// Android — see the consuming app's integration tests.
class IntentShimPluginTests: XCTestCase {
    func testBridgeInstantiation() {
        let plugin = IntentShimPlugin()
        XCTAssertEqual(plugin.jsName, "IntentShim")
        XCTAssertEqual(plugin.pluginMethods.count, 3)
    }
}
