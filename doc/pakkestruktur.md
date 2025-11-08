src/
├── main/
│   ├── java/
│   │   └── org/
│   │       └── example/
│   │           └── aiproject/
│   │               ├── AiProjectApplication.java          # Main class (Spring Boot starter)
│   │               │
│   │               ├── controller/                        # REST endpoints
│   │               │   └── WorkoutController.java         # HTTP request handling
│   │               │
│   │               ├── service/                           # Business logic
│   │               │   ├── WorkoutService.java            # Workout business logic
│   │               │   └── GroqService.java               # Groq API integration
│   │               │
│   │               ├── mapper/                            # Data transformation
│   │               │   └── WorkoutMapper.java             # DTO ↔ Entity mapping
│   │               │
│   │               ├── dto/                               # Data Transfer Objects
│   │               │   ├── WorkoutRequest.java            # Frontend → Backend
│   │               │   ├── WorkoutResponse.java           # Backend → Frontend
│   │               │   ├── GroqRequest.java               # Backend → Groq API
│   │               │   └── GroqResponse.java              # Groq API → Backend
│   │               │
│   │               ├── model/                             # Database entities
│   │               │   └── Workout.java                   # JPA Entity
│   │               │
│   │               └── repository/                        # Data access layer
│   │                   └── WorkoutRepository.java         # JPA Repository interface
│   │
│   └── resources/
│       ├── application.properties                         # Configuration
│       │
│       └── static/                                        # Frontend files (kommer senere)
│           ├── index.html
│           ├── css/
│           │   └── style.css
│           └── js/
│               └── main.js
│
└── test/
└── java/
└── org/
└── example/
└── aiproject/
├── AiProjectApplicationTests.java     # Integration tests
├── service/
│   ├── WorkoutServiceTest.java        # Service tests
│   └── GroqServiceTest.java           # API tests
└── mapper/
└── WorkoutMapperTest.java         # Mapper tests