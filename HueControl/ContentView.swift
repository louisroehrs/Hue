import SwiftUI

struct ContentView: View {
    @EnvironmentObject private var hueManager: HueManager
    @State private var showingPairingAlert = false
    
    var body: some View {
        NavigationView {
            Group {
                if hueManager.bridgeIP == nil {
                    discoveryView
                } else if hueManager.apiKey == nil {
                    pairingView
                } else {
                    lightListView
                }
            }
            .navigationTitle("Hue Control")
            .alert("Error", isPresented: .constant(hueManager.error != nil)) {
                Button("OK") {
                    hueManager.error = nil
                }
            } message: {
                Text(hueManager.error ?? "")
            }
        }
    }
    
    private var discoveryView: some View {
        VStack(spacing: 20) {
            if hueManager.isDiscovering {
                ProgressView()
                    .progressViewStyle(CircularProgressViewStyle())
                Text("Searching for Hue Bridge...")
            } else {
                Text("No Hue Bridge found")
                    .font(.headline)
                Button("Search for Bridge") {
                    hueManager.discoverBridge()
                }
                .buttonStyle(.borderedProminent)
            }
        }
        .padding()
    }
    
    private var pairingView: some View {
        VStack(spacing: 20) {
            Text("Press the link button on your Hue Bridge")
                .font(.headline)
                .multilineTextAlignment(.center)
            
            Image(systemName: "button.programmable")
                .font(.system(size: 50))
            
            Button("Start Pairing") {
                hueManager.pairWithBridge()
            }
            .buttonStyle(.borderedProminent)
            
            Button("Start Over", role: .destructive) {
                hueManager.bridgeIP = nil
            }
        }
        .padding()
    }
    
    private var lightListView: some View {
        List {
            ForEach(hueManager.lights) { light in
                LightRowView(light: light)
            }
        }
        .refreshable {
            hueManager.fetchLights()
        }
        .toolbar {
            Button("Reset", role: .destructive) {
                UserDefaults.standard.removeObject(forKey: "bridgeIP")
                UserDefaults.standard.removeObject(forKey: "apiKey")
                hueManager.bridgeIP = nil
                hueManager.apiKey = nil
            }
        }
    }
}

struct LightRowView: View {
    @EnvironmentObject private var hueManager: HueManager
    let light: HueManager.Light
    
    var body: some View {
        VStack(alignment: .leading) {
            HStack {
                Image(systemName: light.state.on ? "lightbulb.fill" : "lightbulb")
                    .foregroundColor(light.state.on ? .yellow : .gray)
                Text(light.name)
                    .font(.headline)
                Spacer()
                Toggle("", isOn: .init(
                    get: { light.state.on },
                    set: { _ in hueManager.toggleLight(light) }
                ))
            }
            
            if light.state.on, let brightness = light.state.bri {
                Slider(
                    value: .init(
                        get: { Double(brightness) },
                        set: { hueManager.setBrightness(Int($0), for: light) }
                    ),
                    in: 0...254
                )
            }
        }
        .opacity(light.state.reachable ? 1 : 0.5)
    }
}

#Preview {
    ContentView()
        .environmentObject(HueManager())
} 