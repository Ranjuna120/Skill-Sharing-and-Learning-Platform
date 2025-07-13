import React from 'react';
import UserSearch from '../components/User/UserSearch';
import { useAuth } from '../contexts/AuthContext';
import { useNavigate } from 'react-router-dom';

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
    <div className="content-container">
      <div className="page-header">
        <div>
          <h1 className="page-title">🔍 Find Users</h1>
          <p className="page-subtitle">Connect with other learners and experts</p>
        </div>
      </div>
      <UserSearch currentUserId={user.id} />
    </div>
  );
};

export default UserSearchPage; 