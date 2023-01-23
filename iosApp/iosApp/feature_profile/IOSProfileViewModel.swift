//
//  IOSProfileViewModel.swift
//  iosApp
//
//  Created by Amal Nuritdinkhodzhaev on 14/1/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

@MainActor final class IOSProfileViewModel: ObservableObject {
    
    private var profileRepository: ProfileRepository
    private var authRepository: AuthRepository
    
    private let viewModel: ProfileViewModel
    
    @Published var hasProfileError: Bool = false
    
    @Published var isLogoutConfirmed: Bool = false
    
    @Published var isProfileLoading: Bool = false
    
    @Published var state: ProfileState = ProfileState(
        isLoading: false, profile: nil, error: nil
    )
    
    private var handle: DisposableHandle?
    
    init(
        profileRepository: ProfileRepository,
        authRepository: AuthRepository
    ) {
        self.profileRepository = profileRepository
        self.authRepository = authRepository
        self.viewModel = ProfileViewModel(
            profileRepository: profileRepository,
            authRepository: authRepository,
            coroutineScope: nil
        )
    }
    
    func onEvent(event: ProfileEvent) {
        viewModel.onEvent(event: event)
    }
    
    func startObserving() {
        handle = viewModel.state.subscribe { [weak self] state in
            if let state {
                self?.state = state
                self?.isProfileLoading = state.isLoading
                self?.hasProfileError = state.error != nil
            }
        }
        
        onEvent(event: ProfileEvent.GetProfile())
    }
    
    func dispose() {
        handle?.dispose()
    }
}


