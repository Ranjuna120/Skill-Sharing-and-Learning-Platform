import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { FaHeart, FaStar, FaComment, FaPlus, FaUser, FaEdit, FaTrash } from 'react-icons/fa';
import { favoritePost } from '../services/api';
import { useRequireAuth } from '../../hooks/useRequireAuth';
import CommentSection from './CommentSection';
import axios from 'axios';
import './PostList.css';

const PostList = () => {
  const { user, loading } = useRequireAuth();
  const [posts, setPosts] = useState([]);
  const [error, setError] = useState('');
  const [expandedComments, setExpandedComments] = useState({});
  const [localFavorites, setLocalFavorites] = useState({});
  const [commentInputs, setCommentInputs] = useState({});
  const [editingPost, setEditingPost] = useState(null);
  const [editForm, setEditForm] = useState({
    title: '',
    description: ''
  });
  const [likeAnimations, setLikeAnimations] = useState({});

  const fetchPosts = async () => {
    try {
      const response = await axios.get('http://localhost:8082/api/posts', {
        withCredentials: true
      });
      setPosts(response.data);
      console.log('Current user:', user);
      console.log('Fetched posts:', response.data);
    } catch (error) {
      setError('Failed to fetch posts');
      console.error('Error fetching posts:', error);
    }
  };

  useEffect(() => {
    if (user) {
      console.log('Logged in user ID:', user.id);
      fetchPosts();
    } else {
      console.log('No user logged in');
    }
  }, [user]);

  const handleDeletePost = async (postId) => {
    if (!window.confirm('Are you sure you want to delete this post? This action cannot be undone.')) {
      return;
    }

    try {
      const userId = user?.id || user?.sub;
      if (!userId) {
        setError('User ID not found. Please log in again.');
        return;
      }

      const response = await axios.delete(`http://localhost:8082/api/posts/${postId}`, {
        params: { userId },
        withCredentials: true
      });

      if (response.status === 204) {
        setPosts(posts.filter(post => post.id !== postId));
        showSuccessMessage('Post deleted successfully');
      }
    } catch (err) {
      const errorMessage = err.response?.data?.message || 
        err.response?.data || 
        'An error occurred while deleting the post. Please try again.';
      setError('Failed to delete post: ' + errorMessage);
      console.error('Error deleting post:', err);
    }
  };

  const handleEditPost = async (postId) => {
    if (!editForm.title.trim() || !editForm.description.trim()) {
      setError('Title and description are required');
      return;
    }

    try {
      const response = await axios.put(`http://localhost:8082/api/posts/${postId}`, 
        {
          title: editForm.title,
          description: editForm.description
        },
        {
          withCredentials: true
        }
      );
      
      if (response.data) {
        setPosts(posts.map(post => 
          post.id === postId ? { ...post, ...response.data } : post
        ));
        setEditingPost(null);
        setEditForm({ title: '', description: '' });
        showSuccessMessage('Post updated successfully');
      }
    } catch (err) {
      setError('Failed to update post: ' + (err.response?.data?.message || err.message));
      console.error('Error updating post:', err);
    }
  };

  const startEditing = (post) => {
    setEditingPost(post.id);
    setEditForm({
      title: post.title,
      description: post.description
    });
  };

  const cancelEditing = () => {
    setEditingPost(null);
    setEditForm({ title: '', description: '' });
  };

  const handleLike = async (postId) => {
    try {
      if (!user) {
        setError('You must be logged in to like a post');
        return;
      }

      const userId = user.id || user.sub;
      if (!userId) {
        setError('Unable to determine user ID. Please log in again.');
        return;
      }

      // Start like animation
      setLikeAnimations(prev => ({ ...prev, [postId]: true }));

      // Update local state immediately for better UX
      setPosts(posts.map(post => {
        if (post.id === postId) {
          const isLiked = post.likedBy?.includes(parseInt(userId));
          return {
            ...post,
            likedBy: isLiked 
              ? post.likedBy.filter(id => id !== parseInt(userId))
              : [...(post.likedBy || []), parseInt(userId)],
            likeCount: isLiked ? (post.likeCount || 0) - 1 : (post.likeCount || 0) + 1
          };
        }
        return post;
      }));

      // Make API call
      await axios.post(`http://localhost:8082/api/posts/${postId}/like`, null, {
        params: { userId },
        withCredentials: true
      });

      // Reset animation after completion
      setTimeout(() => {
        setLikeAnimations(prev => ({ ...prev, [postId]: false }));
      }, 800); // Match the animation duration
    } catch (error) {
      console.error('Error liking post:', error);
      setError('Failed to like post: ' + (error.response?.data?.message || error.message));
      
      // Revert the local state on error
      fetchPosts();
    }
  };

  const handleFavorite = async (postId) => {
    if (!user) return;
    
    // Update local state immediately for smooth animation
    setLocalFavorites(prev => ({ ...prev, [postId]: !prev[postId] }));
    setPosts(posts.map(post => {
      if (post.id === postId) {
        const newIsFavorited = !post.isFavorited;
        return {
          ...post,
          isFavorited: newIsFavorited,
          favoriteCount: newIsFavorited ? post.favoriteCount + 1 : post.favoriteCount - 1
        };
      }
      return post;
    }));

    // Try to update backend
    try {
      await axios.post(`http://localhost:8082/api/posts/${postId}/favorite`, null, {
        params: { userId: user.sub },
        withCredentials: true
      });
    } catch (err) {
      // Revert on error
      setLocalFavorites(prev => ({ ...prev, [postId]: !prev[postId] }));
      setPosts(posts.map(post => {
        if (post.id === postId) {
          const revertedIsFavorited = post.isFavorited;
          return {
            ...post,
            isFavorited: revertedIsFavorited,
            favoriteCount: revertedIsFavorited ? post.favoriteCount : post.favoriteCount + 1
          };
        }
        return post;
      }));
      setError('Failed to favorite post: ' + err.message);
    }
  };

  const toggleComments = (postId) => {
    setExpandedComments(prev => ({
      ...prev,
      [postId]: !prev[postId]
    }));
  };

  const handleCommentSubmit = async (postId, content) => {
    try {
      if (!user) {
        setError('You must be logged in to comment');
        return;
      }

      if (!content.trim()) {
        setError('Comment cannot be empty');
        return;
      }

      const response = await axios.post(
        `http://localhost:8082/api/posts/${postId}/comments`,
        { content },
        {
          params: { userId: user.id },
          withCredentials: true
        }
      );

      // Update local state with new comment
      setPosts(posts.map(post => {
        if (post.id === postId) {
          return {
            ...post,
            comments: [...post.comments, response.data]
          };
        }
        return post;
      }));

      // Clear comment input
      setCommentInputs(prev => ({ ...prev, [postId]: '' }));
    } catch (error) {
      setError('Failed to add comment: ' + (error.response?.data?.message || error.message));
      console.error('Error adding comment:', error);
    }
  };

  const showSuccessMessage = (message) => {
    const successMessage = document.createElement('div');
    successMessage.className = 'success-message';
    successMessage.textContent = message;
    document.body.appendChild(successMessage);
    setTimeout(() => successMessage.remove(), 3000);
  };

  const isPostOwner = (post) => {
    if (!user || !post || !post.user) {
      console.log('Missing user data:', { user, post });
      return false;
    }
    
    const currentUserId = user.id || user.sub;
    const postUserId = post.user.id;
    
    console.log('Comparing user IDs:', {
      currentUserId,
      postUserId,
      user,
      postUser: post.user
    });
    
    return currentUserId === postUserId;
  };

  if (loading) {
    return (
      <div className="content-container">
        <div className="loading-spinner">Loading...</div>
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
        <div>
          <h1 className="page-title">🎯 Skill Sharing Posts</h1>
          <p className="page-subtitle">Discover and share knowledge with the community</p>
        </div>
        <Link to="/create-post" className="btn btn-primary">
          <FaPlus style={{marginRight: '0.5rem'}} />
          Create Post
        </Link>
      </div>
      {!loading && posts.length === 0 ? (
        <p className="no-posts">No posts available. Create one now!</p>
      ) : (
        <div className="post-list">
          {posts.map((post) => (
            <div key={post.id} className="post-card">
              <div className="post-header">
                <Link to={`/profile/${post.user.id}`} className="user-profile">
                  {post.user.profileImage ? (
                    <img 
                      src={
                        post.user.profileImage.startsWith('data:') 
                          ? post.user.profileImage 
                          : post.user.profileImage.startsWith('http') 
                            ? post.user.profileImage 
                            : `data:image/jpeg;base64,${post.user.profileImage}`
                      }
                      alt={post.user.name} 
                      className="user-avatar"
                      onError={(e) => {
                        console.log('Profile image error:', {
                          originalSrc: e.target.src,
                          user: post.user.name,
                          profileImage: post.user.profileImage
                        });
                        e.target.onerror = null;
                        e.target.src = `https://ui-avatars.com/api/?name=${encodeURIComponent(post.user.name)}&background=random`;
                      }}
                    />
                  ) : (
                    <div className="default-avatar">
                      <FaUser />
                    </div>
                  )}
                  <div className="user-info">
                    <h3>{post.user.name}</h3>
                    <span className="post-date">
                      {new Date(post.createdAt).toLocaleDateString()}
                    </span>
                  </div>
                </Link>
                {isPostOwner(post) && (
                  <div className="post-actions-owner">
                    <button 
                      className="edit-btn"
                      onClick={() => startEditing(post)}
                      title="Edit post"
                    >
                      <FaEdit />
                    </button>
                    <button 
                      className="delete-btn"
                      onClick={() => handleDeletePost(post.id)}
                      title="Delete post"
                    >
                      <FaTrash />
                    </button>
                  </div>
                )}
              </div>
              
              {editingPost === post.id ? (
                <div className="edit-form">
                  <input
                    type="text"
                    value={editForm.title}
                    onChange={(e) => setEditForm({ ...editForm, title: e.target.value })}
                    placeholder="Title"
                    className="edit-input"
                  />
                  <textarea
                    value={editForm.description}
                    onChange={(e) => setEditForm({ ...editForm, description: e.target.value })}
                    placeholder="Description"
                    className="edit-textarea"
                  />
                  <div className="edit-actions">
                    <button 
                      className="save-btn"
                      onClick={() => handleEditPost(post.id)}
                    >
                      Save
                    </button>
                    <button 
                      className="cancel-btn"
                      onClick={cancelEditing}
                    >
                      Cancel
                    </button>
                  </div>
                </div>
              ) : (
                <>
                  <h2 className="post-title">{post.title}</h2>
                  <p className="post-description">{post.description}</p>
                  {post.image1 && (
                    <img
                      src={`data:image/jpeg;base64,${post.image1}`}
                      alt="Post"
                      className="post-image"
                      onError={(e) => (e.target.style.display = 'none')}
                    />
                  )}
                </>
              )}

              <div className="post-actions">
                <button 
                  className={`action-btn like-btn ${
                    post.likedBy?.includes(parseInt(user?.id || user?.sub)) ? 'liked' : ''
                  } ${likeAnimations[post.id] ? 'animate' : ''}`}
                  onClick={() => handleLike(post.id)}
                  disabled={!user}
                >
                  <FaHeart />
                  <span>{post.likeCount || 0}</span>
                </button>
                <button 
                  className={`action-btn favorite-btn ${localFavorites[post.id] ? 'active' : ''}`}
                  onClick={() => handleFavorite(post.id)}
                  disabled={!user}
                >
                  <FaStar />
                  <span>{post.favoriteCount || 0}</span>
                </button>
                <button 
                  className={`action-btn comment-btn ${expandedComments[post.id] ? 'active' : ''}`}
                  onClick={() => toggleComments(post.id)}
                >
                  <FaComment />
                  <span>{post.comments?.length || 0}</span>
                </button>
              </div>
              {expandedComments[post.id] && (
                <div className="comments-section">
                  <CommentSection
                    postId={post.id}
                    comments={post.comments}
                    onCommentSubmit={(content) => handleCommentSubmit(post.id, content)}
                  />
                </div>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default PostList;
