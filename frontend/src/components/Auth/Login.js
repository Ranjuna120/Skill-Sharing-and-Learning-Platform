import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import './Login.css';

const Login = () => {
  const navigate = useNavigate();
  const [isLogin, setIsLogin] = useState(true);
  const [formData, setFormData] = useState({
    email: '',
    password: ''
  });
  const [error, setError] = useState('');

  const handleGoogleLogin = () => {
    // Redirect to Spring Boot's Google OAuth2 endpoint
    window.location.href = 'http://localhost:8081/oauth2/authorization/google';
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    
    try {
      const endpoint = isLogin ? '/api/auth/login' : '/api/auth/register';
      const response = await axios.post(`http://localhost:8081${endpoint}`, formData, {
        withCredentials: true
      });
      
      if (response.data) {
        // Navigate directly to posts page
        navigate('/posts');
      } else {
        setError('Login failed. Please try again.');
      }
    } catch (error) {
      setError(error.response?.data || 'An error occurred. Please try again.');
    }
  };

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  // Check if user was redirected back from OAuth2
  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    const error = params.get('error');
    if (error) {
      setError('Google login failed. Please try again.');
    }
  }, []);

  return (
    <div className="page-container">
      <div className="form-modern">
        <div className="form-header">
          <h1 className="form-title-modern">Welcome Back</h1>
          <p className="form-subtitle">Sign in to share your skills and connect with others</p>
        </div>
        
        <form onSubmit={handleSubmit}>
          <div className="form-group-modern">
            <div className="input-with-icon">
              <input
                type="email"
                name="email"
                placeholder="Enter your email"
                value={formData.email}
                onChange={handleChange}
                className="form-input-modern"
                required
              />
              <span className="input-icon">📧</span>
            </div>
          </div>
          
          <div className="form-group-modern">
            <div className="input-with-icon">
              <input
                type="password"
                name="password"
                placeholder="Enter your password"
                value={formData.password}
                onChange={handleChange}
                className="form-input-modern"
                required
              />
              <span className="input-icon">🔒</span>
            </div>
          </div>
          
          {error && <div className="form-error">⚠️ {error}</div>}
          
          <div className="form-actions-modern center">
            <button type="submit" className="btn btn-primary btn-lg">
              {isLogin ? 'Sign In' : 'Create Account'}
            </button>
          </div>
        </form>

        <div className="divider-modern">
          <span>OR</span>
        </div>

        <button className="btn btn-outline btn-lg google-btn" onClick={handleGoogleLogin}>
          <svg className="google-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 48 48" width="20" height="20">
            <path fill="#4285F4" d="M24 9.5c3.5 0 6.6 1.2 9.1 3.6l6.8-6.8C35.5 2.3 30 0 24 0 14.6 0 6.7 5.7 3.1 14.1l8.1 6.3C13.2 14.2 18.1 9.5 24 9.5z"/>
            <path fill="#34A853" d="M24 38.5c-5.9 0-10.8-4.7-12.8-10.9l-8.1 6.3C6.7 42.3 14.6 48 24 48c5.9 0 11.4-2.3 15.5-6.2l-7.6-5.9c-2.1 1.4-4.7 2.6-7.9 2.6z"/>
            <path fill="#FBBC05" d="M41.9 24c0-1.3-.1-2.6-.4-3.9H24v7.8h10.5c-.5 2.5-1.9 4.7-3.9 6.2l7.6 5.9c4.5-4.1 7.7-10.2 7.7-16z"/>
            <path fill="#EA4335" d="M11.2 27.4c-.5-1.5-.8-3.1-.8-4.9s.3-3.4.8-4.9L3.1 14.1C1.1 18 0 22.4 0 27s1.1 9 .3 12.9l8.1-6.3z"/>
          </svg>
          Continue with Google
        </button>

        <div className="text-center" style={{marginTop: '1.5rem'}}>
          <span style={{color: '#718096'}}>Don't have an account? </span>
          <button 
            className="btn btn-ghost btn-sm"
            onClick={() => navigate('/register')}
          >
            Sign Up
          </button>
        </div>
      </div>
    </div>
  );
};

export default Login;