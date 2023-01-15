//
//  IOSQueueViewModel.swift
//  iosApp
//
//  Created by Amal Nuritdinkhodzhaev on 29/12/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared

@MainActor final class IOSQueueViewModel: ObservableObject {
    
    private var queueService: QueueService
    private var queueSocketService: QueueSocketService
    private var preferenceManager: PreferenceManager
    
    private let viewModel: QueueViewModel
    
    @Published var hasQueueError: Bool = false
    
    @Published var state: QueueState = QueueState(
        isQueueLoading: false, userId: nil, queue: [], error: nil
    )
    
    private var handle: DisposableHandle?
    
    init(
        queueService: QueueService,
        queueSocketService: QueueSocketService,
        preferenceManager: PreferenceManager
    ) {
        self.queueService = queueService
        self.queueSocketService = queueSocketService
        self.preferenceManager = preferenceManager
        self.viewModel = QueueViewModel(
            queueService: queueService,
            queueSocketService: queueSocketService,
            preferenceManager: preferenceManager,
            coroutineScope: nil
        )
    }
    
    func onEvent(event: QueueEvent) {
        viewModel.onEvent(event: event)
    }
    
    func startObserving() {
        handle = viewModel.state.subscribe { [weak self] state in
            if let state {
                self?.state = state
                self?.hasQueueError = state.error != nil
            }
        }
    }
    
    func dispose() {
        handle?.dispose()
    }
}
