# Spring Security Mernis Authentication

Bu proje, Türkiye Cumhuriyeti vatandaşlarının kimlik doğrulamasını sağlamak amacıyla **MERNIS Web Servisi** ile entegre çalışan özel bir **Spring Security Custom Authentication Provider** içerir. Kullanıcı adı ve şifreye ek olarak **TCKN, ad, soyad ve doğum yılı** gibi bilgilerle doğrulama işlemi yapılır.

---


📁 Projeyi Çalıştırmak
> git clone https://github.com/orhanturkmenoglu/spring-security-mernis-auth.git
> cd spring-security-mernis-auth
> mvn clean install


## 🚀 Özellikler

- ✅ **Custom Authentication Provider:** Spring Security'nin standart yapısını genişleterek özelleştirilmiş doğrulama mekanizması.
- 🔐 **MERNIS Entegrasyonu:** Kullanıcının girdiği kimlik bilgileri MERNIS servisi üzerinden doğrulanır.
- 📦 **Layered Architecture:** Clean Code prensiplerine uygun, servis ve config katmanlarına ayrılmış yapı.
- 🧪 Test Edilebilirlik:** Kolayca birim testi yapılabilir şekilde esnek tasarım.

---

## 🧠 Nasıl Çalışır?

1. Kullanıcı giriş formuna TCKN, ad, soyad, doğum yılı ve şifre girer.
2. `CustomAuthenticationProvider`, bu bilgileri alarak `MernisService` üzerinden TC kimlik numarasının geçerliliğini kontrol eder.
3. MERNIS doğrulaması başarılıysa, sistem kullanıcıya yetki verir ve giriş işlemi tamamlanır.

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
  "firstName": "Orhan",
  "lastName": "Türkmenoğlu",
  "birthYear": 1999,
  "password": "123456"
}

💡 Kimler İçin Faydalı?
📚 Spring Security öğrenen geliştiriciler

🧾 Gerçek kimlik doğrulama servisleriyle proje geliştirenler

🧪 Custom Authentication yazmak isteyen backend developer'lar

🇹🇷 Türkiye'deki projelerde TCKN doğrulaması ihtiyacı duyanlar


✨ Katkıda Bulun
Pull request'ler ve issue'lar her zaman memnuniyetle karşılanır. 👨‍💻

📬 İletişim
GitHub: orhanturkmenoglu

LinkedIn: Orhan Türkmenoğlu



