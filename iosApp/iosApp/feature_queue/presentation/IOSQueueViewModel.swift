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
    
    private let viewModel: QueueViewModel
    
    @Published var state: QueueState = QueueState(
        isQueueLoading: false, queue: [], error: nil
    )
    
    private var handle: DisposableHandle?
    
    init(queueService: QueueService, queueSocketService: QueueSocketService) {
        self.queueService = queueService
        self.queueSocketService = queueSocketService
        self.viewModel = QueueViewModel(
            queueService: queueService,
            queueSocketService: queueSocketService,
            coroutineScope: nil
        )
    }
    
    func onEvent(event: QueueSocketEvent) {
        viewModel.onEvent(event: event)
    }
    
    func startObserving() {
        handle = viewModel.state.subscribe { [weak self] state in
            if let state { self?.state = state }
        }
    }
    
    func dispose() {
        handle?.dispose()
    }
}
