import React from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { FaHome, FaPlus, FaBookReader, FaChartLine, FaUser, FaSearch, FaSignOutAlt, FaBell } from 'react-icons/fa';
import { useAuth } from '../../contexts/AuthContext';
import './NavigationBar.css';

const NavigationBar = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { logout } = useAuth();

  const isActive = (path) => {
    return location.pathname === path;
  };

  const handleLogout = async () => {
    try {
      const success = await logout();
      if (success) {
        navigate('/login');
      }
    } catch (error) {
      console.error('Failed to log out:', error);
    }
  };

  return (
    <nav className="navigation-bar">
      <div className="nav-brand">
        <Link to="/posts" className="nav-logo">
          <FaBookReader className="nav-icon brand-icon" />
          <span>Skill Sharing</span>
        </Link>
      </div>
      <div className="nav-links">
        <Link 
          to="/posts" 
          className={`nav-link ${isActive('/posts') ? 'active' : ''}`}
          title="Posts"
        >
          <FaHome className="nav-icon" />
          <span>Posts</span>
        </Link>
        <Link 
          to="/create-post" 
          className={`nav-link ${isActive('/create-post') ? 'active' : ''}`}
          title="Create Post"
        >
          <FaPlus className="nav-icon" />
          <span>Create Post</span>
        </Link>
        <Link 
          to="/plans" 
          className={`nav-link ${isActive('/plans') ? 'active' : ''}`}
          title="Learning Plans"
        >
          <FaBookReader className="nav-icon" />
          <span>Learning Plans</span>
        </Link>
        <Link 
          to="/progress" 
          className={`nav-link ${isActive('/progress') ? 'active' : ''}`}
          title="Progress"
        >
          <FaChartLine className="nav-icon" />
          <span>Progress</span>
        </Link>
        <Link 
          to="/users" 
          className={`nav-link ${isActive('/users') ? 'active' : ''}`}
          title="Search Users"
        >
          <FaSearch className="nav-icon" />
          <span>Find Users</span>
        </Link>
        <Link 
          to="/notifications" 
          className={`nav-link ${isActive('/notifications') ? 'active' : ''}`}
          title="Notifications"
        >
          <FaBell className="nav-icon" />
          <span>Notifications</span>
        </Link>
        <Link 
          to="/profile" 
          className={`nav-link ${isActive('/profile') ? 'active' : ''}`}
          title="Profile"
        >
          <FaUser className="nav-icon" />
          <span>Profile</span>
        </Link>
        <button 
          onClick={handleLogout}
          className="nav-link logout-button"
          title="Logout"
        >
          <FaSignOutAlt className="nav-icon" />
          <span>Logout</span>
        </button>
      </div>
    </nav>
  );
};

export default NavigationBar; 