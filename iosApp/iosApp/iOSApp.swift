import SwiftUI
import shared
import Firebase

@main
struct iOSApp: App {
    
    @UIApplicationDelegateAdaptor(AppDelegate.self) var deletegate
    private let appModule = AppModule()
    
    var body: some Scene {
        WindowGroup {
            SplashScreen(
                authRepository: appModule.authRepository,
                validationUseCase: appModule.validationUseCase,
                queueService: appModule.queueService,
                queueSocketService: appModule.queueSocketService,
                profileRepository: appModule.profileRepository,
                preferenceManager: appModule.preferenceManager
            )
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
