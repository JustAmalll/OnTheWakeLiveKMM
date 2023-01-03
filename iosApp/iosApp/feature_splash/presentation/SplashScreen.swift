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
    @ObservedObject var viewModel: IOSSplashViewModel
    
    private var queueService: QueueService
    private var queueSocketService: QueueSocketService
    
    init(authRepository: AuthRepository, queueService: QueueService, queueSocketService: QueueSocketService) {
        self.queueService = queueService
        self.queueSocketService = queueSocketService
        self.authRepository = authRepository
        self.viewModel = IOSSplashViewModel(authRepository: authRepository)
    }
    
    @State var isActive: Bool = false
    @State var logoSize = 0.8
    @State var logoOpacity = 0.6
    
    var body: some View {
        
        if isActive {
            if let isAuthorized = viewModel.state.isAuthorized  {
                if isAuthorized == true {
                    ContentView(
                        queueService: queueService,
                        queueSocketService: queueSocketService
                    )
                } else {
                    LoginScreen(authRepository: authRepository)
                }
            }
        } else {
            VStack {
                Image("onthewake_logo_black")
                    .resizable()
                    .scaledToFit()
                    .frame(width: 300, height: 300)
            }
            .scaleEffect(logoSize)
            .opacity(logoOpacity)
            .onAppear {
                withAnimation(.easeIn(duration: 0.6)) {
                    self.logoSize = 0.9
                    self.logoOpacity = 1.0
                }
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
