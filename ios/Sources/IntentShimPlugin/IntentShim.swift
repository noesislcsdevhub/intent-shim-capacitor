import Foundation

@objc public class IntentShim: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
