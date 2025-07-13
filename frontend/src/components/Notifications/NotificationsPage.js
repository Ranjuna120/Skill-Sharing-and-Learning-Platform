import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { FaUser, FaHeart, FaComment, FaTimes } from 'react-icons/fa';
import { useAuth } from '../../contexts/AuthContext';
import axios from 'axios';
import './NotificationsPage.css';

const NotificationCard = ({ notification, onClear }) => {
  const renderNotificationContent = () => {
    const userLink = (
      <Link to={`/profile/${notification.userId}`} className="notification-user-name">
        {notification.userName}
      </Link>
    );

    if (notification.type === 'comment') {
      return (
        <>
          <p className="notification-text">
            {userLink} commented on {notification.postTitle}
            <FaComment className="notification-icon comment-icon" />
          </p>
          <p className="notification-comment">{notification.content}</p>
        </>
      );
    } else if (notification.type === 'like') {
      return (
        <p className="notification-text">
          {userLink} liked {notification.postTitle}
          <FaHeart className="notification-icon like-icon" />
        </p>
      );
    }
  };

  return (
    <div className="notification-card">
      <div className="notification-user-info">
        <Link to={`/profile/${notification.userId}`} className="notification-user">
          {notification.userImage ? (
            <img
              src={notification.userImage}
              alt={notification.userName}
              className="notification-user-image"
              onError={(e) => {
                e.target.onerror = null;
                e.target.src = `https://ui-avatars.com/api/?name=${encodeURIComponent(notification.userName)}&background=random`;
              }}
            />
          ) : (
            <div className="notification-default-avatar">
              <FaUser />
            </div>
          )}
        </Link>
        <div className="notification-content">
          {renderNotificationContent()}
          <div className="notification-meta">
            <Link to={`/posts/${notification.postId}`} className="notification-post-link">
              View Post
            </Link>
            <span className="notification-time">
              {new Date(notification.createdAt).toLocaleString()}
            </span>
          </div>
        </div>
      </div>
      <div className="notification-actions">
        <button 
          className="clear-notification-button"
          onClick={() => onClear(notification.id)}
          title="Clear notification"
        >
          <FaTimes className="clear-icon" />
          <span>Clear</span>
        </button>
      </div>
    </div>
  );
};

const NotificationsPage = () => {
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const { user } = useAuth();

  useEffect(() => {
    const fetchNotifications = async () => {
      try {
        if (!user?.id) return;
        
        // Fetch user's posts for comments and likes
        const postsResponse = await axios.get(`http://localhost:8082/api/posts/user/${user.id}`, {
          withCredentials: true
        });

        // Extract all comments and likes from posts and format them as notifications
        const postNotifications = postsResponse.data.flatMap(post => {
          const notifications = [];

          // Add comment notifications
          const commentNotifications = (post.comments || []).map(comment => ({
            id: `comment-${comment.id}`,
            type: 'comment',
            userId: comment.userId,
            userName: comment.userName,
            userImage: comment.userProfileImage,
            postId: post.id,
            createdAt: comment.createdAt,
            content: comment.content,
            postTitle: post.title || 'your post'
          }));
          notifications.push(...commentNotifications);

          // Add like notifications
          const likeNotifications = (post.likedBy || []).map(likerId => {
            const likerInfo = post.likerInfo?.find(info => info.id === likerId) || {};
            return {
              id: `like-${post.id}-${likerId}`,
              type: 'like',
              userId: likerId,
              userName: likerInfo.name || 'Someone',
              userImage: likerInfo.profileImage,
              postId: post.id,
              createdAt: post.updatedAt || post.createdAt,
              postTitle: post.title || 'your post'
            };
          });
          notifications.push(...likeNotifications);

          return notifications;
        });

        // Sort notifications by date (most recent first)
        const sortedNotifications = postNotifications.sort((a, b) => 
          new Date(b.createdAt) - new Date(a.createdAt)
        );

        setNotifications(sortedNotifications);
        setLoading(false);
      } catch (err) {
        setError('Failed to fetch notifications');
        setLoading(false);
        console.error('Error fetching notifications:', err);
      }
    };

    fetchNotifications();
  }, [user]);

  const handleClearNotification = (notificationId) => {
    setNotifications(prev => prev.filter(notification => notification.id !== notificationId));
  };

  if (loading) {
    return (
      <div className="content-container">
        <div className="loading-spinner">Loading notifications...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="content-container">
        <div className="error-message">{error}</div>
      </div>
    );
  }

  return (
    <div className="content-container">
      <div className="page-header">
        <h1 className="page-title">📮 Notifications</h1>
        <p className="page-subtitle">Stay updated with your latest activities</p>
      </div>
      {notifications.length === 0 ? (
        <div className="card card-info">
          <div className="card-content text-center">
            <p>🔔 No notifications yet</p>
            <p style={{color: '#718096', fontSize: '0.875rem'}}>You'll see notifications when users interact with your posts</p>
          </div>
        </div>
      ) : (
        <div className="notifications-list">
          {notifications.map((notification) => (
            <NotificationCard
              key={notification.id}
              notification={notification}
              onClear={handleClearNotification}
            />
          ))}
        </div>
      )}
    </div>
  );
};

export default NotificationsPage; 
