import React, { useEffect, useState } from 'react';
import { useAuth } from '../../contexts/AuthContext';
import { getComments, addComment, updateComment, deleteComment } from '../services/api';
import { FaEdit, FaTrash } from 'react-icons/fa';
import './CommentSection.css'; // Import the CSS file

const CommentSection = ({ postId }) => {
  const { user } = useAuth();
  const [comments, setComments] = useState([]); // Initial state is an empty array
  const [newComment, setNewComment] = useState('');
  const [error, setError] = useState(null);
  const [editingComment, setEditingComment] = useState(null);
  const [editForm, setEditForm] = useState({
    content: ''
  });

  useEffect(() => {
    if (!postId) return; // Don't fetch if postId is undefined
    
    getComments(postId)
      .then(response => {
        // The response is already the array of comments
        setComments(Array.isArray(response) ? response : []);
      })
      .catch(err => {
        setError('Failed to load comments.');
        console.error('Error fetching comments:', err);
        setComments([]); // Reset to empty array on error
      });
  }, [postId]);

  const handleAddComment = async () => {
    if (!user) {
      setError('You must be logged in to comment.');
      return;
    }

    if (!user.id) {
      setError('Unable to determine user ID. Please log in again.');
      return;
    }

    if (!newComment.trim()) {
      setError('Comment cannot be empty.');
      return;
    }

    try {
      await addComment(postId, newComment, user.id);
      setNewComment('');
      setError(null);
      // Fetch updated comments
      const updatedComments = await getComments(postId);
      setComments(Array.isArray(updatedComments) ? updatedComments : []);
    } catch (err) {
      setError('Failed to add comment.');
      console.error('Error adding comment:', err);
    }
  };

  const handleEditComment = async (commentId) => {
    if (!editForm.content.trim()) {
      setError('Comment cannot be empty.');
      return;
    }

    try {
      await updateComment(commentId, editForm.content);
      setEditingComment(null);
      setEditForm({ content: '' });
      setError(null);
      const updatedComments = await getComments(postId);
      setComments(Array.isArray(updatedComments) ? updatedComments : []);
      
      // Show success message
      const successMessage = document.createElement('div');
      successMessage.className = 'success-message';
      successMessage.textContent = 'Comment updated successfully';
      document.body.appendChild(successMessage);
      setTimeout(() => successMessage.remove(), 3000);
    } catch (err) {
      setError('Failed to update comment.');
      console.error('Error updating comment:', err);
    }
  };

  const handleDeleteComment = async (commentId) => {
    if (!window.confirm('Are you sure you want to delete this comment?')) {
      return;
    }

    try {
      await deleteComment(commentId);
      setComments(comments.filter(comment => comment.id !== commentId));
      
      // Show success message
      const successMessage = document.createElement('div');
      successMessage.className = 'success-message';
      successMessage.textContent = 'Comment deleted successfully';
      document.body.appendChild(successMessage);
      setTimeout(() => successMessage.remove(), 3000);
    } catch (err) {
      setError('Failed to delete comment.');
      console.error('Error deleting comment:', err);
    }
  };

  const startEditing = (comment) => {
    setEditingComment(comment.id);
    setEditForm({
      content: comment.content
    });
  };

  const cancelEditing = () => {
    setEditingComment(null);
    setEditForm({ content: '' });
  };

  const isCommentOwner = (comment) => {
    if (!user || !comment || !comment.userId) {
      return false;
    }
    const currentUserId = user.id || user.sub;
    return currentUserId === comment.userId;
  };

  return (
    <div className="comment-section">
      <h3 className="comment-section-title">Comments</h3>
      {error && <p className="comment-error">{error}</p>}
      {comments.length === 0 ? (
        <p className="no-comments">No comments yet. Be the first to comment!</p>
      ) : (
        <div className="comments-list">
          {comments.map(comment => (
            <div key={comment.id} className="comment-item">
              <div className="comment-header">
                <div className="comment-user-info">
                  <img 
                    src={comment.userProfileImage || 'https://via.placeholder.com/40'} 
                    alt={comment.userName} 
                    className="comment-user-avatar"
                  />
                  <span className="comment-user-name">{comment.userName}</span>
                </div>
                {isCommentOwner(comment) && (
                  <div className="comment-actions">
                    <button 
                      className="edit-btn"
                      onClick={() => startEditing(comment)}
                      title="Edit comment"
                    >
                      <FaEdit />
                    </button>
                    <button 
                      className="delete-btn"
                      onClick={() => handleDeleteComment(comment.id)}
                      title="Delete comment"
                    >
                      <FaTrash />
                    </button>
                  </div>
                )}
              </div>
              {editingComment === comment.id ? (
                <div className="edit-form">
                  <textarea
                    value={editForm.content}
                    onChange={(e) => setEditForm({ ...editForm, content: e.target.value })}
                    placeholder="Edit your comment..."
                    className="edit-textarea"
                  />
                  <div className="edit-actions">
                    <button 
                      className="save-btn"
                      onClick={() => handleEditComment(comment.id)}
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
                <p className="comment-content">{comment.content}</p>
              )}
              <span className="comment-date">
                {new Date(comment.createdAt).toLocaleDateString()}
              </span>
            </div>
          ))}
        </div>
      )}
      <div className="comment-input-group">
        <input
          value={newComment}
          onChange={e => setNewComment(e.target.value)}
          placeholder={user ? "Add a comment..." : "Please login to comment"}
          className="comment-input"
          disabled={!user}
        />
        <button 
          onClick={handleAddComment} 
          className="comment-btn"
          disabled={!user || !newComment.trim()}
        >
          {user ? 'Comment' : 'Login to Comment'}
        </button>
      </div>
    </div>
  );
};

export default CommentSection;