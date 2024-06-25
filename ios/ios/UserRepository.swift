import Foundation
import GoogleSignIn

class UserRepository {
    func getUsername(of user: GIDGoogleUser) async -> String? {
        guard let idToken = user.idToken?.tokenString else { return nil }
        var urlRequest = URLRequest(url: URL(string: "http://localhost:8080/auth/api/users/me")!)
        urlRequest.setValue("Bearer \(idToken)", forHTTPHeaderField: "Authorization")
        do {
            let (data, _) = try await URLSession.shared.data(for: urlRequest)
            let userResponse = try JSONDecoder().decode(UserResponse.self, from: data)
            return userResponse.name
        } catch(let error) {
            print(String(describing: error))
            return nil
        }
    }
}

struct UserResponse: Decodable {
    let name: String
}
