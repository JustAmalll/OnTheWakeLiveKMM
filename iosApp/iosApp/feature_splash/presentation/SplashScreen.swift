//
//  SplashScreen.swift
//  iosApp
//
//  Created by Amal Nuritdinkhodzhaev on 3/1/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct SplashScreen: View {
    
    @EnvironmentObject var splashViewModel: IOSSplashViewModel
        
    var body: some View {
        VStack {
            Image("onthewake_logo_black")
                .resizable()
                .scaledToFit()
                .frame(width: 260, height: 260)
        }
        .onAppear {
            splashViewModel.startObserving()
            
            DispatchQueue.main.asyncAfter(deadline: .now() + 1.0) {
                splashViewModel.isSplashScreenShowing = false
            }
        }
        .onDisappear {
            splashViewModel.dispose()
        }
    }
}
