//
//  QueueScreen.swift
//  iosApp
//
//  Created by Amal Nuritdinkhodzhaev on 29/12/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared
import CachedAsyncImage

struct QueueScreen: View {
    private var queueService: QueueService
    @ObservedObject var viewModel: IOSQueueViewModel
    
    @State private var selected = 1

    init(queueService: QueueService) {
        self.queueService = queueService
        self.viewModel = IOSQueueViewModel(queueService: queueService)
    }
    
    var body: some View {
        NavigationView {
            VStack {
                Picker(
                    selection: $selected, label: Text("Queue")
                ) {
                    Text("Left Line").tag(1)
                    Text("Right Line").tag(2)
                }
                .padding(.horizontal)
                .padding(.top, 10)
                .pickerStyle(SegmentedPickerStyle())
                
                List {
                    ForEach(viewModel.state.queue, id: \.self.id) { queueItem in
                        NavigationLink(destination: Text(queueItem.firstName)) {
                            HStack() {
                                CachedAsyncImage(
                                    url: URL(string: queueItem.profilePictureUri)
                                ) { phase in
                                    
                                    if let image = phase.image {
                                        image
                                            .resizable()
                                            .frame(width: 40, height: 40)
                                            .scaledToFit()
                                            .clipShape(Circle())
                                    } else {
                                        ZStack {
                                            Circle()
                                                .foregroundColor(.gray)
                                            Image(systemName: "person")
                                                .foregroundColor(Color.white)
                                        }
                                        .frame(width: 40, height: 40)
                                    }
                                }
                                .padding(.trailing, 4)
                                
                                VStack(alignment: .leading) {
                                    Text(queueItem.firstName)
                                        .font(.headline)
                                    Text(queueItem.lastName)
                                        .font(.subheadline)
                                }
                            }
                            .swipeActions() {
                                Button(role: .destructive) {
                                    
                                } label: {
                                    Label("Delete", systemImage: "trash.fill")
                                }
                            }
                        }
                    }
                }
            }
            .navigationTitle("Queue")
            .overlay(
                Button(action: {}) {
                    Image(systemName: "plus")
                        .foregroundColor(.white)
                        .frame(width: 20, height: 20)
                        .padding()
                        .background(.black)
                        .cornerRadius(14)
                }
                    .padding(.trailing)
                    .padding(.bottom),
                alignment: .bottomTrailing
            )
            .onAppear {
                viewModel.startObserving()
            }
            .onDisappear {
                viewModel.dispose()
            }
        }
    }
}
