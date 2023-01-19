//
//  EditProfileScreen.swift
//  iosApp
//
//  Created by Amal Nuritdinkhodzhaev on 15/1/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import FirebaseStorage
import CachedAsyncImage
import shared

struct EditProfileScreen: View {
    
    @EnvironmentObject var viewModel: IOSEditProfileViewModel
    @Environment(\.dismiss) var dismiss
    
    @State private var isShowingPhotoPicker = false
    @State private var hasPhotoPickerError: Bool = false
    @State private var photoPickerError: String?
    
    @State private var avatarImage: UIImage?
    
    var body: some View {
        
        let state = viewModel.state
        
        let firstNameError = state.firsNameError
        let lastNameError = state.lastNameError
        let dateOfBirthError = state.dateOfBirthError
        
        VStack {
            Form {
                Section {
                    if let avatarImage = avatarImage {
                        Image(uiImage: avatarImage)
                            .resizable()
                            .scaledToFill()
                            .frame(width: 120, height: 120)
                            .clipShape(Circle())
                            .onTapGesture {
                                isShowingPhotoPicker = true
                            }
                        
                    } else {
                        CachedAsyncImage(
                            url: URL(string: viewModel.state.profilePictureUri)
                        ) { phase in
                            if let image = phase.image {
                                image
                                    .resizable()
                                    .frame(width: 120, height: 120)
                                    .scaledToFit()
                                    .clipShape(Circle())
                            } else {
                                ZStack {
                                    Circle()
                                        .foregroundColor(.gray)
                                    Image(systemName: "person.fill")
                                        .resizable()
                                        .frame(width: 24, height: 24)
                                        .foregroundColor(Color.white)
                                }
                                .frame(width: 120, height: 120)
                            }
                        }
                        .onTapGesture {
                            isShowingPhotoPicker = true
                        }
                    }
                }
                .padding(.top)
                .frame(maxWidth: .infinity, alignment: .center)
                .listRowBackground(Color.clear)
                
                Section {
                    TextField("First name", text: Binding(
                        get: { state.firstName },
                        set: { value in
                            viewModel.onEvent(
                                event: EditProfileEvent.EditProfileFirstNameChanged(value: value)
                            )
                        }
                    ))
                    .textContentType(.name)
                    .keyboardType(.namePhonePad)
                    
                    TextField( "Last name", text: Binding(
                        get: { state.lastName },
                        set: { value in
                            viewModel.onEvent(
                                event: EditProfileEvent.EditProfileLastNameChanged(value: value)
                            )
                        }
                    ))
                    .textContentType(.familyName)
                    .keyboardType(.namePhonePad)
                    
                    TextField("Telegram", text: Binding(
                        get: { state.telegram },
                        set: { value in
                            viewModel.onEvent(
                                event: EditProfileEvent.EditProfileTelegramChanged(value: value)
                            )
                        }
                    ))
                    TextField("Instagram", text: Binding(
                        get: { state.instagram },
                        set: { value in
                            viewModel.onEvent(
                                event: EditProfileEvent.EditProfileInstagramChanged(value: value)
                            )
                        }
                    ))
                    TextField("Date of Birth", text: Binding(
                        get: { state.dateOfBirth },
                        set: { value in
                            viewModel.onEvent(
                                event: EditProfileEvent.EditProfileDateOfBirthChanged(value: value)
                            )
                        }
                    ))
                    .keyboardType(.numberPad)
                } footer: {
                    if let firstNameError = firstNameError {
                        Text(LocalizedStringKey(firstNameError))
                            .foregroundColor(.red)
                    } else if let lastNameError = lastNameError {
                        Text(LocalizedStringKey(lastNameError))
                            .foregroundColor(.red)
                    } else if let dateOfBirthError = dateOfBirthError {
                        Text(LocalizedStringKey(dateOfBirthError))
                            .foregroundColor(.red)
                    } else {
                        Text("")
                    }
                }
                
                Button {
                    viewModel.uploadUserAvatarAndUpdateProfile(
                        avatarImageData: avatarImage?.jpegData(compressionQuality: 0.5)
                    )
                } label: {
                    Text("Edit")
                        .foregroundColor(.blue)
                }
            }
        }
        .navigationTitle("Edit Profile")
        .sheet(isPresented: $isShowingPhotoPicker) {
            PhotoPicker(
                avatarImage: $avatarImage,
                onError: { error in
                    hasPhotoPickerError = true
                    photoPickerError = error
                }
            )
        }
        .overlay {
            if state.isLoading || viewModel.isUpdatingProfile {
                Color(.systemBackground).ignoresSafeArea()
                ProgressView()
            }
        }
        .alert(state.resultMessage ?? "", isPresented: $viewModel.hasResultMessage) {
            Button{
                viewModel.onEvent(
                    event: EditProfileEvent.OnResultSeen()
                )
                // hardcoded string res
                if state.resultMessage == "Successfully updated profile" {
                    dismiss()
                }
            } label: {
                Text("OK")
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


struct EditProfileScreen_Previews: PreviewProvider {
    static var previews: some View {
        EditProfileScreen()
    }
}
