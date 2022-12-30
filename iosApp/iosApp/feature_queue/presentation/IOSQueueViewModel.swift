//
//  IOSQueueViewModel.swift
//  iosApp
//
//  Created by Amal Nuritdinkhodzhaev on 29/12/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared

extension QueueScreen {
    @MainActor class IOSQueueViewModel: ObservableObject {
        private var queueService: QueueService
        
        private let viewModel: QueueViewModel
        
        @Published var state: QueueState = QueueState(
            queue: [], isQueueLoading: false
        )
    
        private var handle: DisposableHandle?
        
        init(queueService: QueueService) {
            self.queueService = queueService
            self.viewModel = QueueViewModel(
                queueService: queueService, coroutineScope: nil
            )
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
}
