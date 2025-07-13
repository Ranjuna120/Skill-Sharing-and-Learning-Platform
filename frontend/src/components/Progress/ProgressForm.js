import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import axios from 'axios';
import './ProgressForm.css';
import { toast } from 'react-hot-toast';
import { deleteProgressUpdate } from '../services/api';

const ProgressForm = () => {
  const navigate = useNavigate();
  const { user } = useAuth();
  const [formData, setFormData] = useState({
    content: '',
    templateType: 'GENERAL'
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [progressUpdates, setProgressUpdates] = useState([]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    if (!user) {
      setError('You must be logged in to create a progress update');
      setLoading(false);
      return;
    }

    const userId = user.sub || user.id;
    if (!userId) {
      setError('Unable to determine user ID. Please log in again.');
      setLoading(false);
      return;
    }

    try {
      await axios.post('http://localhost:8081/api/progress', 
        { ...formData },
        {
          params: { userId },
          withCredentials: true,
          headers: {
            'Content-Type': 'application/json'
          }
        }
      );
      navigate('/progress');
    } catch (error) {
      setError('Failed to create progress update: ' + (error.response?.data?.message || error.message));
      console.error('Error creating progress update:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleDelete = async (progressId) => {
    try {
      if (!user) {
        console.error('User not authenticated');
        return;
      }
      
      await deleteProgressUpdate(progressId, user.sub);
      toast.success('Progress update deleted successfully');
      navigate('/progress');
    } catch (error) {
      console.error('Error deleting progress update:', error);
      toast.error(error.message || 'Failed to delete progress update');
    }
  };

  if (!user) {
    return (
      <div className="page-container">
        <div className="card card-error">
          <div className="card-content text-center">
            <h2>Authentication Required</h2>
            <p>Please log in to create a progress update.</p>
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
      <div className="form-modern animate-fade-in">
        <div className="form-header">
          <h1 className="form-title-modern">Share Your Progress</h1>
          <p className="form-subtitle">Update the community on your learning journey</p>
        </div>
        
        {error && <div className="form-error">⚠️ {error}</div>}
        
        <form onSubmit={handleSubmit}>
          <div className="form-group-modern">
            <label className="form-label-modern" htmlFor="templateType">Update Type</label>
            <select
              id="templateType"
              name="templateType"
              value={formData.templateType}
              onChange={handleChange}
              className="form-input-modern form-select-modern"
              required
            >
              <option value="GENERAL">📝 General Update</option>
              <option value="MILESTONE">🏆 Milestone</option>
              <option value="CHALLENGE">💪 Challenge</option>
              <option value="REFLECTION">🤔 Reflection</option>
            </select>
          </div>
          
          <div className="form-group-modern">
            <label className="form-label-modern" htmlFor="content">Progress Content</label>
            <textarea
              id="content"
              name="content"
              value={formData.content}
              onChange={handleChange}
              className="form-input-modern form-textarea-modern"
              placeholder="Share your progress, challenges, achievements, or insights..."
              required
            />
          </div>
          
          <div className="form-actions-modern center">
            <button 
              type="button" 
              className="btn btn-outline" 
              onClick={() => navigate('/progress')}
              disabled={loading}
            >
              Cancel
            </button>
            <button 
              type="submit" 
              className={`btn btn-primary btn-lg ${loading ? 'loading' : ''}`}
              disabled={loading}
            >
              {loading ? (
                <>
                  <div className="spinner-small"></div>
                  Creating...
                </>
              ) : (
                <>
                  <span>📈</span>
                  Create Update
                </>
              )}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default ProgressForm;