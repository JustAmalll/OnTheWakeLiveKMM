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
    
    init(
        authRepository: AuthRepository,
        queueService: QueueService,
        queueSocketService: QueueSocketService,
        validationUseCase: ValidationUseCase
    ) {
        self.queueService = queueService
        self.queueSocketService = queueSocketService
        self.authRepository = authRepository
        self.validationUseCase = validationUseCase
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
                DispatchQueue.main.asyncAfter(deadline: .now() + 2.0) {
                    isActive = true
                }
            }
            .onDisappear {
                splashViewModel.dispose()
            }
        }
    }
}

//            if let isAuthorized = splashViewModel.state.isAuthorized  {
//                if isAuthorized == true {
//                    ContentView()
//                        .environmentObject(
//                            IOSQueueViewModel(
//                                queueService: queueService,
//                                queueSocketService: queueSocketService
//                            )
//                        )
//                        .environmentObject(
//                            IOSOtpViewModel(
//                                authRepository: authRepository,
//                                validationUseCase: validationUseCase
//                            )
//                        )
//                } else {
//                    ContentView()
//                        .environmentObject(
//                            IOSLoginViewModel(
//                                authRepository: authRepository,
//                                validationUseCase: validationUseCase
//                            )
//                        )
//                        .environmentObject(
//                            IOSRegisterViewModel(
//                                validationUseCase: validationUseCase
//                            )
//                        )
//                        .environmentObject(
//                            IOSOtpViewModel(
//                                authRepository: authRepository,
//                                validationUseCase: validationUseCase
//                            )
//                        )
//                        .environmentObject(
//                            IOSQueueViewModel(
//                                queueService: queueService,
//                                queueSocketService: queueSocketService
//                            )
//                        )
//                }
//            }
