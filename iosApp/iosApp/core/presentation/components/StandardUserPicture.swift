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
        
        if let url = imageUrl, !url.isEmpty {
            CachedAsyncImage(url: URL(string: url)) { phase in
                switch phase {
                case .success(let image):
                    image
                        .resizable()
                        .frame(width: 42, height: 42)
                        .scaledToFit()
                        .clipShape(Circle())
                case .empty:
                    progressPlaceHolder
                case .failure:
                    placeholder
                default:
                    placeholder
                }
            }
        } else {
            placeholder
        }
    }
    
    var placeholder: some View {
        ZStack {
            Circle()
                .foregroundColor(.gray)
            Image(systemName: "person.fill")
                .foregroundColor(Color.white)
        }
        .frame(width: 42, height: 42)
    }
    
    var progressPlaceHolder: some View {
        ZStack {
            Circle()
                .foregroundColor(.gray)
            ProgressView()
                .tint(Color.white)
        }
        .frame(width: 42, height: 42)
    }
}

struct StandardUserPicture_Previews: PreviewProvider {
    static var previews: some View {
        StandardUserPicture(imageUrl: "")
    }
}
