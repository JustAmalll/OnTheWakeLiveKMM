//
//  PasswordTextField.swift
//  iosApp
//
//  Created by Amal Nuritdinkhodzhaev on 31/12/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct PasswordTextField: View {
    
    @Binding var password: String
    @State private var isSecure: Bool = true
    
    var body: some View {
        HStack {
            Group {
                if isSecure {
                    SecureField("Password", text: $password)
                } else {
                    TextField("Password", text: $password)
                        .textContentType(.password)
                        .autocapitalization(.none)
                }
            }
            Button(action: { isSecure.toggle() }) {
                Image(systemName: !isSecure ? "eye.slash" : "eye" )
            }
        }
    }
}

struct PasswordTextField_Previews: PreviewProvider {
    static var previews: some View {
        PasswordTextField(
            password: Binding(get: { "password" }, set: { value in })
        )
    }
}
