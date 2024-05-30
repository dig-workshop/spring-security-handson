import SwiftUI
import GoogleSignIn

@main
struct iosApp: App {
    @State private var user: GIDGoogleUser? = nil
    
    var body: some Scene {
        WindowGroup {
            ContentView(user: $user)
                .onOpenURL { url in
                  GIDSignIn.sharedInstance.handle(url)
                }
                .onAppear {
                    GIDSignIn.sharedInstance.restorePreviousSignIn { user, error in
                        if error != nil { return }
                        self.user = user
                    }
                }
        }
    }
}
