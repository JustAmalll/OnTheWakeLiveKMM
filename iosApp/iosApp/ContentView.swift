import SwiftUI
import shared

struct ContentView: View {
    
    @ObservedObject var splashViewModel: IOSSplashViewModel
    
    @ObservedObject var loginViewModel: IOSLoginViewModel
    @ObservedObject var registerViewModel: IOSRegisterViewModel
    @ObservedObject var otpViewModel: IOSOtpViewModel
    
    @ObservedObject var queueViewModel: IOSQueueViewModel
    @ObservedObject var queueItemDetailsViewModel: IOSQueueItemDetailsViewModel
    
    @ObservedObject var profileViewModel: IOSProfileViewModel
    @ObservedObject var editProfileViewModel: IOSEditProfileViewModel
    
    private let authRepository: AuthRepository
    private let validationUseCase: ValidationUseCase
    
    private let queueService: QueueService
    private let queueSocketService: QueueSocketService
    
    private let profileRepository: ProfileRepository
    
    private let preferenceManager: PreferenceManager
    
    init(
        authRepository: AuthRepository,
        validationUseCase: ValidationUseCase,
        queueService: QueueService,
        queueSocketService: QueueSocketService,
        profileRepository: ProfileRepository,
        preferenceManager: PreferenceManager
    ) {
        self.authRepository = authRepository
        self.validationUseCase = validationUseCase
        
        self.queueService = queueService
        self.queueSocketService = queueSocketService
        
        self.profileRepository = profileRepository
        
        self.preferenceManager = preferenceManager
        
        self.splashViewModel = IOSSplashViewModel(
            authRepository: authRepository
        )
        self.loginViewModel = IOSLoginViewModel(
            authRepository: authRepository,
            validationUseCase: validationUseCase
        )
        self.registerViewModel = IOSRegisterViewModel(
            validationUseCase: validationUseCase
        )
        self.queueViewModel = IOSQueueViewModel(
            queueService: queueService,
            queueSocketService: queueSocketService,
            preferenceManager: preferenceManager
        )
        self.queueItemDetailsViewModel = IOSQueueItemDetailsViewModel(
            queueService: queueService
        )
        self.profileViewModel = IOSProfileViewModel(
            profileRepository: profileRepository,
            authRepository: authRepository
        )
        self.editProfileViewModel = IOSEditProfileViewModel(
            profileRepository: profileRepository,
            validationUseCase: validationUseCase
        )
        self.otpViewModel = IOSOtpViewModel(
            authRepository: authRepository,
            validationUseCase: validationUseCase
        )
    }
    
    var body: some View {
        
        let userId = preferenceManager.getString(key: Constants().PREFS_USER_ID) ?? ""
        let isUserAdmin = Constants().ADMIN_IDS.contains(userId)
        
        let isAuthorized = splashViewModel.authResult == .authorized
        let isLoginSuccess = loginViewModel.state.loginResult == .authorized
        let isOtpVerified = otpViewModel.isOtpVerified
        
        if splashViewModel.isSplashScreenShowing {
            SplashScreen()
                .environmentObject(splashViewModel)
        } else if splashViewModel.authResult == .unknownerror {
            errorContent
        } else {
            if isAuthorized || isLoginSuccess || isOtpVerified {
                if isUserAdmin { adminContent }
                else {
                    mainContent
                        .fullScreenCover(isPresented: $profileViewModel.isLogoutConfirmed) {
                            loginScreen
                        }
                }
            } else {
                loginScreen
            }
        }
    }
    
    var loginScreen: some View {
        LoginScreen()
            .environmentObject(loginViewModel)
            .environmentObject(registerViewModel)
            .environmentObject(otpViewModel)
    }
    
    var adminContent: some View {
        QueueScreen()
            .environmentObject(queueItemDetailsViewModel)
            .environmentObject(queueViewModel)
    }
    
    var mainContent: some View {
        TabView {
            QueueScreen()
                .environmentObject(queueViewModel)
                .environmentObject(queueItemDetailsViewModel)
                .tabItem { Label("Queue", systemImage: "house.fill") }
            
            ProfileScreen()
                .environmentObject(profileViewModel)
                .environmentObject(editProfileViewModel)
                .tabItem { Label("Profile", systemImage: "person.fill") }
        }
        .tint(.black)
    }
    
    var errorContent: some View {
        VStack {
            LottieView(animationName: "server_error")
                .frame(width: 250, height: 250)
            
            Text("Server Unavailable")
                .font(.system(size: 22, weight: .semibold))
                .foregroundColor(.red)
            
            Text("The server is temporarily busy, try again later!")
                .font(.system(size: 16))
                .foregroundColor(.red)
                .padding(.top, 1)
        }
        .padding(.bottom, 80)
    }
}
