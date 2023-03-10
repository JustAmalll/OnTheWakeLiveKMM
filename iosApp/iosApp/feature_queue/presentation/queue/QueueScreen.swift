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
    
    @State private var showDeleteConfirmationDialog = false
    @State private var queueItemIdToDelete: String = ""
    
    @State private var showAdminBottomSheet: Bool = false
    
    @State private var selectedLine = 2
    
    var body: some View {
        
        let state = viewModel.state
        
        let userId = state.userId ?? ""
        let isUserAdmin = Constants().ADMIN_IDS.contains(userId)
        
        NavigationView {
            VStack {
                Picker(selection: $selectedLine, label: Text("Queue")) {
                    Text("Left Line").tag(1)
                    Text("Right Line").tag(2)
                }
                .padding(.horizontal)
                .padding(.top, 10)
                .pickerStyle(SegmentedPickerStyle())
                
                if selectedLine == 1 {
                    QueueLeftContent(
                        state: state,
                        isUserAdmin: isUserAdmin,
                        onSwipeToDelete: { queueItemId in
                            showDeleteConfirmationDialog.toggle()
                            queueItemIdToDelete = queueItemId
                        }
                    )
                } else {
                    QueueRightContent(
                        state: state,
                        isUserAdmin: isUserAdmin,
                        onSwipeToDelete: { queueItemId in
                            showDeleteConfirmationDialog.toggle()
                            queueItemIdToDelete = queueItemId
                        }
                    )
                }
            }
            .navigationTitle("Queue")
            .alert(viewModel.state.error ?? "", isPresented: $viewModel.hasQueueError) {
                Button {
                    viewModel.onEvent(
                        event: QueueEvent.OnQueueErrorSeen()
                    )
                } label: {
                    Text("OK")
                }
            }
            .confirmationDialog(
                "Leave", isPresented: $showDeleteConfirmationDialog
            ) {
                Button(role: .destructive) {
                    viewModel.onEvent(
                        event: QueueEvent.DeleteQueueItem(
                            queueItemId: queueItemIdToDelete
                        )
                    )
                } label: {
                    if isUserAdmin {
                        Text("Remove")
                    } else {
                        Text("Leave")
                    }
                }
            } message: {
                if isUserAdmin {
                    Text("admin_remove_person_confirmation_text")
                } else {
                    Text("leave_queue_confirmation_text")
                }
            }
            .overlay(
                Button {
                    let isUserAdmin = Constants().ADMIN_IDS.contains(userId)
                    if isUserAdmin {
                        showAdminBottomSheet = true
                    } else {
                        viewModel.onEvent(
                            event: QueueEvent.AddToQueue(
                                isLeftQueue: selectedLine == 1,
                                firstName: nil
                            )
                        )
                    }
                } label: {
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
            .sheet(isPresented: $showAdminBottomSheet) {
                AdminBottomSheetContent(
                    queue: state.queue,
                    onAddClicked: { isLeftQueue, firstName in
                        viewModel.onEvent(
                            event: QueueEvent.AddToQueue(
                                isLeftQueue: isLeftQueue,
                                firstName: firstName
                            )
                        )
                    }
                )
            }
            .overlay {
                if state.isQueueLoading  {
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
    let isUserAdmin: Bool
    let onSwipeToDelete: (String) -> Void
    
    var body: some View {
        let rightQueue = state.queue.filter { queue in
            return queue.isLeftQueue == false
        }.reversed()
        
        if rightQueue.isEmpty {
            EmptyQueueContent()
        } else {
            List {
                ForEach(rightQueue, id: \.self.id) { queueItem in
                    QueueItemView(
                        queueItem: queueItem,
                        userId: state.userId ?? "",
                        onSwipeToDelete: onSwipeToDelete
                    )
                }
            }
            .padding(!isUserAdmin ? .bottom : .bottom, 0)
        }
    }
}

struct QueueLeftContent: View {
    let state: QueueState
    let isUserAdmin: Bool
    let onSwipeToDelete: (String) -> Void
    
    var body: some View {
        
        let leftQueue = state.queue.filter { queue in
            return queue.isLeftQueue == true
        }.reversed()
        
        if leftQueue.isEmpty {
            EmptyQueueContent()
        } else {
            List {
                ForEach(leftQueue, id: \.self.id) { queueItem in
                    QueueItemView(
                        queueItem: queueItem,
                        userId: state.userId ?? "",
                        onSwipeToDelete: onSwipeToDelete
                    )
                }
            }
            .padding(!isUserAdmin ? .bottom : .bottom, 0)
        }
    }
}
