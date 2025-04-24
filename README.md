# 🛡️ Spring Security Mernis Authentication

Bu proje, Türkiye Cumhuriyeti vatandaşlarının kimlik doğrulamasını sağlamak amacıyla MERNIS Web Servisi ile entegre çalışan ve **JWT tabanlı güvenli oturum yönetimi sağlayan özel bir Spring Security** yapısı içerir.
Kullanıcı adı ve şifreye ek olarak **TCKN, ad, soyad ve doğum yılı** gibi bilgilerle doğrulama yapılır, ardından başarılı girişlerde JWT token üretilir.

---


## 📁 Projeyi Çalıştırmak

Projeyi lokal ortamınızda çalıştırmak için aşağıdaki adımları izleyin:

git clone https://github.com/orhanturkmenoglu/spring-security-mernis-auth.git

cd spring-security-mernis-auth

mvn clean install

Redis servisini başlatmadan uygulamayı çalıştırmadan önce aşağıdaki Docker komutu ile Redis'i ayağa kaldırın:
 
### 2. Redis Servisini Başlatın

Redis’in çalıştığından emin olun. Aşağıdaki komutu kullanarak Docker üzerinden Redis’i başlatabilirsiniz:


docker run -d -p 6379:6379 --name redis redis


## 🚀 Özellikler

- ✅ **JWT Authentication:** Kullanıcı kimlik doğrulaması sonrası güvenli erişim için JWT token üretimi
- 🛡️ **Method Düzeyinde Güvenlik:** Spring Security ile method düzeyinde erişim kontrolü sağlanır.
- 💾 **JWT Token Cache ve Doğrulama:** Access ve Refresh token'lar Redis üzerinde cache'lenir ve geçerliliği Redis üzerinden kontrol edilir.
- 🔁 **Token Kara Listeleme:** Çıkış yapan veya geçersiz hale gelen token'lar Redis üzerinde kara listeye alınır.
- ♻️ **Refresh Token ile Yenileme:** Geçerli bir refresh token ile yeni access token alınabilir
- 🔐 **MERNIS Entegrasyonu:** Kullanıcının girdiği kimlik bilgileri MERNIS servisi üzerinden doğrulanır.
- 📦 **Layered Architecture:** Clean Code prensiplerine uygun, servis ve config katmanlarına ayrılmış yapı.
- 🧪 **Test Edilebilirlik:** Kolayca birim testi yapılabilir şekilde esnek tasarım.

---

## 🧠 Nasıl Çalışır?
1. Kayıt Olma: Kullanıcı, TCKN, ad, soyad, doğum yılı, kullanıcı adı ve şifre bilgileriyle sisteme kayıt olur.

2. Kimlik Doğrulama: AuthService, MERNIS Web Servisi aracılığıyla kullanıcının TCKN bilgilerini doğrular.

3. Kullanıcı Kaydı: Geçerli kimlik bilgileri ile kullanıcı veritabanına kaydedilir.

4. Giriş Yapma: Kullanıcı adı ve şifre doğrulandıktan sonra, başarılı bir giriş yapılır ve JWT Access ve Refresh Token oluşturulur.

5. Token Cache: Oluşturulan token'lar JwtTokenCacheService aracılığıyla Redis'e cache edilir ve her ikisi de Redis üzerinde saklanır.

6. Token Geçerliliği Kontrolü: Kullanıcı, korunan endpoint'lere her erişim sağladığında, gönderilen Access Token Redis üzerinden kontrol edilerek geçerliliği doğrulanır.

7. Expire Süresi: Token'ların geçerlilik süresi Redis üzerinden kontrol edilir.

7. Kara Liste Kontrolü: Token, geçerliliği devam ediyorsa Redis'teki kara liste kontrol edilir. Eğer token kara listede değilse, erişim sağlanabilir.

8. Access Token Yenileme: Refresh Token kullanılarak yeni bir Access Token alınabilir. refreshAccessToken() metodu bu işlemi yönetir.

9. Çıkış Yapma: logout() metodu çağrıldığında, kullanıcının Access Token ve Refresh Token'ı Redis kara listesini eklenir ve geçersiz hale getirilir. Böylece oturum kapatılır.

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



