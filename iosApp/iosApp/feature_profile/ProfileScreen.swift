//
//  ProfileScreen.swift
//  iosApp
//
//  Created by Amal Nuritdinkhodzhaev on 13/1/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct ProfileScreen: View {
    
    @EnvironmentObject var loginViewModel: IOSLoginViewModel
    @EnvironmentObject var viewModel: IOSProfileViewModel
    
    @State var showLogoutConfitamationDialog: Bool = false
    
    var body: some View {
        
        let state = viewModel.state
        let profile = state.profile
        
        NavigationView {
            VStack {
                NavigationLink(destination: EditProfileScreen()) {
                    Form {
                        Section {
                            HStack {
                                StandardUserPicture(imageUrl: profile?.profilePictureUri)
                                
                                VStack(alignment: .leading) {
                                    Text(profile?.firstName ?? "Not specified")
                                        .font(.headline)
                                    Text(profile?.lastName ?? "Not specified")
                                        .font(.subheadline)
                                }
                            }
                            .frame(
                                maxWidth: .infinity,
                                alignment: .leading
                            )
                            .overlay(alignment: .trailing) {
                                Image(systemName: "pencil")
                                    .resizable()
                                    .frame(width: 18, height: 18)
                                    .padding(.bottom)
                                    .padding(.trailing, 4)
                            }
                        } header: {
                            Spacer()
                        }
                    }
                    .disabled(true)
                    .frame(height: 114)
                }
                
                VStack(alignment: .leading, spacing: 6) {
                    ProfileItem(
                        title: "Instagram",
                        subTitle: profile?.instagram,
                        isLastItem: false
                    )
                    ProfileItem(
                        title: "Telegram",
                        subTitle: profile?.telegram,
                        isLastItem: false
                    )
                    ProfileItem(
                        title: "Phone number",
                        subTitle: profile?.phoneNumber,
                        isLastItem: false
                    )
                    ProfileItem(
                        title: "Date of Bitrh",
                        subTitle: profile?.dateOfBirth,
                        isLastItem: true
                    )
                }
                .padding(.horizontal, 24)
                .padding(.vertical)
            }
            .frame(
                maxWidth: .infinity,
                maxHeight: .infinity,
                alignment: .topLeading
            )
            .navigationTitle("Profile")
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button {
                        showLogoutConfitamationDialog = true
                    } label: {
                        Image(systemName: "rectangle.portrait.and.arrow.right")
                    }
                }
            }
            .alert(state.error ?? "", isPresented: $viewModel.hasProfileError) {
                Button{
                    viewModel.onEvent(
                        event: ProfileEvent.OnProfileErrorSeen()
                    )
                } label: {
                    Text("OK")
                }
            }
            .confirmationDialog(
                "Confirm", isPresented: $showLogoutConfitamationDialog
            ) {
                Button(role: .destructive) {
//                    viewModel.onEvent(event: ProfileEvent.Logout())
                    viewModel.isLogoutConfirmed = true
                } label: {
                    Text("Logout")
                }
            } message: {
                Text("Are you sure you want to logout?")
            }
            .overlay {
                if state.isLoading  {
                    Color(.systemBackground).ignoresSafeArea()
                    ProgressView()
                }
            }
            .onAppear {
                viewModel.startObserving()
                viewModel.onEvent(event: ProfileEvent.GetProfile())
            }
            .onDisappear {
                viewModel.dispose()
            }
        }
    }
}

struct ProfileScreen_Previews: PreviewProvider {
    static var previews: some View {
        ProfileScreen()
    }
}
