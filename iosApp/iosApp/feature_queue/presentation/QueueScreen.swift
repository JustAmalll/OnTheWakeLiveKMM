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
                        .frame(width: 20, height: 20)
                        .padding()
                        .background(.black)
                        .cornerRadius(14)
                }
                    .padding(.trailing)
                    .padding(.bottom),
                alignment: .bottomTrailing
            )
            .navigationTitle("Queue")
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
        List {
            ForEach(rightQueue, id: \.self.id) { queueItem in
                NavigationLink(destination: Text(queueItem.firstName)) {
                    QueueItem(queueItem: queueItem, event: event)
                }
            }
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
        List {
            ForEach(leftQueue, id: \.self.id) { queueItem in
                NavigationLink(destination: Text(queueItem.firstName)) {
                    QueueItem(queueItem: queueItem, event: event)
                }
            }
        }
    }
}
