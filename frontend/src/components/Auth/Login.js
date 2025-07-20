import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import './Login.css';

const Login = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { loginWithCredentials, loginWithGoogle } = useAuth();
  const [isLogin, setIsLogin] = useState(true);
  const [formData, setFormData] = useState({
    email: '',
    password: ''
  });
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [successMessage, setSuccessMessage] = useState('');

  const handleGoogleLogin = () => {
    // Redirect to Spring Boot's Google OAuth2 endpoint
    loginWithGoogle();
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setIsLoading(true);
    
    try {
      const userData = await loginWithCredentials(formData.email, formData.password);
      
      if (userData) {
        // Navigate directly to posts page
        navigate('/posts');
      } else {
        setError('Login failed. Please try again.');
      }
    } catch (error) {
      setError(error.message || 'An error occurred. Please try again.');
    } finally {
      setIsLoading(false);
    }
  };

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  // Check if user was redirected back from OAuth2 or registration
  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    const error = params.get('error');
    if (error === 'oauth_failed') {
      setError('Google login failed. Please try again or use email/password login.');
    }
    
    // Check for success message from registration
    if (location.state?.message) {
      setSuccessMessage(location.state.message);
    }
  }, [location]);

  return (
    <div className="login-page">
      <div className="login-background">
        <div className="floating-shapes">
          <div className="shape shape-1"></div>
          <div className="shape shape-2"></div>
          <div className="shape shape-3"></div>
          <div className="shape shape-4"></div>
        </div>
      </div>
      
      <div className="login-container">
        <div className="login-card">
          <div className="login-header">
            <div className="logo-container">
              <div className="logo-icon">
                <svg width="40" height="40" viewBox="0 0 40 40" fill="none">
                  <circle cx="20" cy="20" r="20" fill="url(#gradient1)"/>
                  <path d="M12 20l6 6 10-10" stroke="white" strokeWidth="3" strokeLinecap="round" strokeLinejoin="round"/>
                  <defs>
                    <linearGradient id="gradient1" x1="0%" y1="0%" x2="100%" y2="100%">
                      <stop offset="0%" stopColor="#667eea"/>
                      <stop offset="100%" stopColor="#764ba2"/>
                    </linearGradient>
                  </defs>
                </svg>
              </div>
              <h1 className="login-title">SkillShare</h1>
            </div>
            <p className="login-subtitle">Welcome back! Sign in to continue your learning journey</p>
          </div>
          
          <form onSubmit={handleSubmit} className="login-form">
            <div className="input-group">
              <div className="input-container">
                <div className="input-icon">
                  <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                    <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/>
                    <polyline points="22,6 12,13 2,6"/>
                  </svg>
                </div>
                <input
                  type="email"
                  name="email"
                  placeholder="Enter your email"
                  value={formData.email}
                  onChange={handleChange}
                  className="modern-input"
                  required
                />
              </div>
            </div>
            
            <div className="input-group">
              <div className="input-container">
                <div className="input-icon">
                  <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                    <rect x="3" y="11" width="18" height="11" rx="2" ry="2"/>
                    <circle cx="12" cy="16" r="1"/>
                    <path d="M7 11V7a5 5 0 0 1 10 0v4"/>
                  </svg>
                </div>
                <input
                  type="password"
                  name="password"
                  placeholder="Enter your password"
                  value={formData.password}
                  onChange={handleChange}
                  className="modern-input"
                  required
                />
              </div>
            </div>
            
            {successMessage && (
              <div className="success-message" style={{ 
                color: '#22c55e', 
                backgroundColor: '#dcfce7', 
                border: '1px solid #bbf7d0', 
                padding: '10px', 
                borderRadius: '8px', 
                marginBottom: '15px',
                textAlign: 'center'
              }}>
                <div className="success-icon">✅</div>
                <span>{successMessage}</span>
              </div>
            )}
            
            {error && (
              <div className="error-message">
                <div className="error-icon">⚠️</div>
                <span>{error}</span>
              </div>
            )}
            
            <button type="submit" className="login-btn" disabled={isLoading}>
              {isLoading ? (
                <div className="loading-spinner"></div>
              ) : (
                <>
                  <span>Sign In</span>
                  <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                    <path d="M5 12h14m-7-7l7 7-7 7"/>
                  </svg>
                </>
              )}
            </button>
          </form>

          <div className="divider">
            <span>or continue with</span>
          </div>

          <button className="google-btn" onClick={handleGoogleLogin}>
            <svg className="google-icon" width="20" height="20" viewBox="0 0 24 24">
              <path fill="#4285F4" d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"/>
              <path fill="#34A853" d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"/>
              <path fill="#FBBC05" d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z"/>
              <path fill="#EA4335" d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"/>
            </svg>
            Continue with Google
          </button>

          <div className="auth-switch">
            <span>Don't have an account?</span>
            <button 
              type="button"
              className="switch-btn"
              onClick={() => navigate('/register')}
            >
              Sign Up
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Login;