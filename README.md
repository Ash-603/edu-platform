# edu-platform

```
edu-platform
├─ edu-platform
│  ├─ .idea
│  │  ├─ $PROJECT_FILE$
│  │  ├─ compiler.xml
│  │  ├─ encodings.xml
│  │  ├─ inspectionProfiles
│  │  │  └─ Project_Default.xml
│  │  ├─ jarRepositories.xml
│  │  ├─ misc.xml
│  │  ├─ qaplug_profiles.xml
│  │  ├─ vcs.xml
│  │  └─ workspace.xml
│  ├─ pom.xml
│  └─ src
│     ├─ main
│     │  ├─ java
│     │  │  ├─ com
│     │  │  │  └─ eduplatform
│     │  │  │     ├─ annotation
│     │  │  │     │  └─ ExtendedLombok.java
│     │  │  │     ├─ config
│     │  │  │     │  └─ WebSocketConfig.java
│     │  │  │     ├─ controller
│     │  │  │     │  ├─ AssignmentController.java
│     │  │  │     │  ├─ ChatController.java
│     │  │  │     │  ├─ CourseController.java
│     │  │  │     │  ├─ NotificationController.java
│     │  │  │     │  ├─ PaymentController.java
│     │  │  │     │  └─ UserController.java
│     │  │  │     ├─ EduPlatformApplication.java
│     │  │  │     ├─ model
│     │  │  │     │  ├─ AlipayPayment.java
│     │  │  │     │  ├─ Assignment.java
│     │  │  │     │  ├─ ChatRoom.java
│     │  │  │     │  ├─ Course.java
│     │  │  │     │  ├─ CourseMaterial.java
│     │  │  │     │  ├─ Message.java
│     │  │  │     │  ├─ Notification.java
│     │  │  │     │  ├─ Order.java
│     │  │  │     │  ├─ Payment.java
│     │  │  │     │  ├─ PaymentStrategy.java
│     │  │  │     │  ├─ Student.java
│     │  │  │     │  ├─ Submission.java
│     │  │  │     │  ├─ Teacher.java
│     │  │  │     │  ├─ User.java
│     │  │  │     │  ├─ UserPreference.java
│     │  │  │     │  └─ WechatPayment.java
│     │  │  │     ├─ repository
│     │  │  │     │  ├─ CourseRepository.java
│     │  │  │     │  ├─ NotificationRepository.java
│     │  │  │     │  ├─ OrderRepository.java
│     │  │  │     │  └─ UserRepository.java
│     │  │  │     ├─ service
│     │  │  │     │  ├─ AssignmentService.java
│     │  │  │     │  ├─ ChatService.java
│     │  │  │     │  ├─ CourseService.java
│     │  │  │     │  ├─ NotificationService.java
│     │  │  │     │  ├─ PaymentService.java
│     │  │  │     │  └─ UserService.java
│     │  │  │     └─ util
│     │  │  │        ├─ DateUtils.java
│     │  │  │        ├─ FileUtils.java
│     │  │  │        └─ SecurityUtils.java
│     │  │  └─ org
│     │  │     └─ example
│     │  │        └─ Main.java
│     │  └─ resources
│     │     └─ application.properties
│     └─ test
│        └─ java
├─ frontend
│  ├─ docs
│  │  └─ api
│  │     ├─ course.js
│  │     ├─ payment.js
│  │     └─ user.js
│  ├─ index.html
│  ├─ jsconfig.json
│  ├─ package-lock.json
│  ├─ package.json
│  ├─ public
│  │  └─ favicon.ico
│  ├─ README.md
│  ├─ src
│  │  ├─ App.vue
│  │  ├─ assets
│  │  │  ├─ base.css
│  │  │  ├─ logo.svg
│  │  │  └─ main.css
│  │  ├─ components
│  │  │  ├─ common
│  │  │  │  ├─ FooterComponent.vue
│  │  │  │  ├─ Header.vue
│  │  │  │  ├─ NotificationList.vue
│  │  │  │  └─ Sidebar.vue
│  │  │  ├─ course
│  │  │  │  ├─ CourseCard.vue
│  │  │  │  └─ VideoPlayer.vue
│  │  │  ├─ HelloWorld.vue
│  │  │  ├─ icons
│  │  │  │  ├─ IconCommunity.vue
│  │  │  │  ├─ IconDocumentation.vue
│  │  │  │  ├─ IconEcosystem.vue
│  │  │  │  ├─ IconSupport.vue
│  │  │  │  └─ IconTooling.vue
│  │  │  ├─ TheWelcome.vue
│  │  │  └─ WelcomeItem.vue
│  │  ├─ main.js
│  │  ├─ router
│  │  │  └─ index.js
│  │  ├─ store
│  │  │  ├─ index.js
│  │  │  └─ modules
│  │  │     ├─ assignment.js
│  │  │     ├─ chat.js
│  │  │     ├─ course.js
│  │  │     ├─ notification.js
│  │  │     ├─ payment.js
│  │  │     └─ user.js
│  │  └─ views
│  │     ├─ AssignmentSubmission.vue
│  │     ├─ CourseCreate.vue
│  │     ├─ CourseDetail.vue
│  │     ├─ GradeAssignment.vue
│  │     ├─ Home.vue
│  │     ├─ Login.vue
│  │     ├─ Register.vue
│  │     └─ TeacherDashboard.vue
│  └─ vite.config.js
└─ README.md

```
