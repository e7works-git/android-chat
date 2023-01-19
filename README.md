![License](https://img.shields.io/badge/License-MIT-informational)
![Platform](https://img.shields.io/badge/platform-ANDROID-informational)
![Languages](https://img.shields.io/badge/language-JAVA-informational)

## 시작하기

Android 용으로 만들어진 샘플코드 입니다.
코드를 다운로드 받아서 안드로이드 빌드를 통해 앱을 실행해 보세요.

## 시작 전 체크사항

해당 안드로이드를 빌드 하기 위해서는 아래 요건이 필요 합니다.

### 요건

 - 안드로이드 스튜디오
 - Android 8.0(API 레벨 28) 이상
 - Java 8 이상
 - Androidx 사용
 - Android Gradle 플러그인 4.0.0 이상
 - Vchatcloud 1.0.0 이상 라이브러리 적용

### 프로젝트 생성

1. **Welcome to Android Studio** 창에서 **Start a new Android Studio project**를 클릭합니다.
2. [*Select a Project Template*]창에서 [*Empty Activity*]를 선택하고 [*Next*]을 클릭합니다.
3. [*Configure your project*]창의 [*Name*]필드에 프로젝트명을 입력합니다.
4. **Language** 드롭다운 메뉴에서 언어를 **Java** 또는 **Kotlin**로 선택합니다.
5. 'Androidx 사용'을 활성화합니다.* " 」
6. 최소 API 레벨을 28 이상으로 선택합니다.

### Android 용 sdk 적용

적용하기 위해서는 build.gradle 파일에 수정이 필요합니다. 아래 코드라인을 확인 후 해당 라인을 추가 해주세요.

root 폴더에 있는 build.gradle 파일을 열어 maven 주소를 추가해주세요.

```gradle
allprojects {
    
    maven { url "https://vchatcloud.com/maven" }
}
```

만약 그래들 6.8 버전보다 높은 것을 사용하신다면, setting.gradle 파일을 열어 maven 주소를 추가해주세요.

```gradle
dependencyResolutionManagement {
    repositories {
        maven { url "https://vchatcloud.com/maven" }
    }
}
```