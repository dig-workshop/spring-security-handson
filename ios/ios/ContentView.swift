import SwiftUI
import GoogleSignIn
import GoogleSignInSwift

struct UserResponse: Decodable {
    let name: String
}

struct ContentView: View {
    @Binding var user: GIDGoogleUser?
    @State private var username: String = ""
    
    var body: some View {
        if let user = user {
            VStack {
                Text(String(describing: user.idToken?.tokenString))
                Text(username)
                Button {
                    signout()
                } label: {
                    Text("サインアウト")
                }
            }
            .padding()
            .task {
                guard let idToken = user.idToken?.tokenString else { return }
                Task {
                    var urlRequest = URLRequest(url: URL(string: "http://localhost:8080/api/users/me")!)
                    urlRequest.setValue("Bearer \(idToken)", forHTTPHeaderField: "Authorization")
                    do {
                        let (data, _) = try await URLSession.shared.data(for: urlRequest)
                        let userResponse = try JSONDecoder().decode(UserResponse.self, from: data)
                        DispatchQueue.main.async {
                            username = userResponse.name
                        }
                    } catch(let error) {
                        print(String(describing: error))
                    }
                }
            }
        } else {
            GoogleSignInButton(action: handleSignInButton)
        }
    }
    
    private func handleSignInButton() {
        guard let presentingWindow = NSApplication.shared.windows.first else { return }
        GIDSignIn.sharedInstance.signIn(withPresenting: presentingWindow) { signinResult, error in
            guard let result = signinResult else { return }
            self.user = result.user
        }
    }
    
    private func signout() {
        GIDSignIn.sharedInstance.signOut()
        user = nil
    }
}

#Preview {
    ContentView(user: .constant(nil))
}
