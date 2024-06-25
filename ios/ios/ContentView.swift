import SwiftUI
import GoogleSignIn
import GoogleSignInSwift


struct ContentView: View {
    @Binding var username: String?
    let userRepository: UserRepository
    
    var body: some View {
        if let username = username {
            VStack {
                Text(username)
                Button {
                    signout()
                } label: {
                    Text("サインアウト")
                }
            }
            .padding()
        } else {
            GoogleSignInButton(action: handleSignInButton)
        }
    }
    
    private func handleSignInButton() {
        guard let presentingWindow = NSApplication.shared.windows.first else { return }
        GIDSignIn.sharedInstance.signIn(withPresenting: presentingWindow) { signinResult, error in
            guard let result = signinResult else { return }
            Task {
                await username = self.userRepository.getUsername(of: result.user)
                DispatchQueue.main.async {
                    self.username = username
                }
            }
        }
    }
    
    private func signout() {
        GIDSignIn.sharedInstance.signOut()
        username = nil
    }
}

