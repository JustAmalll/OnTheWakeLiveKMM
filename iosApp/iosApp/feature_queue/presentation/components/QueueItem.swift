//
//  QueueItem.swift
//  iosApp
//
//  Created by Amal Nuritdinkhodzhaev on 31/12/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared
import CachedAsyncImage

struct QueueItem: View {
    let queueItem: QueueItemState
    let event: (QueueSocketEvent) -> Void
    
    var body: some View {
        HStack() {
            StandardUserPicture(imageUrl: queueItem.profilePictureUri)
            
            VStack(alignment: .leading) {
                Text(queueItem.firstName)
                    .font(.headline)
                Text(queueItem.lastName)
                    .font(.subheadline)
            }
        }
        .swipeActions() {
            Button(role: .destructive) {
                event(
                    QueueSocketEvent.DeleteQueueItem(
                        queueItemId: queueItem.id
                    )
                )
            } label: {
                Label("Delete", systemImage: "trash.fill")
            }
        }
    }
}

struct QueueItem_Previews: PreviewProvider {
    static var previews: some View {
        QueueItem(
            queueItem: QueueItemState(
                id: "0",
                userId: "0",
                firstName: "Amal",
                lastName: "Last name",
                profilePictureUri: "",
                isLeftQueue: false,
                timestamp: 123123
            ),
            event: { event in }
        )
    }
}
