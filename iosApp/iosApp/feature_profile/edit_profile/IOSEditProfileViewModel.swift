//
//  IOSEditProfileViewModel.swift
//  iosApp
//
//  Created by Amal Nuritdinkhodzhaev on 15/1/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import FirebaseStorage
import SwiftUI
import shared

@MainActor final class IOSEditProfileViewModel: ObservableObject {
    
    private var profileRepository: ProfileRepository
    private var validationUseCase: ValidationUseCase
    
    private var viewModel: EditProfileViewModel
    
    @Published var hasResultMessage: Bool = false
    
    @Published var hasFirebaseStorageError: Bool = false
    @Published var firebaseStorageError: String?
    
    @Published var isUpdatingProfile: Bool = false
    
    @Published var state: EditProfileState = EditProfileState(
        isLoading: false,
        resultMessage: nil,
        userId: nil,
        firstName: "",
        firsNameError: nil,
        lastName: "",
        lastNameError: nil,
        telegram: "",
        instagram: "",
        profilePictureUri: "",
        dateOfBirth: "",
        dateOfBirthError: nil
    )
    
    private var handle: DisposableHandle?
    
    init(
        profileRepository: ProfileRepository,
        validationUseCase: ValidationUseCase
    ) {
        self.profileRepository = profileRepository
        self.validationUseCase = validationUseCase
        self.viewModel = EditProfileViewModel(
            validationUseCase: validationUseCase,
            profileRepository: profileRepository,
            coroutineScope: nil
        )
    }
    
    func uploadUserAvatarAndUpdateProfile(avatarImageData: Data?) {
        
        self.isUpdatingProfile = true
        
        if let avatarImageData = avatarImageData {
            let storageRef = Storage.storage().reference()
            let fileRef = storageRef.child(state.userId ?? UUID().uuidString)
            
            fileRef.putData(avatarImageData) { [self] metadata, error in
                if let error = error {
                    self.handleError(error: error.localizedDescription)
                    return
                }
                
                if metadata != nil {
                    fileRef.downloadURL { url, error in
                        if let error = error {
                            self.handleError(error: error.localizedDescription)
                            return
                        }
                        
                        if let url = url {
                            self.onEvent(
                                event: EditProfileEvent.EditProfile(
                                    profilePictureUri: url.absoluteString
                                )
                            )
                        } else {
                            self.handleError(error: "Unknown error")
                        }
                    }
                }
            }
        } else {
            onEvent(
                event: EditProfileEvent.EditProfile(
                    profilePictureUri: nil
                )
            )
        }
    }
    
    func handleError(error: String) {
        DispatchQueue.main.async {
            self.isUpdatingProfile = false
            self.firebaseStorageError = error
            self.hasFirebaseStorageError = true
        }
    }
    
    func onEvent(event: EditProfileEvent) {
        viewModel.onEvent(event: event)
    }
    
    func startObserving() {
        handle = viewModel.state.subscribe { [weak self] state in
            if let state {
                self?.state = state
                
                if state.resultMessage != nil {
                    self?.hasResultMessage = true
                    self?.isUpdatingProfile = false
                }
            }
        }
        
        onEvent(event: EditProfileEvent.GetProfile())
    }
    
    func dispose() {
        handle?.dispose()
    }
}


