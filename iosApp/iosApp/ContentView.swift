import SwiftUI
import shared

struct ContentView: View {
    
    @ObservedObject var loginViewModel: IOSLoginViewModel
    @ObservedObject var registerViewModel: IOSRegisterViewModel
    @ObservedObject var otpViewModel: IOSOtpViewModel
    
    private let authRepository: AuthRepository
    private let validationUseCase: ValidationUseCase
    
    private let queueService: QueueService
    private let queueSocketService: QueueSocketService
    
    private let profileRepository: ProfileRepository
    
    private let preferenceManager: PreferenceManager
    
    private var isAuthorized: Bool = false
    
    init(
        authRepository: AuthRepository,
        validationUseCase: ValidationUseCase,
        queueService: QueueService,
        queueSocketService: QueueSocketService,
        profileRepository: ProfileRepository,
        preferenceManager: PreferenceManager,
        isAuthorized: Bool
    ) {
        self.queueService = queueService
        self.queueSocketService = queueSocketService
        
        self.profileRepository = profileRepository
        
        self.preferenceManager = preferenceManager
        
        self.authRepository = authRepository
        self.validationUseCase = validationUseCase
        
        self.loginViewModel = IOSLoginViewModel(
            authRepository: authRepository,
            validationUseCase: validationUseCase
        )
        self.registerViewModel = IOSRegisterViewModel(
            validationUseCase: validationUseCase
        )
        self.otpViewModel = IOSOtpViewModel(
            authRepository: authRepository,
            validationUseCase: validationUseCase
        )
        
        self.isAuthorized = isAuthorized
    }
    
    var body: some View {
        
        let isLoginSuccess = loginViewModel.state.loginResult == .authorized
        
        if isAuthorized || isLoginSuccess || otpViewModel.isOtpVerified {
            mainContent
        } else {
            LoginScreen()
                .environmentObject(loginViewModel)
                .environmentObject(registerViewModel)
                .environmentObject(otpViewModel)
        }
    }
    
    var mainContent: some View {
        TabView {
            QueueScreen()
                .environmentObject(
                    IOSQueueViewModel(
                        queueService: queueService,
                        queueSocketService: queueSocketService,
                        preferenceManager: preferenceManager
                    )
                )
                .tabItem {
                    Label("Queue", systemImage: "house.fill")
                }
            ProfileScreen()
                .environmentObject(
                    IOSProfileViewModel(
                        profileRepository: profileRepository
                    )
                )
                .tabItem {
                    Label("Profile", systemImage: "person.fill")
                }
        }
        .tint(.black)
    }
}
