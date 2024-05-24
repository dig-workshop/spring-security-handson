import UserProvider from './components/UserProvider.tsx'
import Authorized from './components/Authorized.tsx'
import MainScreen from './screen/MainScreen.tsx'
import Unauthorized from './components/Unauthorized.tsx'
import LoginScreen from './screen/LoginScreen.tsx'
import {DefaultAuthRepository} from './repository/AuthRepository.ts'

const authRepository = new DefaultAuthRepository()

export default function App() {
    return (
        <UserProvider authRepository={authRepository}>
            <Authorized>
                <MainScreen authRepository={authRepository}/>
            </Authorized>
            <Unauthorized>
                <LoginScreen/>
            </Unauthorized>
        </UserProvider>
    )
}


