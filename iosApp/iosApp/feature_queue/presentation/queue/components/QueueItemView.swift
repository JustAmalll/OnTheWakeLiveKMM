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

struct QueueItemView: View {
    let queueItem: QueueItem
    let userId: String
    let onSwipeToDelete: (String) -> Void
    
    var queueItemContent: some View {
        NavigationLink(
            destination: QueueItemDetailsScreen(
                queueItemId: queueItem.id
            )
        ) {
            HStack {
                StandardUserPicture(imageUrl: queueItem.profilePictureUri)
                
                VStack(alignment: .leading) {
                    Text(queueItem.firstName)
                        .font(.headline)
                    Text(queueItem.lastName)
                        .font(.subheadline)
                }
            }
        }
    }
    
    var body: some View {
        
        let isUserAdmin = Constants().ADMIN_IDS.contains(userId)
        let isAdminQueueItem = Constants().ADMIN_IDS.contains(queueItem.userId)
        let isOwnQueueItem = queueItem.userId == userId
        
        if isOwnQueueItem || isUserAdmin {
            if isAdminQueueItem {
                Text(queueItem.firstName).swipeToDelete {
                    onSwipeToDelete(queueItem.id)
                }
            } else {
                queueItemContent.swipeToDelete {
                    onSwipeToDelete(queueItem.id)
                }
            }
        } else {
            if isAdminQueueItem {
                Text(queueItem.firstName)
            } else {
                queueItemContent
            }
        }
    }
}

extension View {
    func swipeToDelete(onSwiped: @escaping() -> Void) -> some View {
        self.swipeActions {
            Button(role: .destructive) {
                onSwiped()
            } label: {
                Label("Delete", systemImage: "trash.fill")
            }
        }
        .tint(.red)
    }
}

struct QueueItem_Previews: PreviewProvider {
    static var previews: some View {
        QueueItemView(
            queueItem: QueueItem(
                id: "0",
                userId: "0",
                firstName: "Amal",
                lastName: "Last name",
                profilePictureUri: "",
                isLeftQueue: false,
                timestamp: 123123
            ),
            userId: "",
            onSwipeToDelete: { queueItemId in }
        )
    }
}
