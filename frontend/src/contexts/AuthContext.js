import React, { createContext, useContext, useState, useEffect } from 'react';
import axios from 'axios';

const AuthContext = createContext();

export const useAuth = () => {
  return useContext(AuthContext);
};

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const checkAuthStatus = async () => {
      try {
        // Check if we're in the OAuth callback
        const urlParams = new URLSearchParams(window.location.search);
        const code = urlParams.get('code');
        
        if (code) {
          // We're in the OAuth callback, redirect to the backend
          window.location.href = `http://localhost:8081/login/oauth2/code/google?${window.location.search}`;
          return;
        }

        const response = await axios.get('http://localhost:8081/api/user', {
          withCredentials: true
        });
        if (response.data) {
          setUser(response.data);
        }
      } catch (error) {
        console.error('Auth check failed:', error);
        setUser(null);
      } finally {
        setLoading(false);
      }
    };

    checkAuthStatus();
  }, []);

  const loginWithGoogle = () => {
    window.location.href = 'http://localhost:8081/oauth2/authorization/google';
  };

  const logout = async () => {
    try {
      await axios.post('http://localhost:8081/api/auth/logout', {}, {
        withCredentials: true
      });
      setUser(null);
      return true;
    } catch (error) {
      console.error('Logout failed:', error);
      throw new Error('Failed to log out: ' + error.message);
    }
  };

  const value = {
    user,
    loading,
    logout,
    loginWithGoogle
  };

  return (
    <AuthContext.Provider value={value}>
      {!loading && children}
    </AuthContext.Provider>
  );
}; 