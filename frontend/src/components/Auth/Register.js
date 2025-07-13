import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import './Register.css';

const Register = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    password: '',
    confirmPassword: ''
  });
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    // Validate passwords match
    if (formData.password !== formData.confirmPassword) {
      setError('Passwords do not match');
      return;
    }

    // Validate password strength
    if (formData.password.length < 6) {
      setError('Password must be at least 6 characters long');
      return;
    }

    try {
      const response = await axios.post('http://localhost:8081/api/auth/register', {
        email: formData.email,
        password: formData.password
      });
      
      if (response.data) {
        navigate('/login');
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

  return (
    <div className="page-container">
      <div className="form-modern">
        <div className="form-header">
          <h1 className="form-title-modern">Create Account</h1>
          <p className="form-subtitle">Join our community of learners and share your skills</p>
        </div>
        
        <form onSubmit={handleSubmit}>
          <div className="form-group-modern">
            <div className="input-with-icon">
              <input
                type="text"
                name="name"
                placeholder="Enter your full name"
                value={formData.name}
                onChange={handleChange}
                className="form-input-modern"
                required
              />
              <span className="input-icon">👤</span>
            </div>
          </div>
          
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
                placeholder="Create a password"
                value={formData.password}
                onChange={handleChange}
                className="form-input-modern"
                required
              />
              <span className="input-icon">🔒</span>
            </div>
          </div>
          
          <div className="form-group-modern">
            <div className="input-with-icon">
              <input
                type="password"
                name="confirmPassword"
                placeholder="Confirm your password"
                value={formData.confirmPassword}
                onChange={handleChange}
                className="form-input-modern"
                required
              />
              <span className="input-icon">🔐</span>
            </div>
          </div>
          
          {error && <div className="form-error">⚠️ {error}</div>}
          
          <div className="form-actions-modern center">
            <button type="submit" className="btn btn-primary btn-lg">
              Create Account
            </button>
          </div>
        </form>

        <div className="text-center" style={{marginTop: '1.5rem'}}>
          <span style={{color: '#718096'}}>Already have an account? </span>
          <button 
            className="btn btn-ghost btn-sm"
            onClick={() => navigate('/login')}
          >
            Sign In
          </button>
        </div>
      </div>
    </div>
  );
};

export default Register; 