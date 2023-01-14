//
//  SplashScreen.swift
//  iosApp
//
//  Created by Amal Nuritdinkhodzhaev on 3/1/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct SplashScreen: View {
    
    private let authRepository: AuthRepository
    private let validationUseCase: ValidationUseCase
    @ObservedObject var splashViewModel: IOSSplashViewModel
    
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
    }
    
    @State var isActive: Bool = false
    
    var body: some View {
        
        if isActive {
            ContentView(
                authRepository: authRepository,
                validationUseCase: validationUseCase,
                queueService: queueService,
                queueSocketService: queueSocketService,
                profileRepository: profileRepository,
                preferenceManager: preferenceManager,
                isAuthorized: splashViewModel.state.isAuthorized == true
            )
        } else {
            VStack {
                Image("onthewake_logo_black")
                    .resizable()
                    .scaledToFit()
                    .frame(width: 260, height: 260)
            }
            .onAppear {
                splashViewModel.startObserving()
            }
            .onAppear {
                withAnimation(.easeIn) {}
            }
            .onAppear {
                DispatchQueue.main.asyncAfter(deadline: .now() + 1.0) {
                    isActive = true
                }
            }
            .onDisappear {
                splashViewModel.dispose()
            }
        }
    }
}
