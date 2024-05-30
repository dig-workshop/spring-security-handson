import User from '../model/User.ts'

export default interface AuthRepository {
    getUser(): Promise<User>
}

export class DefaultAuthRepository implements AuthRepository {
    async getUser(): Promise<User> {
        const url = '/auth/api/users/me'
        const res = await fetch(url)
        if (!res.ok) throw Error("401")
        return await res.json()
    }
}