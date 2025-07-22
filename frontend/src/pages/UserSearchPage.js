import React from 'react';
import UserSearch from '../components/User/UserSearch';
import { useAuth } from '../contexts/AuthContext';
import { useNavigate } from 'react-router-dom';
import { FaUsers, FaSearch } from 'react-icons/fa';

const UserSearchPage = () => {
  const { user } = useAuth();
  const navigate = useNavigate();

  if (!user) {
    return (
      <div className="page-container">
        <div className="card card-error">
          <div className="card-content text-center">
            <h2>Authentication Required</h2>
            <p>Please log in to search for users.</p>
            <button className="btn btn-primary" onClick={() => navigate('/login')}>
              Login
            </button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="user-search-page">
      <div className="user-search-page-container">
        <div className="page-header">
          <div className="header-content">
            <div className="header-text">
              <h1 className="page-title">
                <FaUsers className="title-icon" />
                Find Users
              </h1>
              <p className="page-subtitle">Connect with other learners and experts in the community</p>
            </div>
          </div>
        </div>
        <UserSearch currentUserId={user.id} />
      </div>
    </div>
  );
};

export default UserSearchPage; 