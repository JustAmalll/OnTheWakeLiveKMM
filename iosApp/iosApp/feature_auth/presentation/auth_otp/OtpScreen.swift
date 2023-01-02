//
//  OtpScreem.swift
//  iosApp
//
//  Created by Amal Nuritdinkhodzhaev on 31/12/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct OtpScreen: View {
    
    @EnvironmentObject var otpModel: OtpViewModel
    
    var body: some View {
        Form {
            TextField("OTP", text: $otpModel.otp)
                .textContentType(.oneTimeCode)
                .keyboardType(.phonePad)
            
            Button {
                Task { await otpModel.verifyOtp() }
            } label: {
                Text("Verify")
            }
        }
        .navigationTitle("Verification")
    }
}

struct OtpScreem_Previews: PreviewProvider {
    static var previews: some View {
        OtpScreen()
    }
}
