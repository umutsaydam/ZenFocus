<div align="center">
  <span><a href="#uygulama-icerik">TR</a></span>
  <span><a href="#app-content">EN</a></span>
</div>

<div align="center">
<a href="https://play.google.com/store/apps/details?id=com.umutsaydam.zenfocus" rel="nofollow">
<img src="https://camo.githubusercontent.com/1ddf90e524a4bfe8b77f9a6902d54fc708380389b7e0d7f9ad29196a799e77db/68747470733a2f2f706c61792e676f6f676c652e636f6d2f696e746c2f656e5f75732f6261646765732f696d616765732f67656e657269632f656e2d706c61792d62616467652e706e67" alt="Get it on Google Play" height="80" data-canonical-src="https://play.google.com/intl/en_us/badges/images/generic/en-play-badge.png" style="max-width: 100%;"></a>
</div>

# <p id="uygulama-icerik">ZenFocus</p>

ZenFocus ile üretkenliğinizi en üst seviyeye taşıyın! Pomodoro tekniği ve yapılacaklar listesiyle zamanınızı etkili bir şekilde yönetin. Kullanıcı dostu ara yüzüyle odaklanma deneyiminizi kişiselleştirin: istediğiniz temayı seçerek tam odak modunu aktif hale getirin ve motivasyonunuzu artırın. Ayrıca, favori odak seslerinizi seçerek çalışma sürecinizi daha keyifli ve verimli hale getirin. ZenFocus, konsantrasyonunuzu artırırken hedeflerinize ulaşmanız için en iyi yol arkadaşı olacak.

# 📋 İçindekiler

- [Özellikler](#özellikler)
- [Kullanılan Teknolojiler](#kullanilan-teknolojiler)
- [Kurulum](#kurulum)
- [Kullanım](#kullanim)
- [Katkıda Bulunma](#katkida-bulunma)
- [Lisans](#lisans)

---

## <p id="özellikler">🚀 Özellikler</p>

- Odak sürelerini belirleme ve yönetme.
- Görev takibi.
- Kullanıcı dostu ve minimal tasarım.
- Kullanıcıya özel tam odak modunda kullanabileceği temalar.
- Birçok odak sesleri
---

## <p id="kullanilan-teknolojiler">🛠 Kullanılan Teknolojiler</p>

- **Kotlin**: Uygulama programlama dili.
- **Jetpack Compose**: Kullanıcı arayüzü oluşturmak için modern Android araç takımı.
- **Navigation Component**: Sayfa geçişlerinin yönetimi.
- **Room Database**: Lokal veritabanı işlemlerin gerçekleştirilmesi.
- **Data Store**: Anahtar/değer çiftlerini veya türlenmiş nesneleri depolanmasını sağlayan bir veri depolama çözümüdür.
- **Dagger Hilt Dependency Injection (Bağımlılık Enjeksiyonu)**: Hilt, bağımlılıkları merkezi bir yerde tanımlayıp yöneterek kod tekrarını azaltır ve kodun okunabilirliğini artırır. Aynı zamanda test edilebilirliği kolaylaştırır ve uygulamanın daha modüler bir yapıya kavuşmasını sağlar.
- **Coroutines**: işlemleri ana iş parcacığından (main thread) ayırarak arka planda çalışmasını sağlar. Bu sayede uzun süren işlemler (ağ istekleri, veritabanı işlemleri vs.) sırasında uygulamanın kullanıcı arayüzünün donmasını engeller.
- **Aws Amplify**: Bulut tabanlı uygulamalar geliştirmek için kullanılan güçlü bir araç setidir. Kullanıcı kimlik doğrulama, veri depolama, dosya yükleme, API oluşturma ve barındırma gibi işlemleri kolaylaştırır.
- **Cognito Auth**: Amazon Cognito, kullanıcı kimlik doğrulama, yetkilendirme ve kullanıcı yönetimi için bulut tabanlı bir çözümdür.
- **S3 Storage**: Amazon S3 (Simple Storage Service), verilerin güvenli, ölçeklenebilir ve yüksek erişilebilir bir şekilde depolanmasını sağlayan bulut tabanlı bir hizmettir.
- **Amazon DynamoDB**: Amazon DynamoDB, tamamen yönetilen, yüksek performanslı ve ölçeklenebilir bir NoSQL veritabanı hizmetidir.
- **Amazon API Gateway**: RESTful API'ler ve WebSocket API'leri oluşturmak, yönetmek ve güvenli hale getirmek için kullanılan tamamen yönetilen bir hizmettir.
- **Coil**: Android uygulamalarında görsel yükleme işlemlerini basitleştiren ve hızlandıran bir kütüphanedir. Görsellerin önbelleğe alınması, dönüşüm yapılması ve yerel dosyalarla entegrasyon gibi işlemleri verimli bir şekilde yönetir.
- **Google Credentials Play Services**: Android cihazlarda Google hizmetlerine erişimi yöneten bir API setidir. Google Credentials ise bu hizmetler üzerinden kullanıcı kimlik doğrulaması, sosyal medya hesaplarıyla giriş, oturum yönetimi ve güvenlik gibi işlemleri kolaylaştırır.
- **Google Mobile Ads SDK**: Android ve iOS uygulamaları için mobil reklam gösterimini sağlayan bir yazılım geliştirme kitidir.
- **Google Billing**: Android uygulamalarında uygulama içi satın alımlar (in-app purchases) ve abonelikler gibi ödeme işlemlerini yönetmek için kullanılan bir hizmettir.
- **Splash Screen**: Uygulamanın başlatılma sürecinde kullanıcıya görsel bir geçiş sağlar ve uygulamanın daha hızlı açılabilmesi için arka planda işlemler yapılırken kullanıcıyı bilgilendirir.
- **Notification**: Kullanıcılara uygulama hakkında önemli bilgiler, güncellemeler veya hatırlatıcılar göndermeyi sağlayan bir mekanizmadır.
- **Foreground Service**: Android uygulamalarında, uygulama kullanıcının görsel olarak etkileşimde olmasa bile sürekli bir işlem gerektiren durumlar için kullanılan bir servis türüdür.

---

## <p id="kurulum">📦 Kurulum</p>

1. Bu projeyi klonlayın (forContribution branch olduğundan emin olun):
    ```bash
     git clone --branch forContribution https://github.com/umutsaydam/ZenFocus.git
    ```
2. Android Studio'da açın.
3. Gerekli bağımlılıkları yüklemek için `Gradle Sync` işlemini gerçekleştirin.
4. Uygulamayı çalıştırın.

---

## <p id="kullanim">🧑‍💻 Kullanım</p>

- ![zenfocus_screens_tr](https://github.com/user-attachments/assets/2786a5aa-43d8-4e91-9fad-0ecd5292a4cb)

---

## <p id="katkida-bulunma">🤝 Katkıda Bulunma</p>

Katkıda bulunmak istiyorsanız, lütfen bir `pull request` gönderin veya bir `issue` açın. Katkılarınız memnuniyetle karşılanacaktır! (forContribution branch'inde olduğundan emin olun.)

---

## <p id="lisans">📜 Lisans</p>

Bilgi için `LICENSE` dosyasına göz atabilirsiniz.

---


# <p id="app-content">ZenFocus</p>

Maximize your productivity with ZenFocus! Manage your time effectively with the Pomodoro technique and to-do lists. Personalize your focus experience with the user-friendly interface: activate the focus mode by selecting your preferred theme and boost your motivation. Additionally, make your work process more enjoyable and efficient by selecting your favorite focus sounds. ZenFocus will be the best companion to help you stay focused and reach your goals.

# 📋 Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Installation](#installation)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

---

## <p id="features">🚀 Features</p>

- Set and manage focus durations.
- Task tracking.
- User-friendly and minimal design.
- Customizable focus modes with personalized themes.
- Multiple focus sounds.
---

## <p id="technologies-used">🛠 Technologies Used</p>

- **Kotlin**: Programming language for the application.
- **Jetpack Compose**: A modern toolkit for building user interfaces on Android.
- **Navigation Component**: Manages page transitions.
- **Room Database**: Local database operations.
- **Data Store**: A data storage solution for storing key-value pairs or typed objects.
- **Dagger Hilt Dependency Injection**: Hilt reduces code repetition and increases readability by centralizing dependency management. It also facilitates easier testing and promotes a more modular architecture.
- **Coroutines**: Runs tasks asynchronously, off the main thread. This helps prevent UI freezing during long-running operations such as network requests or database operations.
- **Aws Amplify**: A powerful toolset for building cloud-powered applications. It simplifies user authentication, data storage, file uploads, API creation, and hosting.
- **Cognito Auth**: Amazon Cognito is a cloud-based solution for user authentication, authorization, and user management.
- **S3 Storage**: Amazon S3 (Simple Storage Service) is a cloud-based service for securely storing data in a scalable and highly accessible manner.
- **Amazon DynamoDB**: Amazon DynamoDB is a fully managed, high-performance, and scalable NoSQL database service.
- **Amazon API Gateway**: A fully managed service for creating, managing, and securing RESTful and WebSocket APIs.
- **Coil**: A library for simplifying and speeding up image loading in Android applications. It efficiently handles image caching, transformations, and integration with local files.
- **Google Credentials Play Services**: A set of APIs that manage access to Google services on Android devices, including user authentication, social media logins, session management, and security features.
- **Google Mobile Ads SDK**: A software development kit that enables mobile ads to be displayed in Android and iOS applications.
- **Google Billing**: A service for managing in-app purchases and subscriptions in Android applications.
- **Splash Screen**: Provides a visual transition during the app launch process, informing the user while background operations are being performed to allow faster app startup.
- **Notification**: A mechanism that allows sending important updates, information, or reminders to users about the app.
- **Foreground Service**:  A service used in Android applications that requires continuous operation even when the user is not interacting with the app visually.

---

## <p id="installation">📦 Installation</p>

1. Clone this project (Make sure you are on the forContribution branch):
    ```bash
     git clone --branch forContribution https://github.com/umutsaydam/ZenFocus.git
    ```
2. Open it in Android Studio.
3. Perform a `Gradle Sync` to download the necessary dependencies.
4. Run the app.

---

## <p id="usage">🧑‍💻 Usage</p>

- ![Image](https://github.com/user-attachments/assets/0065401c-ce55-443a-a08f-248d08fe8eeb)

---

## <p id="contributing">🤝 Contributing</p>

If you'd like to contribute, please submit a `pull request` or open an `issue`. Your contributions are welcome! (Make sure you are on the forContribution branch)

---

## <p id="license">📜 License</p>

Please, see the `LICENSE` file.

