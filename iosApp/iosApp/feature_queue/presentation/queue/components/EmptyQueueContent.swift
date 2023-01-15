//
//  EmptyQueueContent.swift
//  iosApp
//
//  Created by Amal Nuritdinkhodzhaev on 9/1/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct EmptyQueueContent: View {
    var body: some View {
        Spacer()
        VStack {
            LottieView(animationName: "cat")
                .frame(width: 250, height: 250)
            Text("There is no one in the queue")
                .font(.headline)
        }
        .padding(.bottom, 140)
        Spacer()
    }
}

struct EmptyQueueContentPreview: PreviewProvider {
    static var previews: some View {
        EmptyQueueContent()
    }
}
