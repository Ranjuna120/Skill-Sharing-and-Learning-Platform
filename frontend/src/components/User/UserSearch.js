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

      {users.length === 0 && searchQuery && !loading && (
        <div className="card card-info">
          <div className="card-content text-center">
            <p>🔍 No users found</p>
            <p style={{color: '#718096', fontSize: '0.875rem'}}>Try a different search term</p>
          </div>
        </div>
      )}

      {users.length > 0 && (
        <div className="grid grid-cols-1 gap-4" style={{marginTop: '2rem'}}>
          {users.map(user => (
            <div key={user.id} className="card user-card">
              <div className="card-content">
                <div className="user-profile-section">
                  <img
                    src={user.profileImage || `https://ui-avatars.com/api/?name=${encodeURIComponent(user.name)}&background=random`}
                    alt={user.name}
                    className="user-avatar"
                    style={{
                      width: '60px',
                      height: '60px',
                      borderRadius: '50%',
                      objectFit: 'cover'
                    }}
                  />
                  <div className="user-info" style={{flex: 1, marginLeft: '1rem'}}>
                    <h3 style={{margin: '0 0 0.5rem 0', color: '#2d3748'}}>{user.name}</h3>
                    <p style={{margin: '0 0 0.5rem 0', color: '#718096'}}>{user.email}</p>
                    <div className="user-stats" style={{display: 'flex', gap: '1rem', fontSize: '0.875rem', color: '#a0aec0'}}>
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