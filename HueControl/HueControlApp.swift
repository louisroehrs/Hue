import SwiftUI

@main
struct HueControlApp: App {
    @StateObject private var hueManager = HueManager()
    
    var body: some Scene {
        WindowGroup {
            ContentView()
                .environmentObject(hueManager)
        }
    }
} 