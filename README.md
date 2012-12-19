
Jetty開発環境のサンプル

# パッケージ構成
ルートパッケージを\<root_package>と表記します。

## Web関連クラスのパッケージ
|パッケージ|配置するクラスの分類|
|---|---|
|\<root_package>.web|ServletAPIに近いクラス。WebInitializerクラスもここに置く|
|\<root_package>.web.rest|JAX-RSに関するクラス|
|\<root_package>.web.ui|画面を構成するクラス。Wicketの場合は各種Pageクラス|
|\<root_package>.web.ui.component|画面を構成する部品。Wicketの場合はPanelなどのUI部品|

## ビジネスロジッククラスのパッケージ
|パッケージ|配置するクラスの分類|
|---|---|
|\<root_package>.entity|エンティティクラス（データベースモデルをJavaクラスに写したクラス）を置く|
|\<root_package>.model|エンティティクラスよりも高級なデータモデルを置く|
|\<root_package>.service|ビジネスロジックのインターフェイスを置く|
|\<root_package>.service.impl|ビジネスロジックの実装クラスを置く|

## その他のパッケージ
|パッケージ|配置するクラスの分類|
|---|---|
|\<root_package>|グローバル設定値を置く。環境変数など。ただし、ここに属するクラス／変数などは最小限になるように注意するべき|
|\<root_package>.util|Serviceにするほどでないロジックを置く。例えば日付の表現形式を統一するためのユーティリティメソッドなど|

# 大事なクラス

## SampleWebInitializerクラス
いくつかある大事なクラスの中でも、このクラスが飛び抜けて大事です。

このクラスは```javax.servlet.ServletContextListener```インターフェイスをimplementsしています。これにより、Webアプリケーション起動時にこのクラスの```contextInitialized```メソッドがただ一度だけ呼び出されるようになります。

また、ServletAPI3.0になってから、ServletContextを通してコードからServletやFilterを登録することが出来るようになりました。

つまり```contextInitialized```メソッドの中でこの機能を利用すれば、web.xmlに頼らずにServletやFilterを登録することが出来るようになるわけです。

### web.xmlに頼らずに済むことの利点
web.xmlに頼らずJavaコードでServletやFilterを登録する利点は、型安全性です。web.xmlはクラス名を誤って記述してしまっても、Servletコンテナを起動するまでは気付けません。しかしJavaコードで登録する場合、工夫次第でクラス名を謝るなどということは起こらないようにすることが出来ます。


ただし、場合によってはweb.xmlを編集する必要が生じることに注意が必要です。例えばファイルの拡張子とMIMEタイプのマッピングは、web.xmlでないと登録できません。

## SampleRestApplicationクラス
JAX-RSを使うために必要です。
```javax.ws.rs.core.Application```クラスを継承します。

このクラスの主な役割は

1. リソースクラスの登録
1. 各種拡張クラスの登録

です。

なお、```SampleRestApplication```クラスをWebアプリケーションから使えるようにするには、前述の```SampleWebInitializer```クラスの中で```SampleRestApplication```クラスを登録する必要があります。

## SampleWicketAppliationクラス
Wicketを使うためのクラスです。
Wicketの詳しい説明は別項に譲りますが、日本語の情報は多いですし、メーリングリストも充実しているので手詰まりになることはまずないです。

なお```SampleWicketApplication```クラスをWebアプリケーションから使えるようにするには、前述の```SampleWebInitializer```クラスの中で```SampleWicketApplication```クラスを登録する必要があります。

# 各種設定ファイルについて
## pom.xml
このプロジェクトはMaven2というパッケージ管理システムの利用を前提に設定されています。
http://maven.apache.org

Maven2の唯一の設定ファイルが```pom.xml```です。
```pom.xml```にはプロジェクトが依存するライブラリやプロジェクトのディレクトリ構成が書かれています。

## Procfile
このファイルは、このプロジェクトをHerokuにデプロイする際に必要となるファイルです。HerokuというのはPaaS環境です。
http://www.heroku.com

Herokuを使わない場合はこのファイルは無視して下さい。

## .gitignore
ソースをGitで管理する場合に、管理対象外とするファイルのパターンを書くファイルです。
Gitを使わない場合はこのファイルは無視して下さい。

## 
