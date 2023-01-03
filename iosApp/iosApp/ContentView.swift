import SwiftUI
import shared

struct ContentView: View {
    
    private let queueService: QueueService
    private let queueSocketService: QueueSocketService
    
    init(queueService: QueueService, queueSocketService: QueueSocketService) {
        self.queueService = queueService
        self.queueSocketService = queueSocketService
    }
    
    var body: some View {
        TabView {
            QueueScreen(
                queueService: queueService,
                queueSocketService: queueSocketService
            )
            .tabItem {
                Image(systemName: "house.fill")
                Text("Queue")
            }
            Text("Profile")
                .navigationTitle("Profile")
                .tabItem {
                    Image(systemName: "person.fill")
                    Text("Profile")
                }
        }
    }
}
