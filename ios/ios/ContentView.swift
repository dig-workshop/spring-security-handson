import SwiftUI
import GoogleSignIn
import GoogleSignInSwift

struct UserResponse: Decodable {
    let name: String
    let accessToken: String
}

struct ContentView: View {
    @Binding var user: GIDGoogleUser?
    @State private var username: String = ""
    @State private var accessToken: String? = nil
    
    var body: some View {
        if let user = user {
            VStack {
                Text(String(describing: user.idToken?.tokenString))
                Text(username)
                Text("日記:")
                Diary(accessToken: $accessToken)
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
                    var urlRequest = URLRequest(url: URL(string: "http://localhost:8080/auth/api/users/me")!)
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


struct Diary: View {
    @Binding var accessToken: String?
    @State private var diary: [String] = []
    var body: some View {
        List(diary, id: \.self) { sentence in
            Text(sentence)
        }
        .task {
            guard let accessToken = accessToken else { return }
            Task {
                var urlRequest = URLRequest(url: URL(string: "http://localhost:8080/api/diary")!)
                urlRequest.setValue("Bearer \(accessToken)", forHTTPHeaderField: "Authorization")
                do {
                    let (data, _) = try await URLSession.shared.data(for: urlRequest)
                    let diary = try JSONDecoder().decode([String].self, from: data)
                    DispatchQueue.main.async {
                        self.diary = diary
                    }
                } catch(let error) {
                    print(String(describing: error))
                }
            }
        }
    }
}

#Preview {
    ContentView(user: .constant(nil))
}
