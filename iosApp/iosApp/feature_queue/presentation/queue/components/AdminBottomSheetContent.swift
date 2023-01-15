//
//  AdminBottomSheetContent.swift
//  iosApp
//
//  Created by Amal Nuritdinkhodzhaev on 13/1/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct AdminBottomSheetContent: View {
    
    @Environment(\.dismiss) private var dismiss
    
    @State private var firstName: String = ""
    
    @State private var hasValidationError: Bool = false
    @State private var validationError: String?
    
    @State private var selectedLine = 2
    
    let queue: [QueueItem]
    let onAddClicked: (Bool, String) -> Void
    
    var body: some View {
        NavigationView {
            VStack {
                Picker(selection: $selectedLine, label: Text("Queue")) {
                    Text("Left Line").tag(1)
                    Text("Right Line").tag(2)
                }
                .padding(.horizontal)
                .padding(.top)
                .pickerStyle(SegmentedPickerStyle())
                
                Form {
                    TextField("First name", text: $firstName)
                }
            }
            .navigationTitle("Add to queue")
            .toolbar {
                ToolbarItem(placement: .navigationBarLeading) {
                    Button("Cancel", role: .cancel) {
                        dismiss()
                    }
                    .tint(.blue)
                }
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button("Done") {
                        let result = ValidationUseCase()
                            .validateAdminAddToQueue(
                                firstName: firstName, queue: queue
                            )
                        if result.successful {
                            onAddClicked(selectedLine == 1, firstName)
                            dismiss()
                        } else {
                            hasValidationError = true
                            validationError = result.errorMessage
                        }
                    }
                }
            }
            .alert(validationError ?? "", isPresented: $hasValidationError) {}
        }
    }
}

struct AdminBottomSheetContent_Previews: PreviewProvider {
    static var previews: some View {
        AdminBottomSheetContent(
            queue: [], onAddClicked: { _, _ in }
        )
    }
}
