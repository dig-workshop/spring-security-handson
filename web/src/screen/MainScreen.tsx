import {useUser} from '../UserContext.ts'
import {useEffect, useState} from 'react'

export default function MainScreen(
) {
    const user = useUser()
    const [diary, setDiary] = useState<string[]>([])

    useEffect(() => {
        const accessToken = user?.accessToken
        if (accessToken === undefined) return
        fetch('/api/diary', {headers: {'Authorization': `Bearer ${accessToken}`}})
            .then(res => res.json())
            .then(diary => setDiary(diary))
    }, [user])

    return (
        <>
            <div>main screen contents</div>
            <div>{`ログインユーザー名: ${user?.name}`}</div>
            <div>日記</div>
            {diary.map(sentence => (
                <div id={window.crypto.randomUUID()}>{sentence}</div>
            ))}
            <form action="http://localhost:8080/logout" method="post">
                <button type="submit">ログアウト</button>
            </form>
        </>
    )
}