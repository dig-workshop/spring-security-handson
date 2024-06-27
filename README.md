# Spring Security Hands on

----
このリポジトリはDIG Immersive勉強会「認証・認可」でのハンズオンで使用するサンプルプロジェクトです。
ディレクトリ構成は以下になります。
```
security
├── server・・・・・・・・・・ Spring Boot + Spring Securityで作られたバックエンド
├── ios・・・・・・・・・・・・ iOSアプリ（シュミレーターのダウンロードをしなくても良いように実際はmacOSアプリ）
├── web・・・・・・・・・・・・ TypeScript + React(Vite)で作られたSPA
└── docker-compose.yml・・・ DBのコンテナを立ち上げるためのcomposeファイル
```
ハンズオンは合計4つで構成されており、ブランチを切り替えることで各ハンズオンのスタート地点・ゴール地点に移動できます。
mainブランチは4つのハンズオンが終了した後の完成系のコードです。
## アプリケーションを動かす

---

### DB
1. ターミナルで以下を実行します。5432ポートを使用するので、すでに稼働しているアプリケーションがある場合は停止してください。
```shell
$ docker compose up -d
```

### サーバー
各種環境変数を設定する必要があります。
1. `./server/.envSample`をコピーして`./server/.env`を作成してください。
2. `GOOGLE_CLIENT_ID`と`GOOGLE_CLIENT_SECRET`の値を入れる必要があります。
GoogleCloudコンソールでWebアプリ用のOAuth2.0クライアントIDを作成し、値を入力してください。
3. ターミナルで以下を実行してアプリを動作させます。
```shell
$ cd server
$ make start
```

### Web
1. ターミナルで以下を実行してください
```shell
$ cd web
$ make start
```

### iOS
1. GoogleCloudコンソールでiOSアプリ用のOAuth2.0クライアントIDを作成してください。
2. `./ios/ios/Info.plist`を作成し、以下のコードを貼り付けます。
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
	<key>GIDClientID</key>
	<string>{your_ios_client_id}</string>
    <key>CFBundleURLTypes</key>
    <array>
      <dict>
        <key>CFBundleURLSchemes</key>
        <array>
          <string>{dot_reversed_your_ios_client_id}</string>
        </array>
      </dict>
    </array>
    <key>GIDServerClientID</key>
    <string>{your_web_app_client_id}</string>
</dict>
</plist>

```
3. `your_ios_client_id`には手順1で作成したクライアントIDを、
`dot_reversed_your_ios_client_id`には手順1で作成したクライアントIDをドットで反転させた文字列を、
`your_web_app_client_id`にはWebアプリ用に作成したクライアントIDを、それぞれ書き換えてください。

ドットで反転させる例: `hoge-fuga.apps.googleusercontent.com` -> `com.googleusercontent.apps.hoge-fuga`
4. ターミナルで以下を実行し、XcodeでiOSプロジェクトを開きます。
```shell
$ cd ios
$ xed .
```
5. Xcodeを操作しアプリケーションを起動します。
