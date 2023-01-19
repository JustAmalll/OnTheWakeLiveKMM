//
//  PhotoPicker.swift
//  iosApp
//
//  Created by Amal Nuritdinkhodzhaev on 16/1/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct PhotoPicker: UIViewControllerRepresentable {
    
    @Binding var avatarImage: UIImage?
    
    let onError: (String) -> Void
    
    func makeUIViewController(context: Context) -> UIImagePickerController {
        let picker = UIImagePickerController()
        picker.delegate = context.coordinator
        picker.allowsEditing = true
        return picker
    }
    
    func updateUIViewController(
        _ uiViewController: UIImagePickerController,
        context: Context
    ) {}
    
    func makeCoordinator() -> Coordinator {
        return Coordinator(photoPicker: self, onError: onError)
    }
    
    final class Coordinator: NSObject, UINavigationControllerDelegate, UIImagePickerControllerDelegate {
        
        let photoPicker: PhotoPicker
        let onError: (String) -> Void
        
        init(photoPicker: PhotoPicker, onError: @escaping (String) -> Void) {
            self.photoPicker = photoPicker
            self.onError = onError
        }
        
        func imagePickerController(
            _ picker: UIImagePickerController,
            didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]
        ) {
            if let image = info[.editedImage] as? UIImage {
                photoPicker.avatarImage = image
            } else {
                self.onError("Unknown error")
            }
            picker.dismiss(animated: true)
        }
    }
}
