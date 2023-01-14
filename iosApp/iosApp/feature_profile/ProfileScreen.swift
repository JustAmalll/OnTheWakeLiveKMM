//
//  ProfileScreen.swift
//  iosApp
//
//  Created by Amal Nuritdinkhodzhaev on 13/1/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct ProfileScreen: View {
    var body: some View {
        NavigationView {
            VStack {
                Form {
                    Section {
                        HStack {
                            StandardUserPicture(imageUrl: "")
                            
                            VStack(alignment: .leading) {
                                Text("Amal")
                                    .font(.headline)
                                Text("Just Amalll")
                                    .font(.subheadline)
                            }
                        }
                    } header: {
                        Spacer()
                    }
                }
                .frame(height: 114)
                .disabled(true)
                
                VStack(alignment: .leading, spacing: 6) {
                    ProfileItem(
                        title: "Instagram",
                        subTitle: "Just_Amalll",
                        isLastItem: false
                    )
                    ProfileItem(
                        title: "Telegram",
                        subTitle: "JustAmal",
                        isLastItem: false
                    )
                    ProfileItem(
                        title: "Phone number",
                        subTitle: "+996 555 123 123",
                        isLastItem: false
                    )
                    ProfileItem(
                        title: "Date of Bitrh",
                        subTitle: "Not specified",
                        isLastItem: true
                    )
                }
                .padding(.horizontal, 24)
                .padding(.vertical)
            }
            .frame(
                maxWidth: .infinity,
                maxHeight: .infinity,
                alignment: .topLeading
            )
            .navigationTitle("Profile")
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button {
                        
                    } label: {
                        Image(systemName: "rectangle.portrait.and.arrow.right")
                    }
                }
            }
        }
    }
}

struct ProfileScreen_Previews: PreviewProvider {
    static var previews: some View {
        ProfileScreen()
    }
}
