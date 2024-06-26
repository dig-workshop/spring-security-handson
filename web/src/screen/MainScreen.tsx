import {useUser} from '../UserContext.ts'

export default function MainScreen(
) {
    const user = useUser()

    return (
        <>
            <div>main screen contents</div>
            <div>{`ログインユーザー名: ${user?.name}`}</div>
            <form action="http://localhost:8080/logout" method="post">
                <button type="submit">ログアウト</button>
            </form>
        </>
    )
}