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
    
    @Published var authResult: AuthResult?
    
    func startObserving() {
        handle = viewModel.authResult.subscribe { [weak self] authResult in
            if let authResult { self?.authResult = authResult }
        }
    }
    
    func dispose() {
        handle?.dispose()
    }
}
