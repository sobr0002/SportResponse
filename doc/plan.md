# 🚀 SportResponse - TODO Liste (3 Dages Sprint)

**Projekttid:** 3 dage  
**Team:** [Jeres navne]  
**Deadline:** [Dato]

---

## 📅 DAG 1 - Backend Fundament (Mandag/Tirsdag)

### ⏰ Morgen (2-3 timer)

#### 🗺️ 1. Mapper Klasse
- [ ] Opret `WorkoutMapper.java` i `mapper/` package
- [ ] Implementer `toEntity()` metode (WorkoutRequest + GroqResponse → Workout)
- [ ] Implementer `toResponse()` metode (Workout → WorkoutResponse)
- [ ] Implementer `toResponseList()` metode (List<Workout> → List<WorkoutResponse>)
- [ ] Tilføj `@Component` annotation

#### ⚙️ 2. Application Properties
- [ ] Opdater `application.properties`:
  - [ ] Database konfiguration (H2 til development)
  - [ ] Groq API konfiguration
  - [ ] JPA/Hibernate indstillinger
  - [ ] Logging niveau
  - [ ] H2 console settings

#### 🔐 3. Environment Variables
- [ ] Hent Groq API key fra https://console.groq.com
- [ ] Tilføj API key som environment variable
- [ ] Opret `.env.example` fil med template
- [ ] Bekræft `.env` er i `.gitignore`

### ⏰ Eftermiddag (3-4 timer)

#### 🤖 4. Groq Service (AI Integration)
- [ ] Opret `GroqService.java` i `service/` package
- [ ] Tilføj configuration properties (`@Value` annotations)
- [ ] Implementer `getWorkoutAdvice(String userPrompt)` metode
- [ ] Implementer `buildGroqRequest()` hjælpemetode
- [ ] Tilføj error handling (try-catch med WebClientResponseException)
- [ ] Tilføj logging (token usage)
- [ ] Opret system prompt til fitness rådgivning
- [ ] Test med en simpel main metode eller via terminal

#### 💼 5. Workout Service (Business Logic)
- [ ] Opdater `WorkoutService.java` i `service/` package
- [ ] Inject `WorkoutRepository`, `GroqService`, og `WorkoutMapper`
- [ ] Implementer `createWorkout(WorkoutRequest request)` metode
- [ ] Implementer `getWorkoutById(Long id)` metode
- [ ] Implementer `getAllWorkouts()` metode
- [ ] Implementer `deleteWorkout(Long id)` metode
- [ ] Tilføj error handling og logging
- [ ] Tilføj `@Transactional` annotation hvor nødvendigt

### 🌙 Aften (1-2 timer)

#### 🎮 6. Controller
- [ ] Opret `WorkoutController.java` i `controller/` package
- [ ] Tilføj `@RestController`, `@RequestMapping("/api/v1/workouts")`, `@CrossOrigin(origins = "*")`
- [ ] Implementer endpoints:
  - [ ] `POST /api/v1/workouts` - Opret workout
  - [ ] `GET /api/v1/workouts/{id}` - Hent enkelt workout
  - [ ] `GET /api/v1/workouts` - Hent alle workouts
  - [ ] `DELETE /api/v1/workouts/{id}` - Slet workout
  - [ ] `GET /api/v1/workouts/stats` - Hent statistik (valgfri)
- [ ] Tilføj korrekte HTTP status codes
- [ ] Tilføj logging

#### ✅ 7. Test Backend
- [ ] Start applikationen (`./mvnw spring-boot:run`)
- [ ] Test med Postman eller curl:
  - [ ] POST request: Opret workout
  - [ ] GET request: Hent workout
  - [ ] GET request: Hent alle workouts
  - [ ] DELETE request: Slet workout
- [ ] Verificer database i H2 console: `http://localhost:8080/h2-console`
- [ ] Verificer token tracking fungerer

**🎯 DAG 1 MÅL: Backend er fuldt funktionel og testet!**

---

## 📅 DAG 2 - Frontend & Integration (Onsdag/Torsdag)

### ⏰ Morgen (3-4 timer)

#### 📄 8. HTML Structure
- [ ] Opret `index.html` i `src/main/resources/static/`
- [ ] Tilføj Bootstrap CDN links
- [ ] Opret header med titel og beskrivelse
- [ ] Opret formular til workout request:
  - [ ] Textarea til beskrivelse
  - [ ] Submit knap
  - [ ] Loading spinner (skjult)
  - [ ] Fejlbesked område (skjult)
- [ ] Opret område til at vise AI's svar
- [ ] Opret sektion til at vise workout historik
- [ ] Opret sektion til statistik (valgfri)

#### 🎨 9. CSS Styling
- [ ] Opret `style.css` i `src/main/resources/static/css/`
- [ ] Style formular (moderne, clean design)
- [ ] Style AI response område (markdown-agtig)
- [ ] Style workout cards i historik
- [ ] Tilføj loading spinner animation
- [ ] Tilføj error message styling
- [ ] Gør responsive (mobile-friendly)
- [ ] Tilføj hover effekter og transitions

### ⏰ Eftermiddag (3-4 timer)

#### ⚡ 10. JavaScript Funktionalitet
- [ ] Opret `main.js` i `src/main/resources/static/js/`
- [ ] Definer base URL: `const API_URL = 'http://localhost:8080/api/v1/workouts';`
- [ ] Implementer `createWorkout()` function:
  - [ ] Hent input fra formular
  - [ ] Valider input (ikke tom)
  - [ ] Vis loading spinner
  - [ ] Send POST request til backend
  - [ ] Vis AI's svar
  - [ ] Håndter fejl
  - [ ] Skjul loading spinner
- [ ] Implementer `getAllWorkouts()` function:
  - [ ] Hent alle workouts fra backend
  - [ ] Generer HTML cards
  - [ ] Vis i historik sektion
  - [ ] Tilføj delete knapper
- [ ] Implementer `deleteWorkout(id)` function:
  - [ ] Send DELETE request
  - [ ] Fjern card fra UI
  - [ ] Vis bekræftelsesbesked
- [ ] Implementer `getStatistics()` function (valgfri):
  - [ ] Hent stats fra backend
  - [ ] Vis total tokens, gennemsnit, antal workouts
- [ ] Tilføj event listeners til alle knapper
- [ ] Tilføj auto-load af historik ved page load
- [ ] Tilføj timestamp formatering (dansk format)

### 🌙 Aften (1-2 timer)

#### 🔗 11. Integration Testing
- [ ] Test komplet workflow fra frontend til database:
  - [ ] Indtast workout request i frontend
  - [ ] Verificer AI response vises korrekt
  - [ ] Verificer workout gemmes i database
  - [ ] Verificer historik opdateres
  - [ ] Test delete funktionalitet
- [ ] Test error scenarios:
  - [ ] Tom input
  - [ ] Server fejl (sluk backend)
  - [ ] Netværksfejl
- [ ] Test på forskellige browsere (Chrome, Firefox, Safari)
- [ ] Test på mobil (responsive design)
- [ ] Fix eventuelle bugs

**🎯 DAG 2 MÅL: Komplet fungerende applikation med UI!**

---

## 📅 DAG 3 - Dokumentation & Præsentation (Fredag)

### ⏰ Morgen (2-3 timer)

#### 📚 12. Kode Dokumentation
- [ ] Tilføj JavaDoc til alle public metoder
- [ ] Tilføj class-level kommentarer
- [ ] Dokumenter kompleks logik med inline kommentarer
- [ ] Tilføj kommentarer til alle DTOs
- [ ] Gennemgå kode og ryd op:
  - [ ] Fjern udkommenteret kode
  - [ ] Fjern debug print statements
  - [ ] Konsistent formatering
  - [ ] Konsistente navngivninger

#### 📝 13. README.md
- [ ] Projektbeskrivelse
- [ ] Features liste
- [ ] Teknologi stack (Java, Spring Boot, Groq API, etc.)
- [ ] Setup instruktioner:
  - [ ] Prerequisites (Java 17+, Maven)
  - [ ] Sådan får man Groq API key
  - [ ] Sådan sætter man environment variable
  - [ ] Sådan kører man applikationen
- [ ] API dokumentation:
  - [ ] Liste over alle endpoints
  - [ ] Request/response eksempler
- [ ] Troubleshooting sektion
- [ ] Fremtidige forbedringer

#### 📸 14. Screenshots & Diagrammer
- [ ] Tag screenshots af:
  - [ ] Frontend med workout request
  - [ ] AI response vist i UI
  - [ ] Workout historik
  - [ ] H2 database console med data
  - [ ] Postman request/response
- [ ] Opret arkitektur diagram (simpel tegning):
  - [ ] Frontend → Controller → Service → Repository → Database
  - [ ] Backend → GroqService → Groq API
- [ ] Opret dataflow diagram med de 4 DTOs

### ⏰ Eftermiddag (2-3 timer)

#### 📊 15. Præsentationsmateriale
- [ ] Opret præsentations slides (PowerPoint/Google Slides):
  - [ ] Slide 1: Titel + Team
  - [ ] Slide 2: Problem statement
  - [ ] Slide 3: Løsning (SportResponse)
  - [ ] Slide 4: Teknologi stack
  - [ ] Slide 5: Arkitektur diagram
  - [ ] Slide 6: DTO struktur (4 DTOs forklaret)
  - [ ] Slide 7: Prompt engineering (system message)
  - [ ] Slide 8: Demo screenshots
  - [ ] Slide 9: Token tracking & cost analysis
  - [ ] Slide 10: Udfordringer & løsninger
  - [ ] Slide 11: Læring & refleksion
  - [ ] Slide 12: Fremtidige forbedringer
- [ ] Forbered demo script:
  - [ ] Åbn applikation
  - [ ] Vis frontend
  - [ ] Indtast workout request
  - [ ] Vis AI response
  - [ ] Vis historik
  - [ ] Vis database (H2 console)
  - [ ] Vis kode (vælg interessante dele)
  - [ ] Vis statistik

#### 📊 16. Dokumenter Resultater
- [ ] Saml token usage statistik:
  - [ ] Total tokens brugt
  - [ ] Gennemsnitlig tokens per request
  - [ ] Billigste/dyreste request
- [ ] Dokumenter edge cases I håndterede
- [ ] Dokumenter tekniske valg og hvorfor:
  - [ ] Hvorfor 4 DTOs?
  - [ ] Hvorfor separate mapper klasse?
  - [ ] Hvorfor nested records?
  - [ ] Hvorfor temperature 0.7?
  - [ ] Hvorfor max_tokens 500?

### 🌙 Aften (1-2 timer)

#### 🎤 17. Præsentations Øvelse
- [ ] Øv live demo (2-3 gange)
- [ ] Time præsentationen (maks 10 minutter)
- [ ] Forbered svar på mulige spørgsmål:
  - [ ] Hvordan fungerer Groq API?
  - [ ] Hvad er tokens?
  - [ ] Hvorfor bruge DTOs?
  - [ ] Hvordan håndterer I rate limiting? (I gør ikke - men kunne med Bucket4j)
  - [ ] Hvordan sikrer I API key? (Environment variables)
  - [ ] Hvorfor Spring Boot?
  - [ ] Hvad er next steps?
- [ ] Lav backup plan hvis netværk/demo fejler:
  - [ ] Video af fungerende demo
  - [ ] Screenshots af hver step
  - [ ] Kode eksempler klar til at vise

#### ✅ 18. Final Checklist
- [ ] Alle endpoints virker
- [ ] Frontend ser professionelt ud
- [ ] Database fungerer
- [ ] Screenshots taget
- [ ] README komplet
- [ ] Kode dokumenteret
- [ ] Præsentation klar
- [ ] Demo testet 3+ gange
- [ ] Backup materiale klart
- [ ] Git repository opdateret
- [ ] Alle kender deres del af præsentationen

**🎯 DAG 3 MÅL: Klar til at præsentere!**

---

## 🚀 Quick Reference Commands

### Start Applikationen
```bash
# Set API key (Mac/Linux)
export GROQ_API_KEY=din-api-key

# Set API key (Windows CMD)
set GROQ_API_KEY=din-api-key

# Set API key (Windows PowerShell)
$env:GROQ_API_KEY="din-api-key"

# Start Spring Boot
./mvnw spring-boot:run
```

### Test Endpoints (Curl)
```bash
# POST - Opret workout
curl -X POST http://localhost:8080/api/v1/workouts \
  -H "Content-Type: application/json" \
  -d '{"description":"Give me a chest workout for beginners"}'

# GET - Hent alle workouts
curl http://localhost:8080/api/v1/workouts

# GET - Hent enkelt workout
curl http://localhost:8080/api/v1/workouts/1

# DELETE - Slet workout
curl -X DELETE http://localhost:8080/api/v1/workouts/1

# GET - Hent statistik
curl http://localhost:8080/api/v1/workouts/stats
```

### Adgang til H2 Database Console
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:sportresponse`
- Username: `sa`
- Password: (tomt)

---

## 💡 Prioriteter hvis I løber tør for tid

### Must Have (Skal være med!)
- ✅ Backend funktionel (alle CRUD endpoints)
- ✅ Frontend kan oprette og vise workouts
- ✅ Groq API integration virker
- ✅ Database gemmer data
- ✅ Basic dokumentation (README)
- ✅ Præsentation slides

### Nice to Have (Godt at have)
- ⭐ Statistik endpoint
- ⭐ Delete funktionalitet i frontend
- ⭐ Fancy styling
- ⭐ Responsive design
- ⭐ Detaljeret dokumentation

### Could Have (Hvis tid)
- 💫 Tests (unit tests)
- 💫 Avancerede features
- 💫 Deployment til cloud
- 💫 Video demo

---

## 🐛 Troubleshooting

### Problem: API key virker ikke
- [ ] Check at environment variable er sat korrekt
- [ ] Genstart IDE efter at have sat env variable
- [ ] Check at API key er valid på Groq console

### Problem: Database fejl
- [ ] Check `application.properties` konfiguration
- [ ] Verificer H2 dependency i `pom.xml`
- [ ] Check at table bliver auto-created (se logs)

### Problem: CORS fejl i browser
- [ ] Verificer `@CrossOrigin(origins = "*")` på controller
- [ ] Check at backend kører på port 8080
- [ ] Check browser console for fejlbeskeder

### Problem: Frontend kan ikke connecte til backend
- [ ] Verificer backend kører (`http://localhost:8080/api/v1/workouts`)
- [ ] Check at API_URL i JavaScript er korrekt
- [ ] Åbn Network tab i browser DevTools

---

## 📞 Hjælp & Resources

### Groq API
- Console: https://console.groq.com
- Dokumentation: https://console.groq.com/docs/quickstart

### Spring Boot
- Dokumentation: https://spring.io/projects/spring-boot
- Guides: https://spring.io/guides

### Bootstrap (til frontend)
- Dokumentation: https://getbootstrap.com/docs/

---

## ✅ Aflevering Checklist

- [ ] GitHub repository link klar
- [ ] README.md komplet med:
  - [ ] Beskrivelse
  - [ ] Setup guide
  - [ ] Screenshots
- [ ] Kode på GitHub (seneste version pushed)
- [ ] Præsentation slides uploaded
- [ ] Demo testet og virker
- [ ] Backup materiale (screenshots/video) klar
- [ ] Team klar til at præsentere (5-10 min)
- [ ] Kan forklare tekniske valg
- [ ] Kan svare på spørgsmål om koden

---

**💪 Success Faktorer:**
1. Start med backend - få det til at virke først
2. Test ofte - ikke vent til sidst
3. Commit til Git regelmæssigt
4. Hjælp hinanden når I sidder fast
5. Hold fokus på core features først
6. Dokumenter løbende mens I koder
7. Øv demo før præsentation

**🎉 I kan gøre det! Held og lykke!**

---

**Sidste opdatering:** [Dato]  
**Næste deadline:** [Dato]  
**Status:** 🔴 Ikke startet / 🟡 I gang / 🟢 Færdig