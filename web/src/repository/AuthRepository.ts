import User from '../model/User.ts'

export default interface AuthRepository {
    getUser(): Promise<User>
    logout(): Promise<void>
}

export class DefaultAuthRepository implements AuthRepository {
    async getUser(): Promise<User> {
        const url = '/api/users/me'
        const res = await fetch(url)
        if (!res.ok) throw Error("401")
        return await res.json()
    }

    async logout(): Promise<void> {
        const url = '/api/auth/logout'
        await fetch(url)
    }
}