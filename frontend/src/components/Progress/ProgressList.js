import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { FaPlus, FaUser, FaEdit, FaTrash } from 'react-icons/fa';
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
      <div className="content-container">
        <div className="loading-spinner">Loading progress updates...</div>
      </div>
    );
  }

  return (
    <div className="content-container">
      <div className="page-header">
        <h1 className="page-title">📈 Progress Updates</h1>
        <Link to="/create-progress" className="btn btn-primary">
          <FaPlus style={{marginRight: '0.5rem'}} />
          Create Progress Update
        </Link>
      </div>
      {error && <div className="form-error">{error}</div>}
      {!loading && updates.length === 0 ? (
        <div className="card card-info">
          <div className="card-content text-center">
            <p>📊 No progress updates available</p>
            <p style={{color: '#718096', fontSize: '0.875rem', marginBottom: '1rem'}}>Share your learning progress with the community!</p>
            <Link to="/create-progress" className="btn btn-primary">
              <FaPlus style={{marginRight: '0.5rem'}} />
              Create Your First Update
            </Link>
          </div>
        </div>
      ) : (
        <div className="progress-list">
          {updates.map(update => (
            <div key={update.id} className="card progress-card">
              <div className="card-content">
                <div className="progress-user-info">
                  <div className="user-profile">
                    {update.userProfileImage ? (
                      <img 
                        src={update.userProfileImage} 
                        alt={update.userName} 
                        className="profile-image"
                      />
                    ) : (
                    <FaUser className="default-profile-icon" />
                  )}
                  <span className="user-name">{update.userName}</span>
                </div>
                {user && update.userId === user.id && (
                  <div className="progress-actions-owner">
                    <button 
                      className="edit-btn"
                      onClick={() => startEditing(update)}
                    >
                      <FaEdit />
                    </button>
                    <button 
                      className="delete-btn"
                      onClick={() => handleDeleteProgress(update.id)}
                    >
                      <FaTrash />
                    </button>
                  </div>
                )}
              </div>
              {editingProgress === update.id ? (
                <div className="edit-form">
                  <select
                    value={editForm.templateType}
                    onChange={(e) => setEditForm({ ...editForm, templateType: e.target.value })}
                    className="edit-input"
                  >
                    <option value="GENERAL">General Update</option>
                    <option value="MILESTONE">Milestone</option>
                    <option value="CHALLENGE">Challenge</option>
                    <option value="REFLECTION">Reflection</option>
                  </select>
                  <textarea
                    value={editForm.content}
                    onChange={(e) => setEditForm({ ...editForm, content: e.target.value })}
                    placeholder="Share your progress..."
                    className="edit-textarea"
                  />
                  <div className="edit-actions">
                    <button 
                      className="save-btn"
                      onClick={() => handleEditProgress(update.id)}
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
                <div className="progress-content">
                  <p className="progress-text">{update.content}</p>
                  <p className="progress-type">
                    <strong>Type:</strong> {update.templateType}
                  </p>
                  <p className="progress-date">
                    <strong>Posted:</strong> {new Date(update.createdAt).toLocaleString()}
                  </p>
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