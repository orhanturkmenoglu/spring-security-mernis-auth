# Spring Security Mernis Authentication

Bu proje, Türkiye Cumhuriyeti vatandaşlarının kimlik doğrulamasını sağlamak amacıyla MERNIS Web Servisi ile entegre çalışan ve **JWT tabanlı güvenli oturum yönetimi sağlayan özel bir Spring Security** yapısı içerir.
Kullanıcı adı ve şifreye ek olarak **TCKN, ad, soyad ve doğum yılı** gibi bilgilerle doğrulama yapılır, ardından başarılı girişlerde JWT token üretilir.

---


📁 Projeyi Çalıştırmak
> git clone [https://github.com/orhanturkmenoglu/spring-security-mernis-auth.git](https://github.com/orhanturkmenoglu/spring-security-mernis-auth.git)

> cd spring-security-mernis-auth

> mvn clean install


## 🚀 Özellikler

- ✅ **JWT Authentication:** Kullanıcı kimlik doğrulaması sonrası güvenli erişim için JWT token üretimi
- 🔐 **MERNIS Entegrasyonu:** Kullanıcının girdiği kimlik bilgileri MERNIS servisi üzerinden doğrulanır.
- 📦 **Layered Architecture:** Clean Code prensiplerine uygun, servis ve config katmanlarına ayrılmış yapı.
- 🧪 Test Edilebilirlik:** Kolayca birim testi yapılabilir şekilde esnek tasarım.

---

## 🧠 Nasıl Çalışır?
1.Kullanıcı, giriş formuna tckn, ad, soyad, doğum yılı, kullanıcı adı ve şifre bilgilerini girer.

2.Bu bilgiler CustomAuthenticationProvider üzerinden alınır ve MernisService ile kimlik doğrulaması yapılır.

3.MERNIS doğrulaması başarılıysa kullanıcı bilgileri veritabanında kontrol edilir.

4.Kullanıcı adı, şifre ve rol doğrulaması geçerli ise bir JWT token oluşturulur ve kullanıcıya döndürülür.

5.Kullanıcı sistemdeki tüm korumalı endpoint’lere bu token ile erişim sağlayabilir.


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
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
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



