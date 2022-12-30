import SwiftUI
import shared

struct ContentView: View {
    
    private let appModule = AppModule()
    
    @State private var selection = 1

	var body: some View {
        TabView {
            QueueScreen(queueService: appModule.queueService)
                .tabItem {
                    Image(systemName: "house.fill")
                    Text("Queue")
                }
                .tag(1)
            Text("Profile")
                .tabItem {
                    Image(systemName: "person.fill")
                    Text("Profile")
                }
                .tag(2)
        }
	}
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}
