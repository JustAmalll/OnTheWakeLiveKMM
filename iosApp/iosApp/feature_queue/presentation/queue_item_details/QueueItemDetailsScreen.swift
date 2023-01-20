//
//  QueueItemDetailsScreen.swift
//  iosApp
//
//  Created by Amal Nuritdinkhodzhaev on 15/1/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct QueueItemDetailsScreen: View {
    
    @EnvironmentObject var viewModel: IOSQueueItemDetailsViewModel
    
    let queueItemId: String
    
    var body: some View {
        
        let state = viewModel.state
        let profile = state.profile
        
        VStack {
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
                } header: {
                    Spacer()
                }
            }
            .frame(height: 114)
            .disabled(true)
            
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
        .navigationTitle("Details")
        .alert(state.error ?? "", isPresented: $viewModel.hasError) {
            Button{
                viewModel.onEvent(
                    event: QueueItemDetailsEvent.OnErrorSeen()
                )
            } label: {
                Text("OK")
            }
        }
        .overlay {
            if state.isLoading  {
                Color(.systemBackground).ignoresSafeArea()
                ProgressView()
            }
        }
        .onAppear {
            viewModel.startObserving()
            viewModel.onEvent(
                event: QueueItemDetailsEvent.LoadQueueItemDetails(
                    queueItemId: queueItemId
                )
            )
        }
        .onDisappear {
            viewModel.dispose()
        }
    }
}

struct QueueItemDetailsScreen_Previews: PreviewProvider {
    static var previews: some View {
        QueueItemDetailsScreen(queueItemId: "")
    }
}
