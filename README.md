# 🛡️ Spring Security Mernis Authentication

Bu proje, Türkiye Cumhuriyeti vatandaşlarının kimlik doğrulamasını sağlamak amacıyla MERNIS Web Servisi ile entegre çalışan ve **JWT tabanlı güvenli oturum yönetimi sağlayan özel bir Spring Security** yapısı içerir.
Kullanıcı adı ve şifreye ek olarak **TCKN, ad, soyad ve doğum yılı** gibi bilgilerle doğrulama yapılır, ardından başarılı girişlerde JWT token üretilir.

---


📁 Projeyi Çalıştırmak
> git clone [https://github.com/orhanturkmenoglu/spring-security-mernis-auth.git](https://github.com/orhanturkmenoglu/spring-security-mernis-auth.git)

> cd spring-security-mernis-auth

> mvn clean install


## 🚀 Özellikler

- ✅ **JWT Authentication:** Kullanıcı kimlik doğrulaması sonrası güvenli erişim için JWT token üretimi
- 🛡️ **Method Düzeyinde Güvenlik:** Spring Security ile method düzeyinde erişim kontrolü sağlanır.
- 💾 **JWT Token Cache:** Token bilgileri Redis benzeri bir yapı ile cache'te tutulur
- 🔐 **MERNIS Entegrasyonu:** Kullanıcının girdiği kimlik bilgileri MERNIS servisi üzerinden doğrulanır.
- 📦 **Layered Architecture:** Clean Code prensiplerine uygun, servis ve config katmanlarına ayrılmış yapı.
- 🧪 **Test Edilebilirlik:** Kolayca birim testi yapılabilir şekilde esnek tasarım.

---

## 🧠 Nasıl Çalışır?
1.Kullanıcı, tckn, ad, soyad, doğum yılı, kullanıcı adı ve şifre bilgilerini göndererek kayıt olur.

2.AuthService, MERNIS servisi aracılığıyla kimlik bilgilerini doğrular.

3.Geçerli kimlik bilgileri ve kullanıcı adı kontrolü sonrası kullanıcı veritabanına kaydedilir.

4.Giriş yapılırken, kullanıcı adı ve şifre doğrulanır, başarılıysa JWT Access ve Refresh Token oluşturulur.

5.Token’lar JwtTokenCacheService üzerinden cache’e alınır.

6.Korunan endpoint’lere erişim, sadece geçerli access token ile sağlanır.

7.refreshAccessToken() metodu ile refresh token kullanılarak yeni access token alınabilir.

8.logout() ile token kara listeye alınır ve oturum kapatılır.


---

## 🛠️ Kullanılan Teknolojiler

- Java 17
- Spring Boot
- Spring Security
- SOAP WebService (MERNIS)
- Lombok
- Maven

---

## 📷 Örnek Kullanıcı Girişi

```json
{
  "tckn": "10000000146",
  "username": "orhanturkmen"
  "firstName": "Orhan",
  "lastName": "Türkmenoğlu",
  "birthYear": 1999,
  "password": "123456"
}

🛡️ Başarılı giriş sonrası dönen örnek token:
``json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}


💡 Kimler İçin Faydalı?
📚 Spring Security ve JWT öğrenmek isteyen geliştiriciler

🧾 Gerçek kimlik doğrulama servisleriyle çalışan backend developer’lar

🧪 Custom Authentication & Token yapısı geliştirmek isteyenler

🇹🇷 Türkiye projelerinde TCKN doğrulama ihtiyacı duyanlar


✨ Katkıda Bulun
Pull request'ler ve issue'lar her zaman memnuniyetle karşılanır. 👨‍💻

📬 İletişim
GitHub: orhanturkmenoglu

LinkedIn: Orhan Türkmenoğlu



