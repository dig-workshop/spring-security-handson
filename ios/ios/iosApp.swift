import SwiftUI
import GoogleSignIn

@main
struct iosApp: App {
    @State private var username: String? = nil

    let userRepository = UserRepository()
    
    var body: some Scene {
        WindowGroup {
            ContentView(username: $username, userRepository: userRepository)
                .onOpenURL { url in
                  GIDSignIn.sharedInstance.handle(url)
                }
                .onAppear {
                    GIDSignIn.sharedInstance.restorePreviousSignIn { maybeUser, error in
                        if error != nil { return }
                        guard let user = maybeUser else { return }
                        Task {
                            let username = await self.userRepository.getUsername(of: user)
                            DispatchQueue.main.async {
                                self.username = username
                            }
                        }
                    }
                }
        }
    }
}
