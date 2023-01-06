import SwiftUI
import shared
import Firebase

@main
struct iOSApp: App {
    
    @UIApplicationDelegateAdaptor(AppDelegate.self) var deletegate
    private let appModule = AppModule()
    
	var body: some Scene {
		WindowGroup {
            NavigationView {
                SplashScreen(
                    authRepository: appModule.authRepository,
                    queueService: appModule.queueService,
                    queueSocketService: appModule.queueSocketService,
                    validationUseCase: appModule.validationUseCase
                )
            }
		}
	}
}

class AppDelegate: NSObject, UIApplicationDelegate {
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        FirebaseApp.configure()
        return true
    }
    
    func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable : Any]) async -> UIBackgroundFetchResult {
        return .noData
    }
}
