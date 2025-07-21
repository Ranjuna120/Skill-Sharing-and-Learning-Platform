import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { FaPlus, FaUser, FaEdit, FaTrash, FaRocket, FaChartLine, FaHeart, FaBrain } from 'react-icons/fa';
import { getProgress, deleteProgressUpdate, updateProgress } from '../services/api';
import { useAuth } from '../../contexts/AuthContext';
import './ProgressList.css';
import axios from 'axios';

const ProgressList = () => {
  const [updates, setUpdates] = useState([]);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);
  const { user } = useAuth();
  const [editingProgress, setEditingProgress] = useState(null);
  const [editForm, setEditForm] = useState({
    content: '',
    templateType: ''
  });

  useEffect(() => {
    const fetchUpdates = async () => {
      try {
        setLoading(true);
        const response = await getProgress();
        const updatesWithDetails = response.data.map(update => ({
          ...update,
          userName: update.userName || 'Anonymous',
          userProfileImage: update.userProfileImage || null
        }));
        setUpdates(updatesWithDetails);
      } catch (err) {
        setError('Failed to load progress updates: ' + err.message);
        console.error('Error fetching progress updates:', err);
        setUpdates([]);
      } finally {
        setLoading(false);
      }
    };

    fetchUpdates();
  }, []);

  const handleDeleteProgress = async (progressId) => {
    if (!window.confirm('Are you sure you want to delete this progress update?')) {
      return;
    }

    try {
      await deleteProgressUpdate(progressId, user.id);
      setUpdates(updates.filter(update => update.id !== progressId));
    } catch (err) {
      setError('Failed to delete progress update: ' + err.message);
    }
  };

  const handleEditProgress = async (progressId) => {
    try {
      if (!user) {
        setError('You must be logged in to update progress');
        return;
      }

      const userId = user.id || user.sub;
      if (!userId) {
        setError('Unable to determine user ID. Please log in again.');
        return;
      }

      const response = await updateProgress(progressId, {
        content: editForm.content,
        templateType: editForm.templateType
      }, userId);
      
      if (response) {
        setUpdates(updates.map(update => 
          update.id === progressId ? response : update
        ));
        setEditingProgress(null);
        setEditForm({ content: '', templateType: '' });
      } else {
        throw new Error('No data received from server');
      }
    } catch (error) {
      console.error('Error updating progress:', error);
      setError(`Failed to update progress: ${error.message}`);
    }
  };

  const startEditing = (update) => {
    setEditingProgress(update.id);
    setEditForm({
      content: update.content,
      templateType: update.templateType
    });
  };

  const cancelEditing = () => {
    setEditingProgress(null);
    setEditForm({ content: '', templateType: '' });
  };

  if (loading) {
    return (
      <div className="progress-list-container">
        <div className="loading-overlay">
          <div className="loading-content">
            <div className="loading-spinner-large"></div>
            <div className="loading-text">Loading progress updates...</div>
          </div>
        </div>
      </div>
    );
  }

  const getTemplateIcon = (templateType) => {
    switch(templateType) {
      case 'MILESTONE': return '🏆';
      case 'CHALLENGE': return '💪';
      case 'REFLECTION': return '🤔';
      default: return '📝';
    }
  };

  return (
    <div className="progress-list-container">
      <div className="progress-list-header">
        <div className="header-content">
          <h1 className="progress-list-title">
            <span className="title-icon">📈</span>
            Progress Gallery
          </h1>
          <p className="progress-list-subtitle">Track and celebrate your learning journey</p>
        </div>
        <Link to="/create-progress" className="create-button">
          <FaPlus className="create-icon" />
          <span>Share Progress</span>
        </Link>
      </div>

      {error && (
        <div className="error-card">
          <div className="error-content">
            <span className="error-icon">⚠️</span>
            <div className="error-message">{error}</div>
          </div>
        </div>
      )}

      {!loading && updates.length === 0 ? (
        <div className="empty-state">
          <div className="empty-icon">📊</div>
          <h3 className="empty-title">No Progress Updates Yet</h3>
          <p className="empty-description">
            Be the first to share your learning journey with the community!
          </p>
          <Link to="/create-progress" className="empty-action-btn">
            <FaRocket style={{marginRight: '0.5rem'}} />
            Create Your First Update
          </Link>
        </div>
      ) : (
        <div className="progress-list">
          {updates.map(update => (
            <div key={update.id} className="progress-card">
              <div className="card-header">
                <div className="user-profile">
                  <div className="avatar-container">
                    {update.userProfileImage ? (
                      <img 
                        src={update.userProfileImage} 
                        alt={update.userName} 
                        className="profile-image"
                      />
                    ) : (
                      <div className="default-avatar">
                        <FaUser className="avatar-icon" />
                      </div>
                    )}
                    <div className="user-info">
                      <span className="user-name">{update.userName}</span>
                      <span className="progress-date">
                        {new Date(update.createdAt).toLocaleDateString()}
                      </span>
                    </div>
                  </div>
                  <div className="template-badge">
                    <span className="template-icon">{getTemplateIcon(update.templateType)}</span>
                    <span className="template-text">{update.templateType}</span>
                  </div>
                </div>
                {user && update.userId === user.id && (
                  <div className="progress-actions">
                    <button 
                      className="action-btn edit-btn"
                      onClick={() => startEditing(update)}
                      title="Edit progress"
                    >
                      <FaEdit />
                    </button>
                    <button 
                      className="action-btn delete-btn"
                      onClick={() => handleDeleteProgress(update.id)}
                      title="Delete progress"
                    >
                      <FaTrash />
                    </button>
                  </div>
                )}
              </div>

              <div className="card-content">
                {editingProgress === update.id ? (
                  <div className="edit-form">
                    <div className="form-group-modern">
                      <label className="form-label-modern">Update Type</label>
                      <select
                        value={editForm.templateType}
                        onChange={(e) => setEditForm({ ...editForm, templateType: e.target.value })}
                        className="form-input-modern form-select-modern"
                      >
                        <option value="GENERAL">📝 General Update</option>
                        <option value="MILESTONE">🏆 Milestone</option>
                        <option value="CHALLENGE">💪 Challenge</option>
                        <option value="REFLECTION">🤔 Reflection</option>
                      </select>
                    </div>
                    <div className="form-group-modern">
                      <label className="form-label-modern">Progress Content</label>
                      <textarea
                        value={editForm.content}
                        onChange={(e) => setEditForm({ ...editForm, content: e.target.value })}
                        placeholder="Share your progress..."
                        className="form-input-modern form-textarea-modern"
                      />
                    </div>
                    <div className="edit-actions">
                      <button 
                        className="btn btn-primary btn-sm"
                        onClick={() => handleEditProgress(update.id)}
                      >
                        Save Changes
                      </button>
                      <button 
                        className="btn btn-outline btn-sm"
                        onClick={cancelEditing}
                      >
                        Cancel
                      </button>
                    </div>
                  </div>
                ) : (
                  <div className="progress-content">
                    <p className="progress-text">{update.content}</p>
                  </div>
                )}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default ProgressList;