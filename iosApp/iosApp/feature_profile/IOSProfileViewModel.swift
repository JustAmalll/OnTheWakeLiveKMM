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
    
    private let viewModel: ProfileViewModel
    
    @Published var hasProfileError: Bool = false
    
    @Published var state: ProfileState = ProfileState(
        isLoading: false, profile: nil, error: nil
    )
    
    private var handle: DisposableHandle?
    
    init(
        profileRepository: ProfileRepository
    ) {
        self.profileRepository = profileRepository
        self.viewModel = ProfileViewModel(
            profileRepository: profileRepository,
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
                self?.hasProfileError = state.error != nil
            }
        }
    }
    
    func dispose() {
        handle?.dispose()
    }
}


