//
//  IOSQueueItemDetailsViewModel.swift
//  iosApp
//
//  Created by Amal Nuritdinkhodzhaev on 15/1/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

@MainActor final class IOSQueueItemDetailsViewModel: ObservableObject {
    
    private var queueService: QueueService
    private let viewModel: QueueItemDetailsViewModel
    
    @Published var hasError: Bool = false
    
    @Published var state: QueueItemDetailsState = QueueItemDetailsState(
        isLoading: false, profile: nil, error: nil
    )
    
    private var handle: DisposableHandle?
    
    init(queueService: QueueService) {
        self.queueService = queueService
        self.viewModel = QueueItemDetailsViewModel(
            queueService: queueService,
            coroutineScope: nil
        )
    }
    
    func onEvent(event: QueueItemDetailsEvent) {
        viewModel.onEvent(event: event)
    }
    
    func startObserving() {
        handle = viewModel.state.subscribe { [weak self] state in
            if let state {
                self?.state = state
                self?.hasError = state.error != nil
            }
        }
    }
    
    func dispose() {
        handle?.dispose()
    }
}
