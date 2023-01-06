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
    
    private var authRepository: AuthRepository
    private var validationUseCase: ValidationUseCase
    @ObservedObject var viewModel: IOSSplashViewModel
    
    private var queueService: QueueService
    private var queueSocketService: QueueSocketService
    
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
        self.viewModel = IOSSplashViewModel(authRepository: authRepository)
    }
    
    @State var isActive: Bool = false
    
    var body: some View {
        
        if isActive {
            LoginScreen(
                authRepository: authRepository,
                validationUseCase: validationUseCase
            )
//            if let isAuthorized = viewModel.state.isAuthorized  {
//                if isAuthorized == true {
//                    ContentView(
//                        queueService: queueService,
//                        queueSocketService: queueSocketService
//                    )
//                } else {
//                    LoginScreen(authRepository: authRepository)
//                }
//            }
        } else {
            VStack {
                Image("onthewake_logo_black")
                    .resizable()
                    .scaledToFit()
                    .frame(width: 260, height: 260)
            }
            .onAppear {
                withAnimation(.easeIn) {}
            }
            .onAppear {
                DispatchQueue.main.asyncAfter(deadline: .now() + 2.0) {
                    self.isActive = true
                }
            }
            .onAppear {
                viewModel.startObserving()
            }
            .onDisappear {
                viewModel.dispose()
            }
        }
    }
}
