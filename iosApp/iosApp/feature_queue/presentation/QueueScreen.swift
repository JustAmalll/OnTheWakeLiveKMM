//
//  QueueScreen.swift
//  iosApp
//
//  Created by Amal Nuritdinkhodzhaev on 29/12/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct QueueScreen: View {
    
    @EnvironmentObject var viewModel: IOSQueueViewModel
    @State private var selected = 2
    
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
                
                if selected == 1 {
                    QueueLeftContent(
                        state: viewModel.state, event: { event in
                            viewModel.onEvent(event: event)
                        }
                    )
                } else {
                    QueueRightContent(
                        state: viewModel.state, event: { event in
                            viewModel.onEvent(event: event)
                        }
                    )
                }
            }
            .navigationTitle("Queue")
            .overlay(
                Button(
                    action: {
                        viewModel.onEvent(
                            event: QueueSocketEvent.AddToQueue(
                                isLeftQueue: selected == 1,
                                firstName: "ios test",
                                timestamp: 123123
                            )
                        )
                    }
                ) {
                    Image(systemName: "plus")
                        .foregroundColor(.white)
                        .frame(width: 24, height: 24)
                        .padding()
                        .background(.black)
                        .cornerRadius(14)
                }
                    .padding(.trailing)
                    .padding(.bottom, 40),
                alignment: .bottomTrailing
            )
            .overlay {
                if viewModel.state.isQueueLoading  {
                    Color(.systemBackground).ignoresSafeArea()
                    ProgressView()
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

struct QueueRightContent: View {
    let state: QueueState
    let event: (QueueSocketEvent) -> Void
    
    var body: some View {
        let rightQueue = state.queue.filter { queue in
            return queue.isLeftQueue == false
        }
        
        if rightQueue.isEmpty {
            EmptyQueueContent()
        } else {
            List {
                ForEach(rightQueue, id: \.self.id) { queueItem in
                    NavigationLink(destination: Text(queueItem.firstName)) {
                        QueueItem(queueItem: queueItem, event: event)
                    }
                }
            }
            .padding(.bottom)
        }
    }
}

struct QueueLeftContent: View {
    let state: QueueState
    let event: (QueueSocketEvent) -> Void
    
    var body: some View {
        let leftQueue = state.queue.filter { queue in
            return queue.isLeftQueue == true
        }
        
        if leftQueue.isEmpty {
            EmptyQueueContent()
        } else {
            List {
                ForEach(leftQueue, id: \.self.id) { queueItem in
                    NavigationLink(destination: Text(queueItem.firstName)) {
                        QueueItem(queueItem: queueItem, event: event)
                    }
                }
            }
            .padding(.bottom)
        }
    }
}
