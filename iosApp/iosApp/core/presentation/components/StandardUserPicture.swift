//
//  StandardUserPicture.swift
//  iosApp
//
//  Created by Amal Nuritdinkhodzhaev on 9/1/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import CachedAsyncImage

struct StandardUserPicture: View {
    
    let imageUrl: String?
    
    var body: some View {
        
        CachedAsyncImage(
            url: URL(string: imageUrl ?? "")
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
                    Image(systemName: "person.fill")
                        .foregroundColor(Color.white)
                }
                .frame(width: 40, height: 40)
            }
        }
        .padding(.trailing, 4)
    }
}

struct StandardUserPicture_Previews: PreviewProvider {
    static var previews: some View {
        StandardUserPicture(imageUrl: "")
    }
}
