import React from 'react';
import { BrowserRouter as Router, Route, Routes, useLocation, Navigate } from 'react-router-dom';
import { AuthProvider } from './contexts/AuthContext';
import Login from './components/Auth/Login';
import Register from './components/Auth/Register';
import PostList from './components/Post/PostList';
import PostForm from './components/Post/PostForm';
import PlanList from './components/LearningPlan/PlanList';
import PlanForm from './components/LearningPlan/PlanForm';
import ProgressList from './components/Progress/ProgressList';
import ProgressForm from './components/Progress/ProgressForm';
import Profile from './components/User/Profile';
import NavigationBar from './components/common/NavigationBar';
import UserSearchPage from './pages/UserSearchPage';
import NotificationsPage from './components/Notifications/NotificationsPage';
import UtilsDemo from './components/Utils/UtilsDemo';
import './App.css';
import './styles/GlobalTheme.css';
import './styles/ModernButtons.css';
import './styles/ModernCards.css';
import './styles/LoadingComponents.css';
import './styles/ModernForms.css';

// Wrapper component to conditionally render NavigationBar
const AppContent = () => {
  const location = useLocation();
  const isAuthPage = location.pathname === '/login' || location.pathname === '/register';

  return (
    <div className="App">
      {!isAuthPage && <NavigationBar />}
      <div className={isAuthPage ? "content" : "main-content"}>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/posts" element={<PostList />} />
          <Route path="/list" element={<Navigate to="/posts" replace />} />
          <Route path="/create-post" element={<PostForm />} />
          <Route path="/plans" element={<PlanList />} />
          <Route path="/create-plan" element={<PlanForm />} />
          <Route path="/progress" element={<ProgressList />} />
          <Route path="/create-progress" element={<ProgressForm />} />
          <Route path="/users" element={<UserSearchPage />} />
          <Route path="/notifications" element={<NotificationsPage />} />
          <Route path="/utils-demo" element={<UtilsDemo />} />
          <Route path="/profile/:userId" element={<Profile />} />
          <Route path="/profile" element={<Profile />} />
          <Route path="/" element={<Navigate to="/posts" replace />} />
        </Routes>
      </div>
    </div>
  );
};

function App() {
  return (
    <AuthProvider>
      <Router>
        <AppContent />
      </Router>
    </AuthProvider>
  );
}

export default App;