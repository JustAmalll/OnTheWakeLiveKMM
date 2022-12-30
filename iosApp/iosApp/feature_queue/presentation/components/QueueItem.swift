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
            CachedAsyncImage(
                url: URL(string: queueItem.profilePictureUri)
            ) { phase in
                
                if let image = phase.image {
                    image
                        .resizable()
                        .frame(width: 40, height: 40)
                        .scaledToFit()
                        .clipShape(Circle())
                } else {
                    ZStack {
                        Circle()
                            .foregroundColor(.gray)
                        Image(systemName: "person")
                            .foregroundColor(Color.white)
                    }
                    .frame(width: 40, height: 40)
                }
            }
            .padding(.trailing, 4)
            
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
