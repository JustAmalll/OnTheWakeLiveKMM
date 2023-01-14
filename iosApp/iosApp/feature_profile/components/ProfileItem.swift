//
//  ProfileItem.swift
//  iosApp
//
//  Created by Amal Nuritdinkhodzhaev on 14/1/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct ProfileItem: View {
    
    let title: String
    let subTitle: String?
    let isLastItem: Bool
    
    var body: some View {
        Text(title)
            .font(.system(size: 22, weight: .medium))
        
        Text(formatedSubTitle(subTitle: subTitle))
            .font(.system(size: 18, weight: .regular))
        
        if !isLastItem {
            Divider().padding(.vertical, 10)
        }
    }
    
    func formatedSubTitle(subTitle: String?) -> String {
        guard let subTitle = subTitle else { return "Not specified" }
        if subTitle.isEmpty { return "Not specified" }
        else { return subTitle }
    }
}

struct ProfileItem_Previews: PreviewProvider {
    static var previews: some View {
        ProfileItem(
            title: "Phone number",
            subTitle: "+996 555 123 123",
            isLastItem: false
        )
    }
}