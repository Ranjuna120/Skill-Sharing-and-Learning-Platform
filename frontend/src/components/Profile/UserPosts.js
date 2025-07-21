import React, { useEffect, useState } from 'react';
import { FaHeart, FaStar, FaComment, FaEdit, FaTrash } from 'react-icons/fa';
import { useRequireAuth } from '../../hooks/useRequireAuth';
import CommentSection from '../Post/CommentSection';
import axios from 'axios';
import './UserPosts.css';

const UserPosts = ({ userId }) => {
  const { user: currentUser } = useRequireAuth();
  const [posts, setPosts] = useState([]);
  const [error, setError] = useState('');
  const [expandedComments, setExpandedComments] = useState({});
  const [commentInputs, setCommentInputs] = useState({});
  const [editingPost, setEditingPost] = useState(null);
  const [editForm, setEditForm] = useState({
    title: '',
    description: ''
  });

  useEffect(() => {
    const fetchUserPosts = async () => {
      try {
        console.log('Fetching posts for user:', userId);
        const response = await axios.get(`http://localhost:8081/api/posts/user/${userId}`, {
          withCredentials: true
        });
        console.log('Fetched posts:', response.data);
        setPosts(response.data);
      } catch (error) {
        setError('Failed to fetch posts');
        console.error('Error fetching posts:', error);
      }
    };

    if (userId) {
      fetchUserPosts();
    }
  }, [userId]);

  const handleDeletePost = async (postId) => {
    if (!window.confirm('Are you sure you want to delete this post? This action cannot be undone.')) {
      return;
    }

    try {
      const response = await axios.delete(`http://localhost:8081/api/posts/${postId}`, {
        params: { userId: currentUser.id },
        withCredentials: true
      });

      if (response.status === 204) {
        setPosts(posts.filter(post => post.id !== postId));
        // Show success message
        const successMessage = document.createElement('div');
        successMessage.className = 'success-message';
        successMessage.textContent = 'Post deleted successfully';
        document.body.appendChild(successMessage);
        setTimeout(() => successMessage.remove(), 3000);
      }
    } catch (err) {
      setError('Failed to delete post: ' + (err.response?.data?.message || err.message));
      console.error('Error deleting post:', err);
    }
  };

  const handleEditPost = async (postId) => {
    if (!editForm.title.trim() || !editForm.description.trim()) {
      setError('Title and description are required');
      return;
    }

    try {
      const response = await axios.put(`http://localhost:8081/api/posts/${postId}`, 
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
        
        // Show success message
        const successMessage = document.createElement('div');
        successMessage.className = 'success-message';
        successMessage.textContent = 'Post updated successfully';
        document.body.appendChild(successMessage);
        setTimeout(() => successMessage.remove(), 3000);
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
      if (!currentUser) {
        setError('You must be logged in to like a post');
        return;
      }

      const userId = currentUser.id || currentUser.sub;
      if (!userId) {
        setError('Unable to determine user ID. Please log in again.');
        return;
      }

      // Update local state immediately for smooth animation
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
      await axios.post(`http://localhost:8081/api/posts/${postId}/like`, null, {
        params: { userId },
        withCredentials: true
      });
    } catch (error) {
      console.error('Error liking post:', error);
      setError('Failed to like post: ' + (error.response?.data?.message || error.message));
    }
  };

  const toggleComments = (postId) => {
    setExpandedComments(prev => ({
      ...prev,
      [postId]: !prev[postId]
    }));
  };

  const handleCommentSubmit = async (postId) => {
    try {
      if (!currentUser) {
        setError('You must be logged in to comment');
        return;
      }

      const comment = commentInputs[postId];
      if (!comment.trim()) {
        setError('Comment cannot be empty');
        return;
      }

      await axios.post(
        `http://localhost:8081/api/posts/${postId}/comments`,
        { content: comment },
        {
          params: { userId: currentUser.id },
          withCredentials: true
        }
      );

      // Clear comment input
      setCommentInputs(prev => ({ ...prev, [postId]: '' }));
      
      // Refresh comments
      const response = await axios.get(`http://localhost:8081/api/posts/${postId}/comments`, {
        withCredentials: true
      });
      
      setPosts(posts.map(post => 
        post.id === postId 
          ? { ...post, comments: response.data }
          : post
      ));
    } catch (error) {
      setError('Failed to add comment: ' + (error.response?.data?.message || error.message));
      console.error('Error adding comment:', error);
    }
  };

  if (!posts.length) {
    return (
      <div className="user-posts">
        <div className="no-posts">No posts yet</div>
      </div>
    );
  }

  return (
    <div className="user-posts">
      {error && <div className="error-message">{error}</div>}
      
      {posts.map(post => (
        <div key={post.id} className="post-card">
          <div className="post-header">
            <h3>{post.title}</h3>
            {currentUser && currentUser.id === post.user.id && (
              <div className="post-actions">
                <button className="edit-btn" onClick={() => startEditing(post)}>
                  <FaEdit /> Edit
                </button>
                <button className="delete-btn" onClick={() => handleDeletePost(post.id)}>
                  <FaTrash /> Delete
                </button>
              </div>
            )}
          </div>

          {editingPost === post.id ? (
            <div className="edit-form">
              <input
                type="text"
                value={editForm.title}
                onChange={(e) => setEditForm(prev => ({ ...prev, title: e.target.value }))}
                placeholder="Title"
              />
              <textarea
                value={editForm.description}
                onChange={(e) => setEditForm(prev => ({ ...prev, description: e.target.value }))}
                placeholder="Description"
              />
              <div className="edit-actions">
                <button onClick={() => handleEditPost(post.id)}>Save</button>
                <button onClick={cancelEditing}>Cancel</button>
              </div>
            </div>
          ) : (
            <>
              <p className="post-description">{post.description}</p>
              {post.image1 && (
                <img src={`data:image/jpeg;base64,${post.image1}`} alt="Post content" className="post-image" />
              )}
              <div className="post-interactions">
                <button
                  className={`like-btn ${post.likedBy?.includes(parseInt(currentUser?.id)) ? 'liked' : ''}`}
                  onClick={() => handleLike(post.id)}
                >
                  <FaHeart /> {post.likeCount || 0}
                </button>
                <button onClick={() => toggleComments(post.id)}>
                  <FaComment /> {post.comments?.length || 0}
                </button>
              </div>
              {expandedComments[post.id] && (
                <CommentSection
                  postId={post.id}
                  comments={post.comments}
                  onCommentAdded={(newComment) => {
                    setPosts(posts.map(p =>
                      p.id === post.id
                        ? { ...p, comments: [...p.comments, newComment] }
                        : p
                    ));
                  }}
                />
              )}
            </>
          )}
        </div>
      ))}
    </div>
  );
};

export default UserPosts; 