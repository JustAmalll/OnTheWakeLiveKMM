//
//  SplashViewModel.swift
//  iosApp
//
//  Created by Amal Nuritdinkhodzhaev on 3/1/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

@MainActor final class IOSSplashViewModel: ObservableObject {
    
    private let authRepository: AuthRepository
    private let viewModel: SplashViewModel
    
    @Published var isSplashScreenShowing: Bool = true
    
    init(authRepository: AuthRepository) {
        self.authRepository = authRepository
        self.viewModel = SplashViewModel(
            repository: authRepository,
            coroutineScope: nil
        )
    }
    
    private var handle: DisposableHandle?
    
    @Published var state: SplashScreenState = SplashScreenState(
        isAuthorized: nil
    )
    
    func startObserving() {
        handle = viewModel.state.subscribe { [weak self] state in
            if let state { self?.state = state }
        }
    }
    
    func dispose() {
        handle?.dispose()
    }
}
