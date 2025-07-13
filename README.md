# 🎓 Skill Sharing and Learning Platform

A modern, full-stack web application built with **Spring Boot** and **React** that enables users to share knowledge, create learning plans, track progress, and connect with fellow learners.

## ✨ Features

### Backend (Spring Boot)
- 🔐 **User Authentication** with Google OAuth2
- 📝 **Post Management** - Create, read, update, delete posts
- 💬 **Comment System** - Interactive discussions on posts
- 📚 **Learning Plans** - Structured learning paths
- 📊 **Progress Tracking** - Monitor learning achievements
- 👤 **User Profiles** - Comprehensive user management
- 🔍 **User Search** - Find other learners
- 📤 **File Upload** - Support for multimedia content
- 🔒 **Security** - JWT tokens, CORS configuration
- 🗄️ **Database** - MySQL with JPA/Hibernate

### Frontend (React)
- 🎨 **Modern UI/UX** - Glassmorphism design with gradients
- 📱 **Responsive Design** - Works on all devices
- 🔐 **Authentication** - Login/Register with OAuth integration
- 📝 **Content Creation** - Rich post and comment interfaces
- 📋 **Learning Management** - Interactive learning plans
- 📈 **Progress Visualization** - Track and display progress
- 🔔 **Notifications** - Real-time updates
- 👥 **Social Features** - User profiles and search
- ⚡ **Performance** - Optimized loading and animations

## 🚀 Technology Stack

### Backend
- **Java 21**
- **Spring Boot 3.4.3**
- **Spring Security** with OAuth2
- **Spring Data JPA**
- **MySQL 8.0**
- **Maven**

### Frontend
- **React 18**
- **Modern CSS3** (Grid, Flexbox, Variables)
- **Responsive Design**
- **Glassmorphism UI**

## 📦 Installation & Setup

### Prerequisites
- **Java 21** or higher
- **Node.js 16** or higher
- **MySQL 8.0**
- **Maven 3.6** or higher

### 1. Clone the Repository
```bash
git clone https://github.com/Ranjuna120/Skill-Sharing-and-Learning-Platform.git
cd Skill-Sharing-and-Learning-Platform
```

### 2. Database Setup
1. Install and start MySQL
2. Create a database named `skill`:
```sql
CREATE DATABASE skill;
```

### 3. Environment Configuration
Create a `.env` file in the root directory:
```properties
# Database Configuration
DB_USERNAME=your_mysql_username
DB_PASSWORD=your_mysql_password

# Google OAuth Configuration
GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_CLIENT_SECRET=your_google_client_secret
```

### 4. Google OAuth Setup (Optional)
1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select existing
3. Enable Google+ API
4. Create OAuth 2.0 credentials
5. Add `http://localhost:8081/login/oauth2/code/google` as redirect URI
6. Update your `.env` file with the credentials

### 5. Backend Setup
```bash
# Install dependencies and run
mvn clean install
mvn spring-boot:run
```
Backend will start on: `http://localhost:8081`

### 6. Frontend Setup
```bash
# Navigate to frontend directory
cd frontend

# Install dependencies
npm install

# Start development server
npm start
```
Frontend will start on: `http://localhost:3000`

## 🏃‍♂️ Running the Application

1. **Start Backend**: `mvn spring-boot:run`
2. **Start Frontend**: `cd frontend && npm start`
3. **Access Application**: Open `http://localhost:3000`

## 📝 API Endpoints

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration
- `GET /login/oauth2/code/google` - OAuth callback

### Posts
- `GET /api/posts` - Get all posts
- `POST /api/posts` - Create post
- `GET /api/posts/{id}` - Get specific post
- `PUT /api/posts/{id}` - Update post
- `DELETE /api/posts/{id}` - Delete post

### Comments
- `GET /api/posts/{postId}/comments` - Get post comments
- `POST /api/posts/{postId}/comments` - Add comment

### Learning Plans
- `GET /api/learning-plans` - Get user's learning plans
- `POST /api/learning-plans` - Create learning plan
- `PUT /api/learning-plans/{id}` - Update learning plan

### Progress
- `GET /api/progress` - Get user progress
- `POST /api/progress` - Update progress

### Users
- `GET /api/users/profile` - Get user profile
- `PUT /api/users/profile` - Update profile
- `GET /api/users/search` - Search users

## 🎨 UI Features

- **Glassmorphism Design** - Modern glass-like effects
- **Dark/Light Theme Support** - Automatic theme detection
- **Responsive Layout** - Mobile-first design
- **Smooth Animations** - CSS transitions and transforms
- **Loading States** - Beautiful loading animations
- **Form Validation** - Real-time validation feedback

## 🔧 Configuration

### Database Configuration
Update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/skill?createDatabaseIfNotExist=true
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD:password}
```

### CORS Configuration
CORS is configured to allow requests from:
- `http://localhost:3000` (React dev server)
- `http://127.0.0.1:3000`

## 🐛 Troubleshooting

### Common Issues

1. **Database Connection Failed**
   - Ensure MySQL is running
   - Check database credentials in `.env`
   - Verify database `skill` exists

2. **Frontend Build Errors**
   - Run `npm install` to ensure dependencies
   - Check Node.js version (16+)
   - Clear npm cache: `npm cache clean --force`

3. **OAuth Issues**
   - Verify Google OAuth credentials
   - Check redirect URI configuration
   - Ensure OAuth consent screen is configured

4. **Port Conflicts**
   - Backend: Change `server.port` in `application.properties`
   - Frontend: Use `PORT=3001 npm start` for different port

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/new-feature`
3. Commit changes: `git commit -am 'Add new feature'`
4. Push to branch: `git push origin feature/new-feature`
5. Submit a pull request

## 👨‍💻 Author

**Ranjuna** - [GitHub Profile](https://github.com/Ranjuna120)

---

⭐ If you found this project helpful, please give it a star!
