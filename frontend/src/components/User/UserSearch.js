import React, { useState, useEffect } from 'react';
import { searchUsers, followUser, isFollowing } from '../services/api';
import './UserSearch.css';

const UserSearch = ({ currentUserId }) => {
  const [searchQuery, setSearchQuery] = useState('');
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [followStatus, setFollowStatus] = useState({});

  useEffect(() => {
    const checkFollowStatus = async () => {
      if (!currentUserId) return;
      
      const status = {};
      for (const user of users) {
        status[user.id] = await isFollowing(currentUserId, user.id);
      }
      setFollowStatus(status);
    };

    if (users.length > 0) {
      checkFollowStatus();
    }
  }, [users, currentUserId]);

  const handleSearch = async (e) => {
    e.preventDefault();
    if (!searchQuery.trim()) return;

    setLoading(true);
    try {
      const results = await searchUsers(searchQuery);
      setUsers(results);
    } catch (error) {
      console.error('Error searching users:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleFollow = async (userId) => {
    if (!currentUserId) return;
    
    try {
      await followUser(currentUserId, userId);
      setFollowStatus(prev => ({
        ...prev,
        [userId]: !prev[userId]
      }));
    } catch (error) {
      console.error('Error following user:', error);
    }
  };

  return (
    <div className="user-search">
      <div className="form-modern">
        <form onSubmit={handleSearch}>
          <div className="form-group-modern">
            <div className="input-with-icon">
              <input
                type="text"
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                placeholder="Search users by name or email..."
                className="form-input-modern"
              />
              <span className="input-icon">🔍</span>
            </div>
          </div>
          <div className="form-actions-modern center">
            <button 
              type="submit" 
              className={`btn btn-primary ${loading ? 'loading' : ''}`}
              disabled={loading}
            >
              {loading ? (
                <>
                  <div className="spinner-small"></div>
                  Searching...
                </>
              ) : (
                <>
                  <span>🔍</span>
                  Search Users
                </>
              )}
            </button>
          </div>
        </form>
      </div>

      {loading && (
        <div className="card loading-card">
          <div className="card-content text-center">
            <div className="loading-text">
              <div className="spinner-small"></div>
              Searching users...
            </div>
          </div>
        </div>
      )}

      {users.length === 0 && searchQuery && !loading && (
        <div className="card card-info">
          <div className="card-content text-center">
            <p>🔍 No users found</p>
            <p>Try a different search term</p>
          </div>
        </div>
      )}

      {users.length > 0 && (
        <div className="grid grid-cols-1 gap-4 search-results">
          {users.map(user => (
            <div key={user.id} className="card user-card">
              <div className="card-content">
                <div className="user-profile-section">
                  <img
                    src={user.profileImage || `https://ui-avatars.com/api/?name=${encodeURIComponent(user.name)}&background=random`}
                    alt={user.name}
                    className="user-avatar"
                  />
                  <div className="user-info">
                    <h3>{user.name}</h3>
                    <p>{user.email}</p>
                    <div className="user-stats">
                      <span>👥 {user.followersCount || 0} followers</span>
                      <span>➡️ {user.followingCount || 0} following</span>
                    </div>
                  </div>
                  <button
                    onClick={() => handleFollow(user.id)}
                    className={`btn ${followStatus[user.id] ? 'btn-outline' : 'btn-primary'}`}
                  >
                    {followStatus[user.id] ? '✓ Following' : '+ Follow'}
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default UserSearch; 