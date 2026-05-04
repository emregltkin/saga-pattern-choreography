# 🎬 Sinema Rezervasyon Sistemi — Saga Pattern (Choreography)
 
Microservice mimarisi  **Saga Choreography** design pattern kullanılarak oluşturulmuş sinema koltuk rezervasyon sistemidir.

---
 
## 📋 İçindekiler
 
- [Overview](#overview)
- [Technical Stack](#technical-stack)
- [Proje Yapısı](#proje-yapısı)
- [Kurulum](#kurulum)
- [Veritabanı Kurulumu](#veritabanı-kurulumu)
- [API Referansı](#api-referansı)
- [Koltuk Durum Rehberi](#koltuk-durum-rehberi)

---

## Overview
 
* Bu proje; rezervasyon, ödeme ve koltuk envanteri yönetimini birbirinden bağımsız mikroservisler aracılığıyla gerçekleştirir.
* Servisler arası iletişim, merkezi bir orkestratör olmaksızın **event-driven** yaklaşımla sağlanır.
* Servisler birbirleriyle doğrudan haberleşmez; ortak event modelleri (booking-common-events) üzerinden mesajlaşır.
 
---

## Technical Stack

 * Java 21 / Spring Boot 3.5.14
 * Apache Kafka
 * PostgreSQL
 * Docker (Compose)

---
 
## Proje Yapısı
 
```
saga-pattern-choreography/
│
├── booking-common-events/       # Servisler arası ortak event modelleri
│
├── booking-service/             # Rezervasyon servisi
│   └── src/main/java/.../
│       ├── controller/
│       ├── service/
│       ├── repository/
│       └── model/
│
├── payment-service/             # Ödeme servisi
│   └── src/main/java/.../
│
├── seat-inventory-service/      # Koltuk envanter servisi
│   └── src/main/java/.../
│
├── init-scripts/
│   └── init.sql                 # Veritabanı init scripti
│
├── docker-compose.yml
└── .gitignore
```
 
---


## Kurulum

### Docker
 
```bash
# Repository Clone
git clone https://github.com/emregltkin/saga-pattern-choreography.git
cd saga-pattern-choreography
 
# Servisleri çalıştırır
docker compose up -d
```

## Database Insert Script
 
`init-scripts/init.sql` dosyası Docker compose ile otomatik olarak çalıştırılır.
Verileri eklemek için `seat-inventory-service/../data.sql` scripti ile insert edilir.
 
```sql
INSERT INTO seat_inventory (theater_id, screen_id, show_id, seat_number, status, booking_code, last_updated) VALUES
 
-- 🎬 Salon 1: Avengers Endgame (Akşam Seansı)
('THEATER_PVR_001', 'SCREEN_1', 'SHOW_101', 'A1', 'AVAILABLE', NULL, NOW()),
('THEATER_PVR_001', 'SCREEN_1', 'SHOW_101', 'A2', 'AVAILABLE', NULL, NOW()),
('THEATER_PVR_001', 'SCREEN_1', 'SHOW_101', 'A3', 'LOCKED',    'RSV_12345A01', NOW()),
('THEATER_PVR_001', 'SCREEN_1', 'SHOW_101', 'A4', 'RESERVED',  'RSV_12345A02', NOW()),
('THEATER_PVR_001', 'SCREEN_1', 'SHOW_101', 'A5', 'AVAILABLE', NULL, NOW()),
 
-- 🎬 Salon 2: Joker (Gece Seansı)
('THEATER_PVR_001', 'SCREEN_2', 'SHOW_202', 'B1', 'AVAILABLE', NULL, NOW()),
('THEATER_PVR_001', 'SCREEN_2', 'SHOW_202', 'B2', 'AVAILABLE', NULL, NOW()),
('THEATER_PVR_001', 'SCREEN_2', 'SHOW_202', 'B3', 'LOCKED',    'RSV_12345A03', NOW()),
('THEATER_PVR_001', 'SCREEN_2', 'SHOW_202', 'B4', 'AVAILABLE', NULL, NOW()),
('THEATER_PVR_001', 'SCREEN_2', 'SHOW_202', 'B5', 'RESERVED',  'RSV_12345A04', NOW()),
 
-- 🎬 Salon 3: Oppenheimer (Sabah Seansı)
('THEATER_PVR_002', 'SCREEN_3', 'SHOW_303', 'C1', 'AVAILABLE', NULL, NOW()),
('THEATER_PVR_002', 'SCREEN_3', 'SHOW_303', 'C2', 'LOCKED',    'RSV_12345A05', NOW()),
('THEATER_PVR_002', 'SCREEN_3', 'SHOW_303', 'C3', 'AVAILABLE', NULL, NOW()),
('THEATER_PVR_002', 'SCREEN_3', 'SHOW_303', 'C4', 'RESERVED',  'RSV_12345A06', NOW()),
('THEATER_PVR_002', 'SCREEN_3', 'SHOW_303', 'C5', 'AVAILABLE', NULL, NOW());
```
 
### Örnek Veri Özeti
 
| Sinema ID       | Salon    | Seans ID | Film              |
|-----------------|----------|----------|-------------------|
| THEATER_PVR_001 | SCREEN_1 | SHOW_101 | Avengers: Endgame |
| THEATER_PVR_001 | SCREEN_2 | SHOW_202 | Joker             |
| THEATER_PVR_002 | SCREEN_3 | SHOW_303 | Oppenheimer       |
 
---

## API Referansı
 
### Rezervasyon Oluştur

Belirli bir seans için bir veya birden fazla koltuk rezervasyonu yapar.

```
POST /bookings
```

**cURL:**
 
```bash
curl --request POST 'http://localhost:9191/bookings' \
  --header 'Content-Type: application/json' \
  --data '{
    "userId": "USER_1",
    "showId": "SHOW_101",
    "seatIds": ["A1"],
    "totalAmount": 100
  }'
```

---
 
## Koltuk Durumları
 
| Durum       | Açıklama                                                    |
|-------------|-------------------------------------------------------------|
| `AVAILABLE` | Koltuk müsait, rezervasyon yapılabilir                      |
| `LOCKED`    | Koltuk aktif bir oturum sırasında geçici olarak tutulmuş    |
| `RESERVED`  | Koltuk başarıyla rezerve edilmiş ve onaylanmış              |
 
---


